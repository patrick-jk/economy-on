package com.economiaon.domain.usecase

import com.economiaon.data.repo.UserRepository
import com.economiaon.domain.model.User
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow

class FindUserByIdUseCase(private val userRepository: UserRepository) :
    UseCase<Long, User>() {
    override suspend fun execute(param: Long): Flow<User> {
        return userRepository.findUserById(param)
    }
}