package com.example.budgetbro.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.budgetbro.data.local.dao.CategoryDao
import com.example.budgetbro.data.local.dao.TransactionDao
import com.example.budgetbro.data.local.entity.CategoryEntity
import com.example.budgetbro.data.local.entity.TransactionEntity

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}
