package com.economiaon.domain.usecase

import com.economiaon.data.repo.UserRepository
import com.economiaon.domain.model.User
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class SaveUserUseCase(private val userRepository: UserRepository) :
    UseCase<User, User>() {
    override suspend fun execute(param: User): Flow<User> {
        return userRepository.saveUser(param)
    }
}