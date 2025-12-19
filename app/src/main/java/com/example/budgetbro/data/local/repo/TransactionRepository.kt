package com.example.budgetbro.data.local.repo

import com.example.budgetbro.data.local.dao.TransactionDao
import com.example.budgetbro.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import com.example.budgetbro.data.local.model.TransactionTotals


class TransactionRepository(
    private val dao: TransactionDao
) {
    fun observeRecent(uid: String, limit: Int = 100): Flow<List<TransactionEntity>> =
        dao.observeRecent(uid, limit)

    fun observeByType(uid: String, type: String, limit: Int = 100): Flow<List<TransactionEntity>> =
        dao.observeByType(uid, type, limit)

    fun observeById(id: String) = dao.observeById(id)

    fun observeTotals(uid: String): Flow<TransactionTotals> = dao.observeTotals(uid)

    fun observeByCategory(uid: String, categoryId: String): Flow<List<TransactionEntity>> =
        dao.observeByCategory(uid, categoryId)


    suspend fun upsert(txn: TransactionEntity) = dao.upsert(txn)

    suspend fun delete(txn: TransactionEntity) = dao.delete(txn)
}
