package com.example.whatsappclone.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.Models.DateHeader
import com.example.whatsappclone.Models.Message
import com.example.whatsappclone.Models.chatEvent
import com.example.whatsappclone.R
import com.example.whatsappclone.Utils.formatAsTime
import com.example.whatsappclone.fragment.EmptyViewHolder.EmptyViewHolder
import kotlinx.android.synthetic.main.chat_list_item_recieve.view.*
import kotlinx.android.synthetic.main.chat_list_item_sent.view.*
import kotlinx.android.synthetic.main.chat_list_item_sent.view.content
import kotlinx.android.synthetic.main.chat_list_item_sent.view.time
import kotlinx.android.synthetic.main.list_item_date_header.view.*

class ChatAdapter(private val list : MutableList<chatEvent> , private val currentUid : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

          val inflater = {layout : Int ->
              LayoutInflater.from(parent.context).inflate(layout , parent , false)
          }
              return when(viewType){
                  TEXT_MESSAGE_SENT -> {
                      MessageViewHolder(inflater(R.layout.chat_list_item_sent))
                  }

                  TEXT_MESSAGE_RECIEVED ->{
                      MessageViewHolder(inflater(R.layout.chat_list_item_recieve))
                  }

                  DATE_HEADER ->{
                      DateViewHolder(inflater(R.layout.list_item_date_header))
                  }

                  else ->{
                      EmptyViewHolder(inflater(R.layout.empty_item))
                  }
              }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
             when(val item = list[position]){
                 is DateHeader ->{
                     holder.itemView.textView.text = item.date
                 }

                 is Message ->{
                     holder.itemView.apply {
                         if(getItemViewType(position) == TEXT_MESSAGE_SENT){
                             content.text = item.msg
                             time.text = item.sentAt.formatAsTime()
                         }
                         else{
                             content.text = item.msg
                             time.text = item.sentAt.formatAsTime()
                         }
                     }
                 }
             }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return when(val event = list[position]){
            is Message -> {
                if(event.senderId == currentUid){
                    TEXT_MESSAGE_SENT
                }
                else
                    TEXT_MESSAGE_RECIEVED
            }

            is DateHeader -> DATE_HEADER

            else -> UNSUPPORTED
        }
    }

    class DateViewHolder(view : View) : RecyclerView.ViewHolder(view)

    class MessageViewHolder(view : View) : RecyclerView.ViewHolder(view)

    companion object{
        private const  val UNSUPPORTED = -1
        private const  val TEXT_MESSAGE_RECIEVED = 0
        private const  val TEXT_MESSAGE_SENT  = 1
        private const  val DATE_HEADER = 2
    }
}