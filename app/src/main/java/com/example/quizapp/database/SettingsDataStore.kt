package com.example.quizapp.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(context: Context)
{
    companion object{
        private val NAME= stringPreferencesKey("name")
        private val IS_DARK_MODE= booleanPreferencesKey("is_dark_mode")
    }
    suspend fun storeName(context:Context,name:String)
    {
        context.dataStore.edit{
            it[NAME]=name
        }
    }
    suspend fun storeMode(context: Context,mode:Boolean)
    {
        context.dataStore.edit {
            it[IS_DARK_MODE]=mode
        }
    }
    val nameFlow: Flow<String> = context.dataStore.data.map {
        it[NAME] ?: ""
    }

    val modeFlow:Flow<Boolean> = context.dataStore.data.map {
        it[IS_DARK_MODE]?:false
    }
}