package com.muhammadali.alarmme.utility.compose

import androidx.compose.ui.Modifier

inline fun Modifier.modifyIf(
    condition: Boolean,
    thenModifier: Modifier.() -> Modifier
): Modifier = if (condition) this.thenModifier() else this

inline fun Modifier.modifyIfElse(
    condition: Boolean,
    thenModifier: Modifier.() -> Modifier,
    otherwiseModifier: Modifier.() -> Modifier
): Modifier = if (condition) this.thenModifier() else this.otherwiseModifier()
