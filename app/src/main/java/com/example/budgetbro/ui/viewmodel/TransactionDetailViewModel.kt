package com.example.budgetbro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.data.local.repo.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TransactionDetailViewModel(
    private val txId: String,
    private val repo: TransactionRepository
) : ViewModel() {
    val transaction: StateFlow<TransactionEntity?> =
        repo.observeById(txId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class TransactionDetailViewModelFactory(
    private val txId: String,
    private val repo: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionDetailViewModel(txId, repo) as T
    }
}
