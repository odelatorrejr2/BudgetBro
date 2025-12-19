package com.example.budgetbro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.ui.viewmodel.SortMode
import com.example.budgetbro.ui.viewmodel.TransactionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    vm: TransactionsViewModel,
    onBack: () -> Unit,
    onAdd: () -> Unit,
    onOpen: (String) -> Unit
) {
    val txns by vm.transactions.collectAsState()
    val filter by vm.typeFilter.collectAsState()
    val query by vm.searchQuery.collectAsState()
    val sortMode by vm.sortMode.collectAsState()

    var showDelete by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<TransactionEntity?>(null) }

    var sortMenu by remember { mutableStateOf(false) }

    if (showDelete && deleting != null) {
        AlertDialog(
            onDismissRequest = { showDelete = false; deleting = null },
            title = { Text("Delete transaction?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.delete(deleting!!)
                    showDelete = false
                    deleting = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false; deleting = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) { Text("+") }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ✅ Search
            OutlinedTextField(
                value = query,
                onValueChange = { vm.setSearchQuery(it) },
                label = { Text("Search (note, type, amount)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // ✅ Sort
            Box(Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { sortMenu = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val label = when (sortMode) {
                        SortMode.DATE_DESC -> "Sort: Newest"
                        SortMode.AMOUNT_DESC -> "Sort: Amount (High → Low)"
                        SortMode.AMOUNT_ASC -> "Sort: Amount (Low → High)"
                    }
                    Text(label, modifier = Modifier.weight(1f))
                    Text("▼")
                }

                DropdownMenu(
                    expanded = sortMenu,
                    onDismissRequest = { sortMenu = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(
                        text = { Text("Newest") },
                        onClick = { vm.setSortMode(SortMode.DATE_DESC); sortMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Amount (High → Low)") },
                        onClick = { vm.setSortMode(SortMode.AMOUNT_DESC); sortMenu = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Amount (Low → High)") },
                        onClick = { vm.setSortMode(SortMode.AMOUNT_ASC); sortMenu = false }
                    )
                }
            }

            // ✅ Type filter chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = filter == null,
                    onClick = { vm.setTypeFilter(null) },
                    label = { Text("All") }
                )
                FilterChip(
                    selected = filter == "expense",
                    onClick = { vm.setTypeFilter("expense") },
                    label = { Text("Expense") }
                )
                FilterChip(
                    selected = filter == "income",
                    onClick = { vm.setTypeFilter("income") },
                    label = { Text("Income") }
                )
            }

            if (txns.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No matching transactions.")
                    Spacer(Modifier.height(8.dp))
                    Text("Try clearing filters/search, or tap + to add one.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(txns) { t ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onOpen(t.transactionId) }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text("${t.type.uppercase()}  $${"%.2f".format(t.amount)}")
                                    if (t.note.isNotBlank()) Text(t.note)
                                }
                                TextButton(onClick = {
                                    deleting = t
                                    showDelete = true
                                }) { Text("Delete") }
                            }
                        }
                    }
                }
            }
        }
    }
}
