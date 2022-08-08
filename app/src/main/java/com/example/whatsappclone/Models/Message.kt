package com.example.whatsappclone.Models

import android.content.Context
import android.text.format.DateUtils
import com.example.whatsappclone.Utils.formatAsHeader
import java.util.*

interface chatEvent{
    val sentAt : Date
}

data class Message(
    val msg : String ,
    val msgId : String ,
    val senderId : String ,
    val liked : Boolean = false ,
    override val sentAt : Date = Date() ,
    val status : Int = 1 ,
    val type : String = "TEXT"
) : chatEvent{
    constructor() : this("" , "" , "" , false , Date() , 1 , "")
}


data class DateHeader(
    override val sentAt: Date , val context : Context
) : chatEvent
{
    val date : String = sentAt.formatAsHeader(context)
}