package com.muhammadali.alarmme.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

fun interface NavigationGraph {
    fun getScreen(destination: NavDestination): Screen
}

val LocalNavGraph = compositionLocalOf<NavigationGraph> { error("No navigation graph provided") }

/** Mirrors ProvideNavResultRepository: the composition root installs the graph once. */
@Composable
fun ProvideNavGraph(graph: NavigationGraph, content: @Composable () -> Unit) =
    CompositionLocalProvider(value = LocalNavGraph provides graph, content = content)

fun Navigator.navigateTo(destination: NavDestination, navigationGraph: NavigationGraph) =
    push(navigationGraph.getScreen(destination))