package com.example.budgetbro.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetbro.ui.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    authVm: AuthViewModel,
    onGoLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    val state by authVm.state.collectAsState()

    Column {
        Text("Sign Up")
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Password (min 6)") })

        Spacer(Modifier.height(12.dp))

        if (state.loading) CircularProgressIndicator()
        state.error?.let { Text(it) }

        Spacer(Modifier.height(12.dp))

        Button(onClick = { authVm.signUp(email, pass) }, enabled = !state.loading) {
            Text("Create Account")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoLogin, enabled = !state.loading) {
            Text("Back to Login")
        }
    }
}
