package com.example.budgetbro.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val categoryId: String,
    val userId: String,
    val name: String,
    val iconName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
