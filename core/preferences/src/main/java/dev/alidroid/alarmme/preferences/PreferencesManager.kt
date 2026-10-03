package dev.alidroid.alarmme.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope

class PreferencesManager(private val context: Context, private val scope: CoroutineScope) {

    private val dataStores = mutableMapOf<String, DataStore<Preferences>>()

    private fun Context.dataStore(key: String): DataStore<Preferences> {
        return dataStores.getOrPut(key = key) {
            PreferenceDataStoreFactory.create {
                preferencesDataStoreFile(key)
            }
        }
    }

    fun getPreferenceOf(key: PrefRepositoryKey): PreferencesRepository {
        return PreferencesRepositoryImpl(context.dataStore(key.key), scope)
    }
}