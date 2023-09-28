package com.muhammadali.alarmme.feature.main.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract

class PickRingtoneContract : ActivityResultContract<String, Ringtone?>() {

    private lateinit var context: Context

    override fun createIntent(context: Context, input: String): Intent {
        this.context = context
        return Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Ringtone? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java) ?: return null

            else {
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI) ?: return null
            }
            val ringtoneTitle = RingtoneManager.getRingtone(context, uri).getTitle(context) ?: return null

            Ringtone(uri, ringtoneTitle)
        }
        else
            null
    }
}

data class Ringtone(
    val uri: Uri,
    val title: String
)