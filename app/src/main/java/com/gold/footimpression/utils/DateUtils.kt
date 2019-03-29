package com.gold.footimpression.net.utils

import java.text.SimpleDateFormat
import java.util.*


object DateUtils {

    /**
     * 把long 转换成 日期 再转换成String类型
     */
    fun transferLongToDate(dateFormat: String, millSec: Long?): String {
        val sdf = SimpleDateFormat(dateFormat)
        val date = Date(millSec!!)
        return sdf.format(date)
    }
}
