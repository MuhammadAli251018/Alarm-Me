package com.muhammadali.alarmme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NavResultKey<T : NavigationResult>(val value: String)
class NavResultRepository {
    private val results: MutableStateFlow<Map<NavResultKey<*>, NavigationResult>> =
        MutableStateFlow(emptyMap())

    fun <T : NavigationResult> save(key: NavResultKey<T>, result: T) {
        results.update { it + (key to result) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : NavigationResult> awaitResult(key: NavResultKey<T>): Flow<T> {
        return results.mapNotNull { it[key] as T? }
    }

    fun consume(key: NavResultKey<*>) {
        results.update { it - key }
    }
}

@Composable
fun rememberNavResultRepository() = remember { NavResultRepository() }

val LocalNavResultRepository = compositionLocalOf<NavResultRepository> {
    error("No NavResultRepository provided")
}

@Composable
fun ProvideNavResultRepository(
    repository: NavResultRepository = rememberNavResultRepository(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        value = LocalNavResultRepository provides repository,
        content = content
    )
}

@Composable
fun AwaitNavResult(
    vararg keys: NavResultKey<out NavigationResult>,
    onResultReceived: (key: NavResultKey<out NavigationResult>, result: NavigationResult) -> Unit
) {
    val repository = LocalNavResultRepository.current
    val currentOnResultReceived by rememberUpdatedState(onResultReceived)

    LaunchedEffect(keys.toList()) {
        for (key in keys) {
            launch {
                repository.awaitResult(key).collect {
                    currentOnResultReceived(key, it)
                    repository.consume(key)
                }
            }
        }
    }
}


