package com.example.budgetbro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.budgetbro.ui.components.AppFooterBar
import com.example.budgetbro.ui.navigation.AppNav
import com.example.budgetbro.ui.theme.BudgetBroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BudgetBroTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { AppFooterBar() }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNav()
                    }
                }
            }
        }
    }
}
