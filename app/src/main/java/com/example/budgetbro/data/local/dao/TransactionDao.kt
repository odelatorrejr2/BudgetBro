package com.example.budgetbro.data.local.dao

import androidx.room.*
import com.example.budgetbro.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import com.example.budgetbro.data.local.model.TransactionTotals

@Dao
interface TransactionDao {

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :uid
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun observeRecent(uid: String, limit: Int = 100): Flow<List<TransactionEntity>>

    @Query("""
        SELECT * FROM transactions
        WHERE userId = :uid AND type = :type
        ORDER BY date DESC
        LIMIT :limit
    """)
    fun observeByType(uid: String, type: String, limit: Int = 100): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE transactionId = :id LIMIT 1")
    fun observeById(id: String): Flow<TransactionEntity?>

    @Query("""
    SELECT
        COALESCE(SUM(CASE WHEN type = 'income' THEN amount ELSE 0 END), 0) AS income,
        COALESCE(SUM(CASE WHEN type = 'expense' THEN amount ELSE 0 END), 0) AS expense
    FROM transactions
    WHERE userId = :uid
""")
    fun observeTotals(uid: String): Flow<TransactionTotals>

    @Query("""
    SELECT * FROM transactions
    WHERE userId = :uid AND categoryId = :categoryId
    ORDER BY date DESC
""")
    fun observeByCategory(uid: String, categoryId: String): Flow<List<TransactionEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(txn: TransactionEntity)

    @Delete
    suspend fun delete(txn: TransactionEntity)
}
