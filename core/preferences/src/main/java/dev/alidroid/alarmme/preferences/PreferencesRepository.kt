package dev.alidroid.alarmme.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer


@JvmInline
value class PrefRepositoryKey(val key: String)

interface PreferencesRepository {

    fun <T> getPrefValue(
        key: PreferencesKey,
        defaultValue: T,
        serializer: KSerializer<T>
    ): PreferencesValue<T>

    suspend fun <T> setPrefValue(key: PreferencesKey, value: T, serializer: KSerializer<T>)

    /** Removes every stored value, so each preference falls back to its declared default. */
    suspend fun clear()
}

inline fun <reified T> PreferencesRepository.getPrefValue(
    key: PreferencesKey,
    defaultValue: T
): PreferencesValue<T> {
    return getPrefValue(key, defaultValue, serializer())
}

suspend inline fun <reified T> PreferencesRepository.setPrefValue(key: PreferencesKey, value: T){
    return setPrefValue(key, value, serializer())
}

internal class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope
) : PreferencesRepository {

    private suspend fun getString(key: String): Flow<String?> {
        val key = stringPreferencesKey(key)
        return dataStore.data.map { preferences -> preferences[key] }
    }

    private suspend fun setString(key: String, value: String) {
        val key = stringPreferencesKey(key)
        dataStore.edit { preferences -> preferences[key] = value }
    }

    override fun <T> getPrefValue(key: PreferencesKey, defaultValue: T, serializer: KSerializer<T>): PreferencesValue<T> {
        return PreferencesValueImpl(defaultValue, key).also { value ->
            scope.launch {
                getString(key.value)
                    .map { it?.let { Json.decodeFromString(serializer, it) } ?: defaultValue }
                    .collect { emission -> value.set(emission) }
            }
        }
    }

    override suspend fun <T> setPrefValue(key: PreferencesKey, value: T, serializer: KSerializer<T>) {
        val str = Json.encodeToString(serializer, value)
        setString(key.value, str)
    }

    override suspend fun clear() {
        dataStore.edit { preferences -> preferences.clear() }
    }
}
