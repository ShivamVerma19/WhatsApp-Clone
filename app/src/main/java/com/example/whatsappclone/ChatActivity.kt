package com.example.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

const val ID = "ID"
const val NAME = "NAME"
const val PHOTO = "PHOTO"
class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }
}