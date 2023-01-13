package com.example.demoapplication

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import com.google.android.material.datepicker.*
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class CustomDatePicker : AppCompatActivity() {

    private lateinit var btn_DateRangePicker:Button
    private lateinit var dateRange:MaterialDatePicker<Pair<Long, Long>>
    lateinit var materialDatePicker: MaterialDatePicker<*>


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_date_picker)

//        getIstfromUtc()
//        gettime()
        getIstTime()
//        val calendar = Calendar.getInstance()
//        val dateValidatorMin: CalendarConstraints.DateValidator = DateValidatorPointForward.from(calendar.timeInMillis - 32.days.toLong(
//            DurationUnit.MILLISECONDS))
//        val dateValidatorMax: CalendarConstraints.DateValidator = DateValidatorPointBackward.before(calendar.timeInMillis + 0.days.toLong(
//            DurationUnit.MILLISECONDS))
//        val constraintsBuilderRange = CalendarConstraints.Builder()
//        materialDatePicker = dateRangePicker().build()
//        val listValidators = ArrayList<CalendarConstraints.DateValidator>()
//        listValidators.add(dateValidatorMin)
//        listValidators.add(dateValidatorMax)
//        val validators = CompositeDateValidator.allOf(listValidators)
//        constraintsBuilderRange.setValidator(validators)
//
//
//        btn_DateRangePicker = findViewById(R.id.btn_DateRangePicker)
//        dateRange = dateRangePicker()
//            .setTitleText("Select a date")
//            .setCalendarConstraints(constraintsBuilderRange.build())
//            .setTheme(R.style.CustomThemeOverlay_MaterialCalendar)
//            .setSelection(Pair(MaterialDatePicker.todayInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds()))
//            .build()
//        btn_DateRangePicker.setOnClickListener {
//            dateRange.show(supportFragmentManager, "DATE_RANGE_PICKER")
//        }
//
//        dateRange.addOnPositiveButtonClickListener {
//            it.second
//            Log.e("code",it.first.toString())
//            val from = getDateFromUTCTimestamp(it.first,"yyyy-MM-dd")+" "+"00:00:00"
//            val to = getDateFromUTCTimestamp(it.second,"yyyy-MM-dd")+" "+"23:59:59"
//            Log.e("result", from +" "+ to)
//        }
//        materialDatePicker.addOnPositiveButtonClickListener {
//            Toast.makeText(this,materialDatePicker.getHeaderText(),Toast.LENGTH_SHORT).show()
//        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun getIstfromUtc(){
        val str = "2022-11-15T06:00:43"
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val str2 = df.parse(str)
        df.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val formattedDate = df.format(str2!!)
        Log.e("formattedDate",formattedDate)


        val output: String = formattedDate.substring(11, 19)
        Log.e("output",output)

        val result: String = LocalTime.parse(output, DateTimeFormatter.ofPattern("HH:mm:ss"))
            .format(DateTimeFormatter.ofPattern("hh:mm a"))
        Log.e("result",result)

        var spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val date: Date = spf.parse(formattedDate)!!
        spf = SimpleDateFormat("dd MMM HH:mm")
        val newDate = spf.format(date)
        Log.e("checknew",newDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    fun gettime(){
        val str = "2023-01-03T06:07:14.736719736Z"
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val str2 = df.parse(str)
        df.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val formattedDate = df.format(str2!!)
        Log.e("formattedDate",formattedDate)

        val output: String = formattedDate.substring(11, 19)
        val datenew:String = formattedDate.substring(0,10)



        val dt = SimpleDateFormat("yyyy-MM-dd")
        val date = dt.parse(datenew)

        val dt1 = SimpleDateFormat("MMM-dd")
        Log.e("new date",dt1.format(date as Date))

        val result: String = LocalTime.parse(output, DateTimeFormatter.ofPattern("hh:mm:ss"))
            .format(DateTimeFormatter.ofPattern("hh:mm a"))

        val newResult = dt1.format(date)+" "+result
        Log.e("result",newResult)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateFromUTCTimestamp(mTimestamp: Long, mDateFormate: String?): String? {
        var date: String? = null
        try {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.timeInMillis = mTimestamp
            date = DateFormat.format(mDateFormate, cal.timeInMillis).toString()
            val formatter = SimpleDateFormat(mDateFormate)
            formatter.timeZone = TimeZone.getTimeZone("UTC")
            val value = formatter.parse(date)
            val dateFormatter = SimpleDateFormat(mDateFormate)
            dateFormatter.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
            date = dateFormatter.format(value)
            return date
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date
    }

    fun convert12(str: String) {

        val h1 = str[0].code - '0'.code
        val h2 = str[1].code - '0'.code
        var hh = h1 * 10 + h2

        val Meridien: String
        Meridien = if (hh < 12) {
            "AM"
        } else "PM"
        hh %= 12
        // Handle 00 and 12 case separately
        if (hh == 0) {
            print("12")
            // Printing minutes and seconds
            for (i in 2..7) {
                print(str[i])
            }
        } else {
            print(hh)
            // Printing minutes and seconds
            for (i in 2..7) {
                print(str[i])
            }
        }
        Log.e("time"," "+Meridien)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIstTime(){

        val millis = 1672729600000
        val sdf = SimpleDateFormat("HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val formattedDate = sdf.format(millis)
        Log.e("resulttime",formattedDate)
    }

}