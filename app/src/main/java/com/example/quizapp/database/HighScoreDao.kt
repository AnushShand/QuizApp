package com.example.quizapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HighScoreDao {
    @Insert
    fun insert(highScore: HighScore)

    @Query("DELETE FROM sqlite_sequence WHERE name = 'High_Score_Table'")
    fun reset()

    @Query("DELETE FROM High_Score_Table")
    fun clear()

    @Query("SELECT * from High_Score_Table ORDER BY score DESC, timeTaken ASC")
    fun getHighScore(): List<HighScore>
}