package br.com.microblog.boticario.util

import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    @JvmStatic
    fun StampToDate(time: Long, locale: Locale): String {
        val simpleDateFormat = SimpleDateFormat("dd' de 'MMMM", locale)
        return simpleDateFormat.format(Date(time))
    }

    @JvmStatic
    fun DateToStamp(date: String, locale: Locale): Long {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)
        return simpleDateFormat.parse(date).time
    }
}