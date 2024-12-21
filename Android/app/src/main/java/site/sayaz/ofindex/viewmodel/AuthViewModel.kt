package site.sayaz.ofindex.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import site.sayaz.ofindex.data.remote.RetrofitInstance
import site.sayaz.ofindex.data.remote.request.LoginRequest
import site.sayaz.ofindex.data.remote.request.RegisterRequest
import site.sayaz.ofindex.data.repository.AuthRepository

class AuthViewModel(
    private val authRepository: AuthRepository,
):ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    private val _isAuthed = MutableStateFlow(RetrofitInstance.hasToken())
    val isAuthed: StateFlow<Boolean> get() = _isAuthed


    init {
        viewModelScope.launch(Dispatchers.IO){
            val token = authRepository.getLocalToken()
            RetrofitInstance.setToken(token)
        }

        viewModelScope.launch {
            RetrofitInstance.token.collect { newToken ->
                _isAuthed.value = newToken != null
            }
        }
    }


    fun login(userid: Int, password: String) {
        val loginRequest = LoginRequest(userid, password)
        viewModelScope.launch(Dispatchers.IO){
            _loginState.value = LoginState.Loading
            val result =  authRepository.login(loginRequest)
            result
                .onSuccess { token ->
                    //save token
                    viewModelScope.launch {
                        authRepository.storeToken(token)
                    }
                    RetrofitInstance.setToken(token)
                    _loginState.value = LoginState.Success(token)
                }
                .onFailure { exception ->
                    _loginState.value = LoginState.Error("登录失败: ${exception.message}")
                    viewModelScope.launch {
                        delay(500)
                        _loginState.value = LoginState.Idle
                    }
                }
        }
    }

    fun register(userid: String, password: String) {
        val registerRequest = RegisterRequest(userid, password)
        viewModelScope.launch(Dispatchers.IO){
            _registerState.value = RegisterState.Loading
            val result =  authRepository.register(registerRequest)
            result
                .onSuccess { message ->
                    _registerState.value = RegisterState.Success(message)
                }
                .onFailure { exception ->
                    _registerState.value = RegisterState.Error("注册失败: ${exception.message}")
                    viewModelScope.launch {
                        delay(500)
                        _registerState.value = RegisterState.Idle
                    }
                }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }


}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val error: String) : LoginState()
}
sealed class RegisterState {
    data object Idle : RegisterState()
    data object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val error: String) : RegisterState()
}

