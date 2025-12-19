package com.example.budgetbro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.budgetbro.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authVm: AuthViewModel,
    onGoSignUp: () -> Unit
) {
    val state by authVm.state.collectAsStateWithLifecycle()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val emailError = remember(email) {
        val e = email.trim()
        if (e.isEmpty()) "Email required"
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(e).matches()) "Invalid email format"
        else null
    }

    val passwordError = remember(password) {
        if (password.isEmpty()) "Password required"
        else if (password.length < 6) "Password must be at least 6 characters"
        else null
    }

    val canSubmit = emailError == null && passwordError == null && !state.loading

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                isError = emailError != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (emailError != null) {
                Spacer(Modifier.height(4.dp))
                Text(emailError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                isError = passwordError != null,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (passwordError != null) {
                Spacer(Modifier.height(4.dp))
                Text(passwordError!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(16.dp))

            if (state.error != null) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { authVm.signIn(email.trim(), password) },
                enabled = canSubmit,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text("Login")
            }

            Spacer(Modifier.height(10.dp))

            OutlinedButton(
                onClick = onGoSignUp,
                enabled = !state.loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Sign Up")
            }
        }
    }
}
