package com.example.demoapplication

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.Adapter.NewsAdapter
import com.example.demoapplication.databinding.ActivityTestRecyclerViewBinding
import com.example.demoapplication.response.BaseResponse
import com.example.demoapplication.viewmodel.NewsViewModel


class TestRecyclerView : AppCompatActivity() {

    private lateinit var binding:ActivityTestRecyclerViewBinding
    private val viewModel by viewModels<NewsViewModel>()
    private lateinit var adapter:NewsAdapter


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestRecyclerViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.getHeadlines("fc44b959432d4237bef121343962c432","in")
        viewModel.headlineResponse.observe(this){
            when(it){
                is BaseResponse.Error->{
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
                }
                is BaseResponse.Success->{
                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    adapter = NewsAdapter(it.data!!.articles)
                    binding.rvNews.adapter = adapter
                    binding.rvNews.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                }else->{
                    Toast.makeText(this,"not working",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}