package edu.co.icesi.chatexample.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainViewModel:ViewModel() {

    fun subscribeToMessages(){
        Firebase.firestore
            .collection("users")
            .addSnapshotListener{data, e ->

                for(docChanges in data?.documentChanges!!){
                    val user = docChanges.document.toObject(User::class.java)
                    Log.e(">>>","${user.id} ${user.name} ${docChanges.type.name}")
                }
            }
    }
}
data class User(
    val id:String = "",
    val name:String = ""
)