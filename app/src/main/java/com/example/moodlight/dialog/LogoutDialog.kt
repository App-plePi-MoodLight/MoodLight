package com.example.moodlight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import com.example.moodlight.R

class LogoutDialog(
    context: Context,
    logoutDialogInterface: LogoutDialogInterface
    , mainMessage: String
    , message: String, checkBtn : String, cancleBtn : String)
    : Dialog(context) {

    private var mainMessage: String
    private var message: String
    private var checkBtn : String
    private var cancleBtn : String

    private var logoutDialogInterface : LogoutDialogInterface

    init {
        this.logoutDialogInterface = logoutDialogInterface
        this.mainMessage = mainMessage
        this.message = message
        this.checkBtn =  checkBtn
        this.cancleBtn = cancleBtn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_with_drawal)

        findViewById<TextView>(R.id.mainTv).text = mainMessage
        findViewById<TextView>(R.id.subTv).text = message
        findViewById<TextView>(R.id.checkBtn).text = checkBtn

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        findViewById<TextView>(R.id.checkBtn).setOnClickListener {
            this.logoutDialogInterface.onClickLogout()
        }
        findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            this.logoutDialogInterface.onCancelLogout()
        }
    }
}