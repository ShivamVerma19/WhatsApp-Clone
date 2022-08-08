package com.example.whatsappclone.Models

import java.util.*

data class Inbox(
    val msg: String,
    var from: String,
    var name: String,
    var image: String,
    var count: Int = 0 ,
    val time: Date = Date()
) {

    constructor() : this(""  , "" , "" , "" , 0 , Date())
}