package com.economiaon.domain.usecase

import com.economiaon.data.repo.UserRepository
import com.economiaon.domain.model.User
import com.economiaon.util.UseCase
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class FindUserByEmailUseCase(private val userRepository: UserRepository) :
    UseCase<String, Call<User>>() {
    override suspend fun execute(param: String): Flow<Call<User>> {
        return userRepository.findUserByEmail(param)
    }
}