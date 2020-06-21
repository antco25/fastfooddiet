package com.example.fastfooddiet.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

//TODO: About Dialog
class AboutDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setTitle("About")
                .setMessage("This is a fast food diet app")
                .setPositiveButton("Ok"){ _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}