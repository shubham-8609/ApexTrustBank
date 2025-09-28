package com.codeleg.apextrustbank

sealed class OperationResult {
    data class Success(val message: String, val newBalance: Double? = null, val newUser:User? = null) : OperationResult()
    data class Failure(val reason: String) : OperationResult()
}