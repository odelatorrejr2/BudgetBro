package com.example.budgetbro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.data.local.model.TransactionTotals
import com.example.budgetbro.data.local.repo.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val uid: String,
    private val repo: TransactionRepository
) : ViewModel() {

    val totals: StateFlow<TransactionTotals> =
        repo.observeTotals(uid)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TransactionTotals(0.0, 0.0))

    val recent: StateFlow<List<TransactionEntity>> =
        repo.observeRecent(uid, limit = 5)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

class DashboardViewModelFactory(
    private val uid: String,
    private val repo: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(uid, repo) as T
    }
}
