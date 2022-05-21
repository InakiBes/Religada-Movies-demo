package com.religada.moviesdemo.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.BuildConfig
import com.religada.moviesdemo.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//TODO BORRAR LO NO USADO
fun setImage(context: Context, content: Any, into: ImageView) {
    Glide.with(context)
        .load(normalizeUrlIfRequired(content))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.not_image)
        .into(into)
}

private fun normalizeUrlIfRequired(content: Any): Any =
    when (content) {
        is String -> if (content.startsWith("http")) content.replace("\\", "/") else "http://$content".replace("\\", "/")
        else -> content
    }

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(pass: String): Boolean{
    return (pass.length >5)
}

fun isValidNumber(str: String):Boolean{
    return str.toIntOrNull() != null
    //return if (s.isEmpty()) false else s.all { Character.isDigit(it) }
}

//Devuelve la fecha y hora actual en formato "2021-10-11T07:45:16+00:00"
fun getCurrentDateAndTime():String{
    (return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+02:00'")
        currentDateTime.format(formatter)
    } else {
        "Require Api 21 Android 5.0"
    }).toString()
}

fun getCurrentDate():Long{
    return Date().time
}

fun getCurrentDateString():String {
    return dateLongToTextDate(getCurrentDate(), "dd-MM-yyyy")
}

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentTimeString():String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val currentDateTime = LocalDateTime.now()
    return currentDateTime.format(formatter)
}

//fun convertStringDateToLongTime(dateString: String): Long {
//    //val datePattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+02:00'")
//    //return LocalDate.parse(dateString, datePattern)
//    val localDate = LocalDate.parse(dateString)
//
//    return localDate.atStartOfDay().toEpochSecond(Zoneoffset.UTC)*1000
//}


fun dateTextToDateLong(stringDate:String, formatDate:String): Long {
    var milliseconds : Long= 0
    val format = SimpleDateFormat(formatDate) //"yyyy-MM-dd'T'HH:mm:ss"
    try {
        val date = format.parse(stringDate)
        milliseconds = date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return milliseconds
}

fun dateLongToTextDate(long:Long, format: String):String{
    var dateString = ""
    val dateFormat = SimpleDateFormat(format) //"yyyy-MM-dd'T'HH:mm:ss"
    try {
        dateString = dateFormat.format(Date(long))
    } catch (ex: ParseException) {
        log( ex.localizedMessage)
    }
    log("FECHA $long => $dateString")
    return dateString
}

fun cleanText(str:String):String{

    val pattern1 = "\\[.*?\\]".toRegex()
    val pattern2 = "\\<.*?\\>".toRegex()
    val pattern3 = "&lt;div.*?&lt;div".toRegex()

    return str.replace(pattern1, "").replace(pattern2, "").replace(pattern3, "")
}

fun cleanHtml(str : String): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(str, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(str).toString()
    }
}

fun getLocaleCodeForApi(prefManager: PrefManager) = prefManager.getString(prefManager.LANGUAGE).ifEmpty {
        if (Locale.getDefault().toString().subSequence(0, 2) == "en") "en-us" else "es-es"
    }

fun log(msg: String){
    Log.d("_dev", msg);
}
