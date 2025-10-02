package com.codeleg.apextrustbank

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.MessageDigest
import android.util.Base64
import java.security.SecureRandom

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
     val id: Int,
    @ColumnInfo(name = "username")
     var username: String,
    @ColumnInfo(name= "passwordHash")
     var passwordHash: String,
    @ColumnInfo(name = "balance")
     var balance: Double,
    @ColumnInfo(name = "accountNo")
     val accountNo:String,

) {




    companion object {
        fun create(username: String, plainPassword: String, balance: Double, id: Int): User {
            val hashed = hashPassword(plainPassword)
            return User(id, username, hashed, balance , generateAccountNo()
            )
        }
        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(hashBytes, Base64.NO_WRAP)
        }

        fun generateAccountNo(): String {
            val random = SecureRandom()
            val number = 10000000 + random.nextInt(90000000) // always 8 digits
            return number.toString()
        }

    }
}

