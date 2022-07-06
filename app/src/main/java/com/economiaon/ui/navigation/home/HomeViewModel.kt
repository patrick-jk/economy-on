package com.economiaon.ui.navigation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.model.Finance
import com.economiaon.data.model.User
import com.economiaon.data.repo.UserRepository
import com.economiaon.usecase.ListFinancesByUserIdUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class HomeViewModel(
    private val listFinancesByUserIdUseCase: ListFinancesByUserIdUseCase,
    private val userRepository: UserRepository) : ViewModel() {

    private val _finances = MutableLiveData<State>()
    val finances: LiveData<State> = _finances
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun getFinanceList(userId: Long) {
        viewModelScope.launch {
            listFinancesByUserIdUseCase.execute(userId)
                .onStart {
                    _finances.postValue(State.Loading)
                }
                .catch {
                    _finances.postValue(State.Error(it))
                }
                .collect {
                    _finances.postValue(State.Success(it))
                }
        }
    }

    fun getUserById(userId: Long) {
        viewModelScope.launch {
            val user = userRepository.findUserById(userId)
            user.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        _user.postValue(response.body())
                    } else {
                        _user.postValue(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _user.postValue(null)
                }

            })
        }
    }

    sealed class State {
        object Loading : State()
        data class Success(val list: List<Finance>) : State()
        data class Error(val error: Throwable) : State()
    }
}