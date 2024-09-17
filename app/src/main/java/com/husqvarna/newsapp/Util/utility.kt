package com.husqvarna.newsapp.Util

import android.content.Context
import android.content.Intent
import android.os.Bundle

object utility {

    fun splitSourceString(source: String): String {

        val source2 = source.split("=").toTypedArray()
        val source3 = source2.get(2).removeSuffix("}")
        return source3
    }

    fun launchActivity(
        context: Context,
        cls: Class<*>?, bundle: Bundle? = null, flag: Int?
    ) {
        val intent = Intent(context, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (flag != null) {
            intent.addFlags(flag)
        }
        context.startActivity(intent)
    }
}