package com.codeleg.apextrustbank

interface UserValueUpdateListener {
    fun onValueChanged()
    fun showNotification(message: String)
}
