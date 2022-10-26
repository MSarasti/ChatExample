package edu.co.icesi.chatexample.model

data class Message(
    var id:String = "",
    var message:String = "",
    var date:Long = 0,
    var authorID:String = "0"
)