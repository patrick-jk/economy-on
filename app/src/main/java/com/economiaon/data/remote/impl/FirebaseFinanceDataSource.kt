package com.economiaon.data.remote.impl

import com.economiaon.data.domain.FinanceDataSource
import com.economiaon.domain.model.Finance
import com.economiaon.util.COLLECTION_INFO
import com.economiaon.util.COLLECTION_ROOT
import com.economiaon.util.FINANCE_COLLECTION_ROOT
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.suspendCoroutine

class FirebaseFinanceDataSource(
    firebaseFirestore: FirebaseFirestore
) : FinanceDataSource {

    private val documentReference = firebaseFirestore
        .document("$COLLECTION_ROOT/$FINANCE_COLLECTION_ROOT")


    private val financeReference = documentReference.collection(COLLECTION_INFO)

    override suspend fun getFinancesByUser(userEmail: String): List<Finance> {
        return suspendCoroutine { continuation ->
            financeReference.whereEqualTo("user_email", userEmail).get()
                .addOnSuccessListener { documents ->
                    val finances = mutableListOf<Finance>()
                    for (document in documents) {
                        document.toObject(Finance::class.java).run {
                            finances.add(this)
                        }
                    }
                    continuation.resumeWith(Result.success(finances))
                }.addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun saveFinance(finance: Finance): Finance {
        return suspendCoroutine { continuation ->
            financeReference.document(finance.id).set(finance).addOnSuccessListener {
                continuation.resumeWith(Result.success(finance))
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }

    override suspend fun updateFinance(finance: Finance): Void {
        return suspendCoroutine { continuation ->
            financeReference.document(finance.id).set(finance).addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }

    override suspend fun deleteAllFinancesByUser(userEmail: String): Void {
        return suspendCoroutine { continuation ->
            financeReference.document(userEmail).delete().addOnSuccessListener {
                continuation.resumeWith(Result.success(it))
            }.addOnFailureListener { exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }
}