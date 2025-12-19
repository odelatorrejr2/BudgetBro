package com.example.budgetbro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.budgetbro.ui.viewmodel.DashboardViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    vm: DashboardViewModel,
    onGoCategories: () -> Unit,
    onGoTransactions: () -> Unit,
    onAddTransaction: () -> Unit,
    onOpenTransaction: (String) -> Unit,
    onSignOut: () -> Unit
) {
    val totals by vm.totals.collectAsStateWithLifecycle()
    val recent by vm.recent.collectAsStateWithLifecycle()

    val balance = totals.income - totals.expense

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ElevatedCard(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "Balance: $${"%.2f".format(balance)}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text("Income:  $${"%.2f".format(totals.income)}")
                        Text("Expense: $${"%.2f".format(totals.expense)}")
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onAddTransaction,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    OutlinedButton(
                        onClick = onGoTransactions,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Transactions", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }

                    OutlinedButton(
                        onClick = onGoCategories,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Categories", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }

            item {
                Text("Recent", style = MaterialTheme.typography.titleMedium)
            }

            if (recent.isEmpty()) {
                item {
                    Text("No transactions yet. Add one to get started.")
                }
            } else {
                items(recent) { t ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenTransaction(t.transactionId) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text("${t.type.uppercase()}  $${"%.2f".format(t.amount)}")
                                if (t.note.isNotBlank()) Text(t.note)
                            }
                            Text(">")
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }
            }
        }
    }
}
