package dev.alidroid.alarmme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import dev.alidroid.alarmme.navigation.AlarmListDestination
import dev.alidroid.alarmme.navigation.ProvideNavGraph
import dev.alidroid.alarmme.navigation.ProvideNavResultRepository
import dev.alidroid.alarmme.ui.theme.AlarmMeTheme
import dev.alidroid.alarmme.ui.theme.AlarmMeThemeProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen().setKeepOnScreenCondition { !AppStartupInitializer.isReady.value }

        AppStartupInitializer.start()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val isReady = AppStartupInitializer.isReady.collectAsStateWithLifecycle()
            if (isReady.value) {
                AlarmMeApp()
            }
        }
    }
}

@Composable
fun AlarmMeApp() {
    RequestNotificationPermission()

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

// The ringing screen is raised by a full-screen-intent notification, so without this grant an alarm
// never rings. It lives here rather than in a feature because it belongs to the Activity hosting
// the app UI — RingingActivity deliberately doesn't ask, as a permission dialog over the lockscreen
// is useless.

@Composable
private fun RequestNotificationPermission() {
    // POST_NOTIFICATIONS only exists on 33+; below that the grant is implicit.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        // The system suppresses the dialog once denied, so there is nothing to react to.
        onResult = {}
    )

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
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
