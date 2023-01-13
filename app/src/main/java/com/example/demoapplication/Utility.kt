package com.example.demoapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class Utility {

    @SuppressLint("CommitPrefEdits")
    fun storeCount(count:String, activity: Activity)
    {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("count", Context.MODE_PRIVATE)
        var editor = sharedPreferences.edit()
        editor.putString("count", "count "+""+count)
        editor.apply()
    }
    fun getUserToken(activity: Activity):String
    {
        val token = activity.getSharedPreferences("count", Context.MODE_PRIVATE).getString("count","")
        return token!!

    }

}