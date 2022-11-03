package com.example.demoapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.demoapplication.testing.CustomButton
import com.example.demoapplication.testing.TestingCallbackInterface

class MainActivity2 : AppCompatActivity(),TestingCallbackInterface{

    lateinit var button:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        button= findViewById(R.id.button)
        var cusButton = CustomButton()

        button.setOnClickListener {
            cusButton.onClickButton(this)
        }
    }

    override fun onClick() {
        Toast.makeText(this,"Testing of callback is successful",Toast.LENGTH_SHORT).show()
    }


}