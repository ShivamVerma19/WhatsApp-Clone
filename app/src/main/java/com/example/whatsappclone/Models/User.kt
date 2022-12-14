package com.example.whatsappclone.Models

import com.google.firebase.firestore.FieldValue

data class User(
    val name : String ,
    val imageUrl : String ,
    val thumbImage : String ,
    val uid : String ,
    val deviceToken : String ,
    val status : String ,
    val onlineStatus : Boolean
) {

    constructor() : this ("" , "" , "" , "" , "" , "" , false)

    constructor(name : String , imageUrl : String , thumbImage: String , uid : String ) : this (

              name ,
              imageUrl ,
              thumbImage ,
              uid ,
              "" ,
              "Hey there I am using WhatsApp" ,
              false
            )
}