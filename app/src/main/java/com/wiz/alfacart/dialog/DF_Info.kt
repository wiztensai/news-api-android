package com.wiz.alfacart.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import com.wiz.alfacart.R
import com.wiz.alfacart.base.BaseDialogFragment
import com.wiz.alfacart.databinding.DfInfoBinding
import com.wiz.alfacart.util.DpPxConverterUtil

@SuppressLint("ValidFragment")
class DF_Info(val msg:String, val listener:MCallback) : BaseDialogFragment() {

    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: DfInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // biar statusbarnya transparan, defaultnya hitam
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.let {
            it.setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT) // full width dialog

            // untuk atur warna, g bisa dari sini
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, DpPxConverterUtil.dpToPixel(16, context!!))
            it.setBackgroundDrawable(inset)

            setCancelable(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DfInfoBinding.bind(LayoutInflater.from(context).inflate(R.layout.df_info, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.tvInfo.setText(msg)

        bind.btnCloseApp.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity?.finishAndRemoveTask()
            } else {
                activity?.finishAffinity()
            }
        }

        bind.btnRetry.setOnClickListener {
            dismiss()
            listener.onRetry()
        }
    }

    interface MCallback {
        fun onRetry()

    }
}