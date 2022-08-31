package com.example.quizapp.screens.title

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//This viewmodel is used to share the name of the player between Title Fragment and GameEnd Fragment
class SharedNameViewModel: ViewModel() {
        val name = MutableLiveData<String>()
        fun changeName(updatedName:String){
                name.value=updatedName
        }
}