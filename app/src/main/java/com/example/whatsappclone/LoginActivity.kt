package com.example.whatsappclone

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.auth.api.credentials.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    val CREDENTIAL_PICKER_REQUEST = 4019
    lateinit var countryCode : String
    lateinit var phoneNumber : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)





        phoneNumberEt.addTextChangedListener {
            val a = !(it.isNullOrEmpty() || it.length < 10)

            if(a){
                nxtButton.isEnabled = true
                nxtButton.setBackgroundColor(Color.BLACK)
            }
        }

        nxtButton.setOnClickListener {
            getNumber()
        }
    }

    private fun getNumber() {
        countryCode = ccp.selectedCountryCodeWithPlus
        phoneNumber = countryCode + phoneNumberEt.text.toString()

        notifyUser()
    }

    private fun notifyUser() {

        MaterialAlertDialogBuilder(this).apply {
            setMessage("We will be verifying the phone number : $phoneNumber \n Is this Ok or would you like to edit the number?")
            setPositiveButton("OK"){_ , _ ->
                startOTPScreenActivity()
            }
            setNegativeButton("Edit"){dialog  , which ->
                dialog.dismiss()
            }
            setCancelable(false)
            create()
            show()
        }

    }

    private fun startOTPScreenActivity() {
        val intent = Intent(this , OtpActivity::class.java)
        intent.putExtra(PHONE_NUMBER , phoneNumber)

        startActivity(intent)
        finish()
    }


}