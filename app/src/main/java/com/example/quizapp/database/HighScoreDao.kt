package com.example.quizapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HighScoreDao {
    @Insert
    fun insert(highScore: HighScore)

    @Query("DELETE from High_Score_Table")
    fun clear()

    @Query("SELECT * from High_Score_Table ORDER BY score DESC, timeTaken ASC LIMIT :limit")
    fun getHighScore(limit:Int):LiveData<List<HighScore>>
}