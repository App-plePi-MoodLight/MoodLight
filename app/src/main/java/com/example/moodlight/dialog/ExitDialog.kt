package com.example.moodlight.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.TextView
import com.example.moodlight.R

class ExitDialog(
    context: Context,
    exitDialogInterface: ExitDialogInterface
    , mainMessage: String
    , message: String, checkBtn : String, cancleBtn : String)
    : Dialog(context) {

    private var mainMessage: String
    private var message: String
    private var checkBtn : String
    private var cancleBtn : String

    private var exitDialogInterface : ExitDialogInterface

    init {
        this.exitDialogInterface = exitDialogInterface
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
        findViewById<TextView>(R.id.checkBtn2).text = checkBtn

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        findViewById<TextView>(R.id.checkBtn2).setOnClickListener {
            this.exitDialogInterface?.onCheckExitBtnClick()
        }
        findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            this.exitDialogInterface.onCancleExitBtnClick()
        }
    }
}