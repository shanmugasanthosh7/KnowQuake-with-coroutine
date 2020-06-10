package com.sample.knowquake.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    /**
     * Format EE, dd MMM yyyy HH:mm:ss z
     */
    const val DATE_TIME_FORMAT = "EE, dd MMM yyyy HH:mm:ss z"

    /**
     * Format yyyy/MM/dd hh:mm:ss a
     */
    const val DATE_TIME_FORMAT_2 = "yyyy/MM/dd hh:mm:ss a"

    /**
     * Format yyyy/MM/dd HH:mm:ss
     */
    const val DATE_TIME_FORMAT_3 = "yyyy/MM/dd HH:mm:ss"

    /**
     * Format yyyy/MM/dd
     */
    const val DATE_FORMAT_3 = "yyyy/MM/dd"

    /**
     * Format HH:mm:ss
     */
    const val TIME_FORMAT_3 = "HH:mm:ss"


    fun getFormat(format: String, locale: Locale): SimpleDateFormat {
        return SimpleDateFormat(format, locale)
    }

    fun getUnixTimestampToDate(unix: String, format: String, skipUnixConversion: Boolean): String {
        return try {
            //convert seconds to milliseconds
            val afterUnix: Long = if (skipUnixConversion) {
                unix.toLong()
            } else {
                parseUnixDate(unix)
            }
            val date = parseDate(afterUnix)
            // format of the date
            val jdf = getFormat(format, Locale.ENGLISH)
            jdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    private fun parseDate(unix: Long): Date {
        return Date(unix)
    }

    fun formatDate(date: Date, format: String): String {
        return getFormat(format, Locale.ENGLISH).format(date)
    }

    private fun parseUnixDate(unix: String): Long {
        return try {
            val timestamp = java.lang.Double.parseDouble(unix) * 1000
            timestamp.toLong()
        } catch (e: Exception) {
            0
        }
    }
}
