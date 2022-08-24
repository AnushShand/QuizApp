package com.example.quizapp.screens.score
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.quizapp.database.HighScoreDao

class ScoreViewModelFactory(private val database:HighScoreDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoreViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}