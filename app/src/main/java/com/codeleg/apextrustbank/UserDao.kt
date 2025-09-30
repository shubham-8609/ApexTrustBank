package com.codeleg.apextrustbank

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {

    @Dao
    interface UserDao {

        // --- READ ---
        @Query("SELECT * FROM users")
        suspend fun getAllUsers(): List<User>

        @Query("SELECT * FROM users WHERE id = :id")
        suspend fun getUserById(id: Int): User?

        @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
        suspend fun getUserByUsername(username: String): User?

        // --- CREATE ---
        @Insert
        suspend fun insertUser(user:User): Long

        @Insert
        suspend fun insertUsers(vararg users: User)

        // --- UPDATE ---
        @Update
        suspend fun updateUser(user: User)

        @Query("UPDATE users SET balance = :newBalance WHERE id = :id")
        suspend fun updateBalance(id: Int, newBalance: Double)

        @Query("UPDATE users SET passwordHash = :newHash WHERE id = :id")
        suspend fun updatePassword(id: Int, newHash: String)

        // --- DELETE ---
        @Delete
        suspend fun deleteUser(user: User)
    }

}