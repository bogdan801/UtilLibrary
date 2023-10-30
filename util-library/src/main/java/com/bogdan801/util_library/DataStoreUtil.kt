package com.bogdan801.util_library

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val Context.intSettings: IntPreference get() = IntPreference(this)
val Context.stringSettings: StringPreference get() = StringPreference(this)

class IntPreference(private val context: Context) {
    operator fun get(key: String): Flow<Int?> {
        val dataStoreKey = intPreferencesKey(key)
        return flow {
            context.dataStore.data.collect { preferences ->
                emit(preferences[dataStoreKey])
            }
        }
    }

    suspend fun set(key: String, value: Int?){
        withContext(Dispatchers.IO){
            val dataStoreKey = intPreferencesKey(key)
            context.dataStore.edit { settings ->
                if(value != null) settings[dataStoreKey] = value
                else settings.remove(dataStoreKey)
            }
        }
    }
}

class StringPreference(private val context: Context){
    operator fun get(key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return flow {
            context.dataStore.data.collect { preferences ->
                emit(preferences[dataStoreKey])
            }
        }
    }

    suspend fun set(key: String, value: String?){
        withContext(Dispatchers.IO){
            val dataStoreKey = stringPreferencesKey(key)
            context.dataStore.edit { settings ->
                if(value != null) settings[dataStoreKey] = value
                else settings.remove(dataStoreKey)
            }
        }
    }
}