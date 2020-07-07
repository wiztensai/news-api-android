package com.wiz.alfacart.util

import android.content.Context
import android.util.DisplayMetrics

class DpPxConverterUtil {
    companion object {
        fun dpToPixel(dp: Int, context: Context): Int {
            val dpCalculation = context.getResources().getDisplayMetrics().density
            return (dpCalculation * dp).toInt()
        }
    }
}