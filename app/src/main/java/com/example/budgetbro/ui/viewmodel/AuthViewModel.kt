package com.example.budgetbro.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.auth.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null
)

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    val user: StateFlow<FirebaseUser?> =
        repo.authState().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    fun signUp(email: String, password: String) = viewModelScope.launch {
        val err = validate(email, password)
        if (err != null) { _state.value = AuthUiState(error = err); return@launch }

        _state.value = AuthUiState(loading = true)
        try {
            repo.signUp(email.trim(), password)
            _state.value = AuthUiState()
        } catch (t: Throwable) {
            _state.value = AuthUiState(error = t.message ?: "Sign up failed.")
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        val err = validate(email, password)
        if (err != null) { _state.value = AuthUiState(error = err); return@launch }

        _state.value = AuthUiState(loading = true)
        try {
            repo.signIn(email.trim(), password)
            _state.value = AuthUiState()
        } catch (t: Throwable) {
            _state.value = AuthUiState(error = t.message ?: "Login failed.")
        }
    }

    fun signOut() = repo.signOut()

    private fun validate(email: String, password: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) return "Enter a valid email."
        if (password.length < 6) return "Password must be at least 6 characters."
        return null
    }
}

class AuthViewModelFactory(private val repo: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}
