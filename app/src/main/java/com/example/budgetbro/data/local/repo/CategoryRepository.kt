package com.example.budgetbro.data.local.repo

import com.example.budgetbro.data.local.dao.CategoryDao
import com.example.budgetbro.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val dao: CategoryDao
) {
    fun observeCategories(uid: String): Flow<List<CategoryEntity>> = dao.observeCategories(uid)

    fun observeById(uid: String, categoryId: String) =
        dao.observeById(uid, categoryId)

    suspend fun upsert(category: CategoryEntity) = dao.upsert(category)

    suspend fun delete(category: CategoryEntity) = dao.delete(category)
}
