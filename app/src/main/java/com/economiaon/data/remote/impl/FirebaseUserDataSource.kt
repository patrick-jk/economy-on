package com.economiaon.data.remote.impl

import com.economiaon.data.domain.UserDataSource
import com.economiaon.domain.model.User
import com.economiaon.presentation.statepattern.State
import com.economiaon.util.COLLECTION_INFO
import com.economiaon.util.COLLECTION_ROOT
import com.economiaon.util.USER_COLLECTION_ROOT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.suspendCoroutine

class FirebaseUserDataSource(
    private val firebaseAuth: FirebaseAuth,
    firebaseFirestore: FirebaseFirestore
) : UserDataSource {

    private val documentReference = firebaseFirestore
        .document("$COLLECTION_ROOT/$USER_COLLECTION_ROOT")

    private val userReference = documentReference.collection(COLLECTION_INFO)

    override suspend fun findUser(userId: String): User {
        try {
            val document = userReference.document(userId).get().await()
            return document.toObject(User::class.java)
                ?: throw NoSuchElementException("User not found for ID: $userId")
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun saveUser(user: User): User {
        return channelFlow {
            try {
                val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
                authResult.user?.sendEmailVerification()?.await()

                saveUserAfterAuth(user)

                channel.send(State.Success(user))
            } catch (e: Exception) {
                channel.send(State.Error(e))
            } finally {
                channel.close()
            }
        }.flowOn(Dispatchers.IO).toList().map {
            it as State.Success
            it.info
        }.first()
    }

    private suspend fun saveUserAfterAuth(user: User) {
        try {
            userReference.document(user.email).set(user).await()
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun updateUser(user: User): Void {
        return suspendCoroutine { continuation ->
            userReference.document(user.email).set(user).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }

    override suspend fun deleteUser(user: User): Void {
        return suspendCoroutine { continuation ->
            userReference.document(user.id).delete().addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }
}