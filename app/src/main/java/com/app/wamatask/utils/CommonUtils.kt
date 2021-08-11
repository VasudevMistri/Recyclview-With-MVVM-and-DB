package com.app.wamatask.utils

import android.content.res.Resources
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CommonUtils {

    companion object {
        @JvmStatic
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels

        @JvmStatic
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels
        val screenDensity = Resources.getSystem().displayMetrics.density


        // getCurrentDateTime
        fun getCurrentDateTime(): String {
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mm aa")
            val date = Date()
            return formatter.format(date).toString()
        }

        // getCurrentDateTime
        fun getCurrentDateTime1(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = Date()
//            val date = formatter.parse("2021-05-26 10:10:00")
            return formatter.format(date).toString()
        }

        // getCurrentDateTime
        fun getCurrentDateObject(): Date? {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date1 = sdf.parse(sdf.format(Date()))
//            val date1 = sdf.parse("2021-05-26")
            return date1
        }

        // getCurrentDateTime
        fun getOrderDateObject(invDate: String?): Date? {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val d = sdf.parse(invDate)
            sdf.applyPattern("yyyy-MM-dd")
            val date1 = sdf.parse(sdf.format(d))
            return date1
        }

        // getCurrentDateTime
        fun getCurrentFilterDate(invDate: String?): Date? {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date1 = sdf.parse(invDate)
            return date1
        }

        // getCurrentDateTime
        fun getCurrentDateTime2(): String {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = Date()
            return formatter.format(date).toString()
        }

        // getCurrentDateTime
        fun getCurrentOnlyDate(): String {
            val formatter = SimpleDateFormat("dd")
            val date = Date()
            return formatter.format(date).toString()
        }

        fun getPreviousDate(inputDay: Int): String {
            var inputDate = ""
            val format = SimpleDateFormat("yyyy-MM-dd")
            try {
                val date = format.parse(format.format(Date()))
                val c = Calendar.getInstance()
                c.time = date
                c.add(Calendar.DATE, -inputDay)
                inputDate = format.format(c.time)
                Log.e("date", "selected date : $inputDate")
                System.out.println(date)
            } catch (e: java.lang.Exception) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                inputDate = ""
            }
            return inputDate
        }

        // Like This Convert Date : YYYY-MM-DD
        fun convertDateMMM(date: String?): String? {
            var spf = SimpleDateFormat("yyyy-MM-dd")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("dd-MMM-yyyy")
            return spf.format(newDate)
        }

        // Like This Convert Date : YYYY-MM-DD
        fun convertDateTime(date: String?): String? {
            var spf = SimpleDateFormat("dd-MMM-yyyy")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("yyyy-MM-dd")
            return spf.format(newDate)
        }

        // Like This Convert Date : dd-MMM-yyyy
        fun convertDate(date: String?): String? {
            var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX") //2020-12-29 11:24:55
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("dd MMM")
            return spf.format(newDate)
        }

        fun roundDoubleValue(value: Double, places: Int): Double {
            var value = value
            require(places >= 0)
            val factor = Math.pow(10.0, places.toDouble()).toLong()
            value = value * factor
            val tmp = Math.round(value)
            return tmp.toDouble() / factor
        }

        fun convertDateDDMMMYYYY(date: String?): String? {
            var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("dd MMM yyyy")
            return spf.format(newDate)
        }

        fun convertTime1(date: String?): String? {
            var spf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var newDate: Date? = null
            try {
                newDate = spf.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            spf = SimpleDateFormat("hh:mm a")
            return spf.format(newDate)
        }

        fun getRandomNumberString(): String? {
            // It will generate 6 digit random Number.
            // from 0 to 999999
            val rnd = Random()
            val number = rnd.nextInt(999999)
            // this will convert any number sequence into 6 character.
            return String.format("%06d", number)
        }

    }
}