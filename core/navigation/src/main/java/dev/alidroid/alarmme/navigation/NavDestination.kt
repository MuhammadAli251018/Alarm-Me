package dev.alidroid.alarmme.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@JvmInline
@Parcelize
value class ScreenId(val value: String) : Parcelable

@Parcelize
data class NavDestination(
    val screenId: ScreenId,
    val parameters: NavigationParameter? = null
) : Parcelable

/** Typed access to a destination's parameters, for the composition root's NavigationGraph. */
inline fun <reified T : NavigationParameter> NavDestination.requireParams(): T =
    parameters as? T
        ?: error("Destination ${screenId.value} has parameters=$parameters, expected ${T::class.simpleName}")