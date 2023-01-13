package com.example.demoapplication

import android.R.id.input
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.demoapplication.databinding.ActivityMainBinding
import com.example.demoapplication.response.BaseResponse
import com.example.demoapplication.viewmodel.MainViewModel
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity(),MainViewModel.Validate{

    private lateinit var binding:ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button2.setOnClickListener {

            viewModel.loginUser(
                binding.editTextPassword.text.toString(),
                binding.editTextTextEmailAddress.text.toString(),
                this
            )
        }

        viewModel.loginResult.observe(this){
            when (it){
                is BaseResponse.Error->{
                    Toast.makeText(this,it.msg,Toast.LENGTH_SHORT).show()
                }
                is BaseResponse.Success->{
                    Toast.makeText(this, it.data!!.message,Toast.LENGTH_SHORT).show()
                }
                else->{
                    Toast.makeText(this,"nai ho raha",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun invalidEmail() {
        Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show()
    }

    override fun invalidPassword() {
        TODO("Not yet implemented")
    }


}

