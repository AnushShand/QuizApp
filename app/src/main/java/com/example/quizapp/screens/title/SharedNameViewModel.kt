package com.example.quizapp.screens.title

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedNameViewModel: ViewModel() {
        val name = MutableLiveData<String>()
        fun changeName(updatedName:String){
                name.value=updatedName
        }
}