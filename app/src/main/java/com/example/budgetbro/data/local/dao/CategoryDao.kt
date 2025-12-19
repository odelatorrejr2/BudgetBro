package com.example.budgetbro.data.local.dao

import androidx.room.*
import com.example.budgetbro.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE userId = :uid ORDER BY name ASC")
    fun observeCategories(uid: String): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(category: CategoryEntity)

    @Query("""
    SELECT * FROM categories
    WHERE userId = :uid AND categoryId = :categoryId
    LIMIT 1
""")
    fun observeById(uid: String, categoryId: String): Flow<CategoryEntity?>


    @Delete
    suspend fun delete(category: CategoryEntity)
}
