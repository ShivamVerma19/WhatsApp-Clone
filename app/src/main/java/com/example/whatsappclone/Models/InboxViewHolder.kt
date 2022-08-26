package com.example.whatsappclone.Models

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.example.whatsappclone.Utils.formatAsListItem
import com.example.whatsappclone.Utils.formatAsTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class InboxViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    fun bind(item : Inbox , onClick : (name : String , photo : String , id : String) -> Unit){

        with(itemView){

                if(item.count > 0 ){
                    itemCountMsg.isVisible = true
                    itemCountMsg.text = item.count.toString()
                }

                itemTime.text = item.time.formatAsListItem(context)

                itemName.text = item.name

                itemStatus.text = item.msg

             Log.e("TAG" , item.image)
                if(!item.image.isBlank()) {
                    Picasso.get()
                        .load(item.image)
                        .placeholder(R.drawable.defaultavatar)
                        .error(R.drawable.defaultavatar)
                        .into(itemIv)
                }

                setOnClickListener {
                    onClick.invoke(item.name , item.image , item.from)
                }
            }
        }
    }

