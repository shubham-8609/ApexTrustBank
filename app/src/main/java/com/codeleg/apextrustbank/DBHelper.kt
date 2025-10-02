package com.codeleg.apextrustbank

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Transaction::class], // Include all entities
    version = 1,
    exportSchema = false
)
abstract class DBHelper : RoomDatabase() {

    // 🔹 Add DAOs here
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        private const val DATABASE_NAME = "apextrustbankdb"



        @Volatile
        private var INSTANCE: DBHelper? = null

        fun getDB(context: Context): DBHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
