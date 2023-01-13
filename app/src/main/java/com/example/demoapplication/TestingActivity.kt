package com.example.demoapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.Adapter.AdapterOne
import com.example.demoapplication.Adapter.AdapterTwo
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class TestingActivity : AppCompatActivity(){


    private lateinit var rv1: RecyclerView
    private lateinit var rv2: RecyclerView
    private var firstAdapter: RecyclerView.Adapter<AdapterOne.ViewHolder>? = null
    private var secondAdapter: RecyclerView.Adapter<AdapterTwo.ViewHolder>? = null
    private var count: String = "0"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing)

        rv1 = findViewById(R.id.recyclerView1)
        rv2 = findViewById(R.id.recyclerView2)

        firstAdapter = AdapterOne(this)

        rv1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv1.adapter = firstAdapter

        Toast.makeText(this,"working", Toast.LENGTH_SHORT).show()
        secondAdapter = AdapterTwo(this, count)
        rv2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        rv2.adapter = secondAdapter


    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        doThis(IntentServiceResult("0"))
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(intentServiceResult: IntentServiceResult) {
        Toast.makeText(this, intentServiceResult.quantity, Toast.LENGTH_SHORT).show()
    }

}