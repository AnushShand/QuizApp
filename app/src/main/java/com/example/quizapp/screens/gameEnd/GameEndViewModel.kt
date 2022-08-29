package com.example.quizapp.screens.gameEnd

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.quizapp.database.HighScore
import com.example.quizapp.database.HighScoreDao
import kotlinx.coroutines.*

class GameEndViewModel(private val score:Int,
                       private val timeTaken:Long,
                       private val database: HighScoreDao) : ViewModel()
{
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private suspend fun insert(scoreCard:HighScore)=withContext(Dispatchers.Default)
    {
        database.insert(scoreCard)
        Log.i("GameEnd","$scoreCard inserted")
    }
    fun insertLauncher()
    {
        uiScope.launch {
            val scoreCard=HighScore(name="Anush",score = score, timeTaken = timeTaken)
            insert(scoreCard)
        }
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}