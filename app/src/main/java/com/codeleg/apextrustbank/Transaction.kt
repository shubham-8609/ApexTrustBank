package com.codeleg.apextrustbank

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class  Transaction(
    @PrimaryKey(autoGenerate = true)
    val id:Int,

    val userId: Int,
    val amount: Double,
    val timeStamp: Date,
    val type: String
)
