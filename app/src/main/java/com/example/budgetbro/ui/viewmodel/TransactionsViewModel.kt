package com.example.budgetbro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.local.entity.TransactionEntity
import com.example.budgetbro.data.local.repo.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

enum class SortMode { DATE_DESC, AMOUNT_DESC, AMOUNT_ASC }

class TransactionsViewModel(
    private val uid: String,
    private val repo: TransactionRepository
) : ViewModel() {

    private val _typeFilter = MutableStateFlow<String?>(null)
    val typeFilter: StateFlow<String?> = _typeFilter

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _sortMode = MutableStateFlow(SortMode.DATE_DESC)
    val sortMode: StateFlow<SortMode> = _sortMode

    private val baseList: Flow<List<TransactionEntity>> =
        _typeFilter.flatMapLatest { type ->
            if (type == null) repo.observeRecent(uid)
            else repo.observeByType(uid, type)
        }

    val transactions: StateFlow<List<TransactionEntity>> =
        combine(baseList, _searchQuery, _sortMode) { list, query, sort ->
            val q = query.trim().lowercase()

            val filtered = if (q.isEmpty()) list else {
                list.filter { t ->
                    t.note.lowercase().contains(q) ||
                            t.type.lowercase().contains(q) ||
                            t.amount.toString().contains(q)
                }
            }

            when (sort) {
                SortMode.DATE_DESC -> filtered.sortedByDescending { it.date }
                SortMode.AMOUNT_DESC -> filtered.sortedByDescending { it.amount }
                SortMode.AMOUNT_ASC -> filtered.sortedBy { it.amount }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setTypeFilter(type: String?) { _typeFilter.value = type }
    fun setSearchQuery(q: String) { _searchQuery.value = q }
    fun setSortMode(mode: SortMode) { _sortMode.value = mode }

    fun delete(txn: TransactionEntity) = viewModelScope.launch { repo.delete(txn) }

    fun upsert(
        amount: Double,
        type: String,
        categoryId: String,
        date: Long,
        note: String,
        existing: TransactionEntity? = null
    ) = viewModelScope.launch {
        val entity = existing?.copy(
            amount = amount,
            type = type,
            categoryId = categoryId,
            date = date,
            note = note
        ) ?: TransactionEntity(
            transactionId = UUID.randomUUID().toString(),
            userId = uid,
            categoryId = categoryId,
            type = type,
            amount = amount,
            date = date,
            note = note
        )
        repo.upsert(entity)
    }
}

class TransactionsViewModelFactory(
    private val uid: String,
    private val repo: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionsViewModel(uid, repo) as T
    }
}
