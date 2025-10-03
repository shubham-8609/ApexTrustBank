package com.codeleg.apextrustbank

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.core.net.toUri

class DialogHelper {
    companion object{
    fun showSnacksbar(view: View, text: String){
        Snackbar.make(view , text , Snackbar.LENGTH_SHORT).show()
    }


    fun sendEmail(context: Context , view: View, sub:String , msg:String  ){
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri() // Only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL , arrayOf("shubhamgupta8609@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT , sub )
            putExtra(Intent.EXTRA_TEXT , msg)
            setPackage("com.google.android.gm")
        }
        try {
            context.startActivity(intent)
        } catch (e:Exception){
            Snackbar.make(view, "No proper app to send mail", Snackbar.LENGTH_LONG).show()


        }
    }




        }
}