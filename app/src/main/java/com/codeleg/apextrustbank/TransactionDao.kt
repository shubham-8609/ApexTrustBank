package com.codeleg.apextrustbank

import androidx.room.*

@Dao
interface TransactionDao {

    // Insert new transaction
    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    // Update transaction (rare, but useful if you allow edits)
    @Update
    suspend fun updateTransaction(transaction: Transaction)

    // Delete transaction (rare too, mostly for admin or cleanup)
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    // Fetch all transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>

    // Fetch transactions by userId
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getTransactionsByUser(userId: Int): List<Transaction>

    // Fetch transactions by type (deposit / withdrawal)
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY timestamp DESC")
    suspend fun getTransactionsByType(type: String): List<Transaction>

    // Fetch last N transactions (useful for showing recent activity)
    @Query("SELECT * FROM transactions WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getRecentTransactions(userId: Int, limit: Int): List<Transaction>
}
