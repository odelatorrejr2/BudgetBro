package com.example.budgetbro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.budgetbro.data.local.entity.CategoryEntity
import com.example.budgetbro.data.local.repo.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class CategoriesViewModel(
    private val uid: String,
    private val repo: CategoryRepository
) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> =
        repo.observeCategories(uid).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun upsert(name: String, existing: CategoryEntity? = null) = viewModelScope.launch {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return@launch

        val entity = if (existing == null) {
            CategoryEntity(
                categoryId = UUID.randomUUID().toString(),
                userId = uid,
                name = trimmed
            )
        } else {
            existing.copy(name = trimmed)
        }

        repo.upsert(entity)
    }

    fun delete(category: CategoryEntity) = viewModelScope.launch {
        repo.delete(category)
    }
}

class CategoriesViewModelFactory(
    private val uid: String,
    private val repo: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesViewModel(uid, repo) as T
    }
}
