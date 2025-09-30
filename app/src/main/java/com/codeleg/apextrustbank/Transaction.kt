package com.codeleg.apextrustbank

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class  Transaction(
    @PrimaryKey(autoGenerate = true)
    val id:Int,

    val userId: Int,
    val amount: Double,
    val timeStamp:Long,
    val type: String
)
