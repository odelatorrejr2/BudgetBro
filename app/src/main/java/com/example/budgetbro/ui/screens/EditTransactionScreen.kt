package com.example.budgetbro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.budgetbro.data.local.entity.CategoryEntity
import com.example.budgetbro.ui.viewmodel.CategoriesViewModel
import com.example.budgetbro.ui.viewmodel.TransactionDetailViewModel
import com.example.budgetbro.ui.viewmodel.TransactionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    categoriesVm: CategoriesViewModel,
    txVm: TransactionsViewModel,
    detailVm: TransactionDetailViewModel,
    onBack: () -> Unit
) {
    val categories by categoriesVm.categories.collectAsStateWithLifecycle()
    val tx by detailVm.transaction.collectAsStateWithLifecycle()

    if (tx == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Transaction") },
                    navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
                )
            }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding).fillMaxSize()) {
                Text("Loading...", modifier = Modifier.padding(16.dp))
            }
        }
        return
    }

    var type by remember { mutableStateOf(tx!!.type) }
    var amountText by remember { mutableStateOf(tx!!.amount.toString()) }
    var note by remember { mutableStateOf(tx!!.note) }

    var selectedCategoryId by remember { mutableStateOf<String?>(tx!!.categoryId) }
    var menuOpen by remember { mutableStateOf(false) }

    val amountError = remember(amountText) {
        val v = amountText.toDoubleOrNull()
        if (v == null || v <= 0.0) "Enter a valid amount" else null
    }
    val categoryError = remember(selectedCategoryId, categories) {
        if (categories.isNotEmpty() && selectedCategoryId == null) "Pick a category" else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Transaction") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = type == "expense",
                    onClick = { type = "expense" },
                    label = { Text("Expense") }
                )
                FilterChip(
                    selected = type == "income",
                    onClick = { type = "income" },
                    label = { Text("Income") }
                )
            }

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Amount") },
                isError = amountError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (amountError != null) Text(amountError!!, color = MaterialTheme.colorScheme.error)

            if (categories.isEmpty()) {
                Text("No categories yet. Add a category first.", color = MaterialTheme.colorScheme.error)
            } else {
                val selectedName =
                    categories.firstOrNull { it.categoryId == selectedCategoryId }?.name ?: "Select category"

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { menuOpen = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(selectedName, modifier = Modifier.weight(1f))
                        Text("▼")
                    }

                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        categories.forEach { cat: CategoryEntity ->
                            DropdownMenuItem(
                                text = { Text(cat.name) },
                                onClick = {
                                    selectedCategoryId = cat.categoryId
                                    menuOpen = false
                                }
                            )
                        }
                    }
                }

                if (categoryError != null) Text(categoryError!!, color = MaterialTheme.colorScheme.error)
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            val canSave = amountError == null && (categories.isEmpty() || selectedCategoryId != null)

            Button(
                onClick = {
                    txVm.upsert(
                        amount = amountText.toDouble(),
                        type = type,
                        categoryId = selectedCategoryId ?: "",
                        date = tx!!.date,
                        note = note,
                        existing = tx!!
                    )
                    onBack()
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Save Changes") }
        }
    }
}
