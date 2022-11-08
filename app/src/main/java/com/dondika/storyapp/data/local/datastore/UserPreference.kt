package com.dondika.storyapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface IUserPreference

class UserPreference(private val dataStore: DataStore<Preferences>) : IUserPreference {

    suspend fun saveUser(token: String){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    fun fetchUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }
    }

    suspend fun deleteUser(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this){
                UserPreference(dataStore).also {
                    INSTANCE = it
                }
            }
        }

    }

}