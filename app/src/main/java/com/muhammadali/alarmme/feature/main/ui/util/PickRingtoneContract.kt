package com.muhammadali.alarmme.feature.main.ui.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract

class PickRingtoneContract : ActivityResultContract<String?, Uri?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)

            else
                intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        }
        else
            null
    }

}