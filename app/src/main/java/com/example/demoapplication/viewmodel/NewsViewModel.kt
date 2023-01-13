package com.example.demoapplication.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.demoapplication.repository.NewsRepository
import com.example.demoapplication.response.BaseResponse
import com.example.demoapplication.response.HeadLineResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class NewsViewModel(application: Application):AndroidViewModel(application) {

    val newsRepository:NewsRepository = NewsRepository()
    val headlineResponse:MutableLiveData<BaseResponse<HeadLineResponse>> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHeadlines(apikey:String, country:String){
        getIstTime()
        viewModelScope.launch {
            try {
                val response = newsRepository.getNews(apikey,country)
                if (response!!.code() == 200){
                    headlineResponse.value = BaseResponse.Success(response.body())
                }else{
                    headlineResponse.value = BaseResponse.Error(response.message())
                }
            }catch (ex:Exception){
                headlineResponse.value = BaseResponse.Error(ex.message)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIstTime(){

//        val millis = 1667370474000L
//        val str = "2022-10-17T06:43:42"
//        val instant: Instant = Instant.ofEpochMilli(millis)
//        val odt: OffsetDateTime = OffsetDateTime.ofInstant(str, ZoneOffset.UTC)
//        val lt: LocalTime = odt.toLocalTime()
//        val time = odt.format(DateTimeFormatter.ISO_DATE)
        val str = "2022-10-17T06:43:42"

        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val str2 = df.parse(str)
        df.timeZone = TimeZone.getDefault()
        val formattedDate = df.format(str2!!)

        Log.e("formattedDate",formattedDate)


        val output: String = formattedDate.substring(0, 10)
        var spf = SimpleDateFormat("yyyy-mm-dd")
        val newDate: Date = spf.parse(output)!!
        spf = SimpleDateFormat("dd MMM yyyy")
        var date = spf.format(newDate)
        Log.e("checknew",date)
    }

}