package com.efhem.mystaff.utils

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore


fun getFilePath(contentURI: Uri, activity: Activity): String {
    val cursor = activity.contentResolver.query(contentURI, null, null, null, null)
    return if (cursor == null) {
        contentURI.path!!
    } else {
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        cursor.getString(idx)
    }
}

fun capitalin(fullWords: String): String{
    val str = "kotlin tutorial examples"

    val words = fullWords.split(" ").toMutableList()

    var output = ""

    for(word in words){
        output += word.capitalize() +" "
    }

    output = output.trim()
    return output
}