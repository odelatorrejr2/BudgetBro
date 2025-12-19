package com.example.budgetbro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.local.entity.CategoryEntity
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.data.local.repo.CategoryRepository
import com.example.budgetbro.data.local.repo.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CategorySummary(
    val income: Double = 0.0,
    val expense: Double = 0.0,
    val balance: Double = 0.0,
    val count: Int = 0
)

class CategoryDetailViewModel(
    private val uid: String,
    private val categoryId: String,
    private val catRepo: CategoryRepository,
    private val txRepo: TransactionRepository
) : ViewModel() {

    val category: StateFlow<CategoryEntity?> =
        catRepo.observeById(uid, categoryId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val transactions: StateFlow<List<TransactionEntity>> =
        txRepo.observeByCategory(uid, categoryId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val summary: StateFlow<CategorySummary> =
        transactions
            .combine(category) { txns, _ ->
                val income = txns.filter { it.type == "income" }.sumOf { it.amount }
                val expense = txns.filter { it.type == "expense" }.sumOf { it.amount }
                CategorySummary(
                    income = income,
                    expense = expense,
                    balance = income - expense,
                    count = txns.size
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CategorySummary())
}

class CategoryDetailViewModelFactory(
    private val uid: String,
    private val categoryId: String,
    private val catRepo: CategoryRepository,
    private val txRepo: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoryDetailViewModel(uid, categoryId, catRepo, txRepo) as T
    }
}
