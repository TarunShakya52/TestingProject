package com.example.demoapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.R
import com.example.demoapplication.response.HeadLineResponse

class NewsAdapter(articles: List<HeadLineResponse.Article>) :RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var articles:List<HeadLineResponse.Article> = articles

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val txtHeadline = itemView.findViewById<TextView>(R.id.textView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvheadlines,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = articles[position]
        holder.txtHeadline.text = result.title
    }

    override fun getItemCount(): Int {
        return articles.size
    }
}