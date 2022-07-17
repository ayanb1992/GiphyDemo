package com.example.giphydemo.util

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object HapticHelper {
    private var job: Job? = null

    fun vibrate(context: Context, duration: Long) {
        cancelVibration()
        job = CoroutineScope(Dispatchers.Default).launch {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }

    fun cancelVibration() {
        job?.cancel()
    }
}