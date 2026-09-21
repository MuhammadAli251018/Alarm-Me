package com.muhammadali.alarmme.navigation

import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator

interface NavDestination {
    fun navigateTo(navigator: Navigator)
}

fun interface ScreenFactory {
    fun create(): Screen
}

internal class ScreenDestination(private val screenFactory: ScreenFactory) : NavDestination {
    override fun navigateTo(navigator: Navigator) {
        navigator.push(screenFactory.create())
    }
}

fun screenDestination(screen: Screen): NavDestination = ScreenDestination { screen }

fun screenDestination(screen: () -> Screen): NavDestination = ScreenDestination(screen)

fun Navigator.navigateTo(destination: NavDestination) =
    destination.navigateTo(this)