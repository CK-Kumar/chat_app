package com.chandra.chatapp

import android.content.Context
import android.provider.Telephony.Sms.Sent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECIEVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view:View = LayoutInflater.from(context).inflate(R.layout.recieved,parent,false)
            return ReceivedViewHolder(view)
        }
        else {
            val view:View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            return SentViewHolder(view)
        }

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.javaClass == SentViewHolder::class.java)
        {
            val viewHolder = holder as SentViewHolder
            val current = messageList[position]
            viewHolder.sentMessage.text = current.message
        }
        else
        {
            val viewHolder = holder as ReceivedViewHolder
            val current = messageList[position]
            viewHolder.receivedMessage.text = current.message
        }
        }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return  ITEM_SENT
        }
        else {
            return  ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }




    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sentMessage = itemView.findViewById<TextView>(R.id.sentMessage)
    }

    class ReceivedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receivedMessage = itemView.findViewById<TextView>(R.id.rcMessage)
    }
}