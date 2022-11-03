package com.example.demoapplication.testing

import com.example.demoapplication.CallbackTestClass
import com.example.demoapplication.TestCallbackInterface

class CustomButton {
    fun onClickButton(tesingCallback:TestingCallbackInterface){
        tesingCallback.onClick()
    }
}