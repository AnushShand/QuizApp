package com.example.quizapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "High_Score_Table")
data class HighScore(
    @PrimaryKey(autoGenerate = true)
    val id:Long,

//    @ColumnInfo
//    val date:Date=Calendar.getInstance().time,

    @ColumnInfo
    val score:Int,

    @ColumnInfo
    val timeTaken:Long
)
{
}