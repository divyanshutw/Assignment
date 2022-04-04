package com.assignment.physicswallah

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView

class DialogsUtil(val context: Context) {

    private var dialogListener: DialogListener? = null
    private val dialog = Dialog(context)

    init {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    fun showDialog(text: String, buttonText: String, clickListener: DialogListener): DialogsUtil{
        this.dialogListener = clickListener
        dialog.setContentView(R.layout.dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val dialogButton: Button = dialog.findViewById(R.id.dialog_button)
        val dialogText = dialog.findViewById<TextView>(R.id.dialog_text)
        dialogText.text = text
        dialogButton.text = buttonText
        dialogButton.setOnClickListener {
            dialogListener!!.onDialogButtonClicked(dialog)
        }
        dialog.setCancelable(false)
        dialogListener!!.onDialogReady(dialog, dialogText, dialogButton)
        dialog.setOnCancelListener {
            clickListener.onDialogCancelled(dialog)
        }
        dialog.show()

        return this
    }


    fun dismissDialog(){
        if(dialog.isShowing)
            dialog.dismiss()
    }

    interface DialogListener {
        fun onDialogButtonClicked(dialog: Dialog)
        fun onDialogReady(dialog: Dialog, dialogText: TextView, dialogButton: Button){}
        fun onDialogCancelled(dialog: Dialog){}
    }

}