package com.codeleg.apextrustbank

import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

data class User(
    private var username: String,
    private var passwordHash: String,
    private var balance: Double,
    private val id: Int,
    private val accountNo:String,
    private val transactions: MutableList<Transaction> = mutableListOf()
) {



    // Getters
    fun getUsername(): String = username
    fun getBalance(): Double = balance
    fun getId(): Int = id
    fun getTransactions(): List<Transaction> = transactions.toList() // Immutable copy
    fun getAccountNo(): String = accountNo

    // Setters
    fun setUsername(newUsername: String) { username = newUsername }
    fun setBalance(newBalance: Double) { balance = newBalance }

    // Password check
    fun checkPassword(input: String) = hashPassword(input) == passwordHash

    // Change password safely
    fun changePassword(oldPass: String, newPass: String): OperationResult {
        return if (checkPassword(oldPass)) {
            if (newPass.isBlank()) {
                OperationResult.Failure("New password cannot be blank")
            } else {
                passwordHash = hashPassword(newPass)
                OperationResult.Success("Password changed successfully")
            }
        } else {
            OperationResult.Failure("Incorrect old password")
        }
    }

    // Deposit with validation
    fun deposit(amount: Double): OperationResult {
        return if (amount in 1.0..50000.0) {
            balance += amount
            val transaction = Transaction(amount , System.currentTimeMillis() , "Deposit" )
            transactions.add(transaction)
            OperationResult.Success("Deposit successful", balance)
        } else {
            OperationResult.Failure("Deposit must be between 1 and 50,000")
        }
    }

    // Withdraw
    fun withdraw(amount: Double): OperationResult {
        return when {
            amount <= 0 -> OperationResult.Failure("Withdrawal must be positive")
            amount > balance -> OperationResult.Failure("Insufficient balance")
            else -> {
                balance -= amount
                val transaction = Transaction( amount , System.currentTimeMillis() , "Withdraw")
                transactions.add(transaction)
                OperationResult.Success("Withdrawal successful", balance)
            }
        }
    }

    companion object {
        fun create(username: String, plainPassword: String, balance: Double, id: Int): User {
            val hashed = hashPassword(plainPassword)
            return User(username, hashed, balance, id , generateAccountNo()
            )
        }
        fun hashPassword(password: String): String {
            val digest = MessageDigest.getInstance("SHA-256")
            val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
            return Base64.getEncoder().encodeToString(hashBytes)
        }

        fun generateAccountNo(): String{
            return UUID.randomUUID().toString().take(8) // short unique acc no
        }
    }
}

