package com.example.quizapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HighScore::class], version = 4, exportSchema = false)
abstract class HighScoreDatabase:RoomDatabase() {
    abstract val highScoreDao:HighScoreDao
    companion object{
        @Volatile
        private var INSTANCE:HighScoreDatabase?=null

        fun getInstance(context: Context):HighScoreDatabase{
            synchronized(this)
            {
                var instance= INSTANCE
                if(instance==null)
                {
                    instance= Room.databaseBuilder(context.applicationContext,HighScoreDatabase::class.java,"high_score_database").fallbackToDestructiveMigration().build()
                }
                INSTANCE=instance
                return instance
            }
        }
    }

}