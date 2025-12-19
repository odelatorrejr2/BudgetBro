package com.example.budgetbro.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetbro.data.local.entity.CategoryEntity
import com.example.budgetbro.ui.viewmodel.CategoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    vm: CategoriesViewModel,
    onBack: () -> Unit,
    onOpen: (String) -> Unit
) {
    val categories by vm.categories.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<CategoryEntity?>(null) }

    var showDelete by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf<CategoryEntity?>(null) }

    if (showForm) {
        CategoryFormDialog(
            initialName = editing?.name.orEmpty(),
            title = if (editing == null) "Add Category" else "Edit Category",
            onDismiss = {
                showForm = false
                editing = null
            },
            onSave = { name ->
                vm.upsert(name, existing = editing)
                showForm = false
                editing = null
            }
        )
    }

    if (showDelete && deleting != null) {
        AlertDialog(
            onDismissRequest = { showDelete = false; deleting = null },
            title = { Text("Delete category?") },
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
                title = { Text("Categories") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editing = null
                showForm = true
            }) { Text("+") }
        }
    ) { innerPadding ->
        val pad = Modifier.padding(innerPadding)

        if (categories.isEmpty()) {
            Column(
                modifier = pad.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text("No categories yet.")
                Spacer(Modifier.height(8.dp))
                Text("Tap + to add your first category (Food, Gas, Rent, etc.).")
            }
        } else {
            LazyColumn(
                modifier = pad.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpen(cat.categoryId) } // ✅ tap opens detail
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(cat.name, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Tap to view transactions",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Row {
                                TextButton(onClick = {
                                    editing = cat
                                    showForm = true
                                }) { Text("Edit") }

                                TextButton(onClick = {
                                    deleting = cat
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

@Composable
private fun CategoryFormDialog(
    initialName: String,
    title: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    val error = remember(name) { if (name.trim().isEmpty()) "Name required" else null }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Category name") },
                    isError = error != null,
                    singleLine = true
                )
                if (error != null) {
                    Spacer(Modifier.height(6.dp))
                    Text(error, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onSave(name) },
                enabled = error == null
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
