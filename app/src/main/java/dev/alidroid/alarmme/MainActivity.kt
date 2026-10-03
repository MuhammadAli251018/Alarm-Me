package dev.alidroid.alarmme

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import dev.alidroid.alarmme.navigation.AlarmListDestination
import dev.alidroid.alarmme.navigation.ProvideNavGraph
import dev.alidroid.alarmme.navigation.ProvideNavResultRepository
import dev.alidroid.alarmme.ui.theme.AlarmMeTheme
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Before super.onCreate: this swaps the launch theme out for postSplashScreenTheme, and it
        // has to happen while the window is still being created.
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // AlarmMeScaffold paints every inset itself, so the system's contrast scrim is not needed.
        // Left on, it tints the 3-button navigation bar a cooler shade than the background behind
        // it — a visible band down the side of the window in landscape.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            AlarmMeApp()
        }
    }
}

@Composable
fun AlarmMeApp() {
    AlarmMeThemeProvider {
        ProvideNavResultRepository {
            ProvideNavGraph(AppNavigationGraph) {
                AlarmMeScaffold {
                    Navigator(screen = AppNavigationGraph.getScreen(AlarmListDestination))
                }
            }
        }
    }
}

// Should be inside :core:ui/component, and then should be used by each screen,
// as the project's conventions dictate that each screen must handle its padding.
// But as it's unlikely that a screen will occur in feature with different approach to handle the coloring
// of the system paddings, so we can use this here, and wrap all the screens inside.

@Composable
fun AlarmMeScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(AlarmMeTheme.colors.background)
    ) {
        content()
    }
}
