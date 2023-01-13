package com.example.demoapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.R

class AdapterTwo(private var context: Context, count: String) : RecyclerView.Adapter<AdapterTwo.ViewHolder>() {


    var qunatity = count.toInt()

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var btnMinus: Button = itemView.findViewById(R.id.button_minus)
        var btnPlus: Button = itemView.findViewById(R.id.button_plus)
        var txtCount: TextView = itemView.findViewById(R.id.textView_second)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtCount.text = qunatity.toString()
        holder.btnPlus.setOnClickListener {
            qunatity++
            holder.txtCount.text = qunatity.toString()

        }
        holder.btnMinus.setOnClickListener {
            qunatity--
            holder.txtCount.text = qunatity.toString()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}