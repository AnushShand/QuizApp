package com.example.quizapp.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizapp.database.HighScore
import com.example.quizapp.database.HighScoreDao
import kotlinx.coroutines.*

class ScoreViewModel(private val database: HighScoreDao) : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _data= MutableLiveData<List<HighScore>>()
    val data:LiveData<List<HighScore>>
        get()=_data

    private suspend fun getScores():List<HighScore>
    {
        return withContext(Dispatchers.Default){database.getHighScore()}
    }

    private suspend fun clearData()
    {
        //When clearing the data, also reset the index for auto-increment field ID
        withContext(Dispatchers.Default)
        {database.clear()
        database.reset()}
    }
    fun getData()
    {
        uiScope.launch {
            _data.value=getScores()
        }
    }
    fun clear()
    {
        uiScope.launch{
            clearData()
            _data.value= listOf()
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}