package com.economiaon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economiaon.data.model.Finance
import com.economiaon.data.model.User
import com.economiaon.data.repo.FinanceRepository
import com.economiaon.data.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection

class AddFinanceViewModel(private val financeRepository: FinanceRepository,
                          private val userRepository: UserRepository): ViewModel() {
    private val _isFinanceUpdated = MutableLiveData<Boolean>()
    val isFinanceUpdated: LiveData<Boolean> = _isFinanceUpdated
    private val _loggedUser = MutableLiveData<User?>()
    val loggedUser: LiveData<User?> = _loggedUser
    private val _isFinanceCreated = MutableLiveData<Boolean>()
    val isFinanceCreated: LiveData<Boolean> = _isFinanceCreated

    fun updateFinance(finance: Finance) {
        viewModelScope.launch(Dispatchers.IO) {
            val updateFinance = financeRepository.updateFinance(finance)
            updateFinance.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                        _isFinanceUpdated.postValue(true)
                    } else {
                        _isFinanceUpdated.postValue(false)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    _isFinanceUpdated.postValue(false)
                }

            })
        }
    }

    fun saveFinance(finance: Finance) {
        viewModelScope.launch(Dispatchers.IO) {
            val saveFinance = financeRepository.saveFinance(finance)
            saveFinance.enqueue(object : Callback<Finance> {
                override fun onResponse(call: Call<Finance>, response: Response<Finance>) {
                    if (response.code() == HttpURLConnection.HTTP_CREATED) {
                        _isFinanceCreated.postValue(true)
                    } else {
                        _isFinanceCreated.postValue(false)
                    }
                }

                override fun onFailure(call: Call<Finance>, t: Throwable) {
                    _isFinanceCreated.postValue(false)
                }

            })
        }
    }

    fun getUserById(userId:Long) {
        viewModelScope.launch {
            val userLogged = userRepository.findUserById(userId)
            userLogged.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        _loggedUser.postValue(response.body())
                    } else {
                        _loggedUser.postValue(null)
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    _loggedUser.postValue(null)
                }

            })
        }
    }
}