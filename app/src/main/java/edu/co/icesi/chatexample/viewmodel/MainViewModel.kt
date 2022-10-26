package edu.co.icesi.chatexample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import edu.co.icesi.chatexample.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    private var userID = ""
    private var otherUserID = ""

    private val arrayMessages:ArrayList<Message> = arrayListOf()
    private val _messages:MutableLiveData<ArrayList<Message>> = MutableLiveData(arrayListOf())
    val messages:LiveData<ArrayList<Message>> get() = _messages

    fun test(){
        Firebase.firestore
            .collection("messages")
            .document(chat.id)
            .collection("messages").addSnapshotListener{ data, e ->
                for(doc in data!!.documentChanges){
                    if(doc.type.name == "ADDED"){
                        val msg = doc.document.toObject(Message::class.java)
                        Log.e(">>>", msg.message)
                        arrayMessages.add(msg)
                        _messages.value = arrayMessages
                    }
                }

            }
    }
    fun subscribeToMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            var otherUserFound = false
            lateinit var otherChat:Chat
            val misChats = Firebase.firestore.collection("chats")
                .whereArrayContains("members",userID).get().await()
            for(doc in misChats.documents) {
                val chat = doc.toObject(Chat::class.java)
                if(chat!!.members.contains(otherUserID)){
                    otherUserFound = true
                    otherChat = chat
                    break
                }
            }
            if(otherUserFound){
                withContext(Dispatchers.Main){
                    subscribeRealTimeMessages(otherChat)
                }
            }else {
                //Generate chat
            }
        }
    }

    fun subscribeToMessagesEfficient(){
        viewModelScope.launch(Dispatchers.IO) {
           val result = Firebase.firestore
                .collection("chats")
                .document(userID)
                .collection("rooms")
                .whereEqualTo("friendID", otherUserID).get().await()

           for(doc in result) {
               val chat = doc.toObject(Chat::class.java)
               withContext(Dispatchers.Main){
                   subscribeRealTimeMessages(chat!!)
               }
           }

        }
    }

    fun subscribeRealTimeMessages(chat:Chat){
        Firebase.firestore
            .collection("chats")
            .document(chat.id)
            .collection("messages")
            .addSnapshotListener {data, e ->
                for(doc in data!!.documentChanges){
                    if(doc.type.name == "ADDED"){
                        val msg = doc.document.toObject(Message::class.java)
                        Log.e(">>>","${msg.message}")
                        arrayMessages.add(msg)
                        _messages.value = arrayMessages
                    }
                }
            }
    }
}
