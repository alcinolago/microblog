package br.com.microblog.boticario.helper

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import br.com.microblog.boticario.R

class DialogProgressBar {

    companion object {

        private lateinit var dialog: Dialog

        fun show(context: Context) {

            dialog = AlertDialog.Builder(context)
                .setView(R.layout.dialog_loading)
                .setCancelable(false)
                .show()

            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        fun dismiss() {
            dialog.dismiss()
        }
    }
}
