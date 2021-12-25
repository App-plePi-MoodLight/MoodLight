package com.example.moodlight.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat.finishAffinity
import com.example.moodlight.R

class TerminationDialog(activity: Activity, context: Context) : Dialog(context) {

    private val activity = activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_termination_dialog)
        this.setCancelable(false)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        findViewById<AppCompatButton>(R.id.checkBtn).setOnClickListener {
            finishAffinity(activity)
            System.runFinalization();
            System.exit(0);
        }
    }
}