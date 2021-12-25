package com.example.moodlight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.moodlight.R

class LoadingDialog(context: Context) : Dialog(context) {

    // use : val dialog = LoadingDialog(context)
    // use : dialog.show()
    // if you want dialog dismiss, dialog.dismiss()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        this.setCancelable(false)
        this.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}