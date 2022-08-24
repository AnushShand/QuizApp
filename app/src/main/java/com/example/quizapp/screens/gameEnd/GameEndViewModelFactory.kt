package com.example.quizapp.screens.gameEnd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.database.HighScoreDao
import com.example.quizapp.screens.score.ScoreViewModel

class GameEndViewModelFactory(private val score:Int,
                              private val timeTaken:Long,
                              private val database:HighScoreDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameEndViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameEndViewModel(score,timeTaken,database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}