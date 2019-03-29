package com.gold.footimpression.net.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import com.gold.footimpression.R
import com.gold.footimpression.ui.event.EventHandler
import com.gold.footimpression.databinding.LayoutCommDialogBinding
import com.gold.footimpression.databinding.LayoutProgressDialogBinding


class DialogUtils {

    companion object {
        fun showCommDialog(
            context: Context,
            cancleAble: Boolean,
            callBack: (view: View, type: Int) -> Unit,
            title: String,
            content: String,
            positiveStr: String = context.getString(R.string.sure),
            negativeStr: String = context.getString(R.string.cancle)

        ): AlertDialog {
            var dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setCancelable(cancleAble)
            var binding = DataBindingUtil.inflate<LayoutCommDialogBinding>(
                LayoutInflater.from(context),
                R.layout.layout_comm_dialog,
                null,
                false
            );
            binding!!.negativeStr = negativeStr
            binding!!.positiveStr = positiveStr
            binding!!.title = title
            binding!!.content = content
            binding!!.click = object : EventHandler(context, false) {
                override fun onClickView(view: View?) {
                    super.onClickView(view)
                    when (view!!.id) {
                        R.id.tv_positive -> {
                            callBack(view, 1)
                        }
                        R.id.tv_negative -> {
                            callBack(view, 0)
                        }
                    }
                }
            }
            dialogBuilder.setView(binding!!.root)
            var dialog = dialogBuilder.create()
            dialog.show()
            return dialog
        }


        fun showProgressDialog(
            context: Context,
            cancelAble: Boolean,
            onDiss: (it: DialogInterface) -> Unit
        ): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context, R.style.translate_them)

            val binding = DataBindingUtil.inflate<LayoutProgressDialogBinding>(
                LayoutInflater.from(context),
                R.layout.layout_progress_dialog,
                null,
                false
            );
            dialogBuilder.setView(binding!!.root)
            val dialog = dialogBuilder.create()
            dialog.setOnDismissListener {
                onDiss(it)
            }
            dialog.setCancelable(cancelAble)
            dialog.show()
            return dialog
        }


        fun cancelDialog(dialog: Dialog?) {
            if (null != dialog && dialog.isShowing) {
                dialog.dismiss()
            }
        }

        fun destroyDialog(dialog: Dialog?) {
            if (null != dialog && dialog.isShowing) {
                dialog.dismiss()
            }
            var dialog = dialog
            dialog = null
        }
    }

}