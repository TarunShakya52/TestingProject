package com.example.demoapplication

import android.R.id.input
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(),TestCallbackInterface{

    lateinit var callbackTestClass:CallbackTestClass

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getIstTime()
        callbackTestClass = CallbackTestClass()
        callbackTestClass.start(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIstTime(){

        val millis = 1667370474000L
        val instant: Instant = Instant.ofEpochMilli(millis)
        val odt: OffsetDateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC)
        val lt: LocalTime = odt.toLocalTime()
        val time = lt.format(DateTimeFormatter.ISO_LOCAL_TIME)
        val output: String = time.substring(0, 5)
        Log.e("checknew",output)
    }

    override fun start() {
        Log.e("start","Start is called by callback")
    }

    override fun stop() {
        Log.e("start","stop is called by callback")
    }
}