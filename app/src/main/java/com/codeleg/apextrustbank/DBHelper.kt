package com.codeleg.apextrustbank

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Transaction::class], // Include all entities
    version = 1,
    exportSchema = true  // Keep schema for versioning/migrations
)
abstract class DBHelper : RoomDatabase() {

    // 🔹 Add DAOs here
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private const val DATABASE_NAME = "apextrustdb"

        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getInstance(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    DATABASE_NAME
                )
                    // Optional: clears data if migration missing (good for dev only!)
                    .fallbackToDestructiveMigration()
                    // Optional: allows queries on main thread (avoid in prod!)
                    //.allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
