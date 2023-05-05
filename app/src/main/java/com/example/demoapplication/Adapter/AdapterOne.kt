package com.example.demoapplication.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demoapplication.IntentServiceResult
import com.example.demoapplication.R
//import org.greenrobot.eventbus.EventBus


class AdapterOne(private var context: Context) : RecyclerView.Adapter<AdapterOne.ViewHolder>()  {

    private var qunatity = 0

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var btnMinus: Button = itemView.findViewById(R.id.button2)
        var btnPlus: Button = itemView.findViewById(R.id.button3)
        var txtCount: TextView = itemView.findViewById(R.id.textView2)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rv1, parent, false)
        return ViewHolder(v)    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.btnPlus.setOnClickListener {
            qunatity++
            holder.txtCount.text = qunatity.toString()
            handleIntent(quantity = qunatity.toString())
        }
        holder.btnMinus.setOnClickListener {
            qunatity--
            holder.txtCount.text = qunatity.toString()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    fun handleIntent(quantity:String){
//        EventBus.getDefault().post(IntentServiceResult(quantity))
    }
}