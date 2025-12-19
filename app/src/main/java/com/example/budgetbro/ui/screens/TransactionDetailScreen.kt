package com.example.budgetbro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.ui.viewmodel.TransactionDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    vm: TransactionDetailViewModel,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    val tx by vm.transaction.collectAsStateWithLifecycle()
    var showDelete by remember { mutableStateOf(false) }

    if (showDelete && tx != null) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Delete transaction?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(tx!!)
                    showDelete = false
                    onBack()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (tx == null) {
                Text("Loading...")
                return@Column
            }

            Text("Type: ${tx!!.type}")
            Text("Amount: $${tx!!.amount}")
            Text("Note: ${if (tx!!.note.isBlank()) "(none)" else tx!!.note}")

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onEdit(tx!!.transactionId) }) { Text("Edit") }
                OutlinedButton(onClick = { showDelete = true }) { Text("Delete") }
            }
        }
    }
}
