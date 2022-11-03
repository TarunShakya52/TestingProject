package com.example.demoapplication

import android.content.Context

class CallbackTestClass() {

    lateinit var mListner:OnClick

    fun start(testCallbackInterface: TestCallbackInterface){
       testCallbackInterface.start()
        testCallbackInterface.stop()
    }

     fun setNewClick(onCustomeClick:OnClick){
        mListner = onCustomeClick

    }

    interface OnClick{
        fun onNewClick()
    }
}