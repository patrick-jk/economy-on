package com.economiaon.domain

import com.economiaon.data.repo.UserRepository
import com.economiaon.domain.model.User
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class UpdateUserUseCase(private val userRepository: UserRepository) :
    UseCase<User, Void>() {
    override suspend fun execute(param: User): Flow<Void> {
        return userRepository.updateUser(param)
    }
}