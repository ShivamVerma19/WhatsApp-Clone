package com.example.whatsappclone.Models

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    fun bind(user: User, onClick: (name : String , photo : String , id : String) -> Unit) {
        with(itemView){
            itemCountMsg.isVisible = false
            itemTime.isVisible = false

            itemName.text = user.name
            itemStatus.text = user.status
            Picasso.get()
                .load(user.thumbImage)
                .placeholder(R.drawable.defaultavatar)
                .error(R.drawable.defaultavatar)
                .into(itemIv)


            setOnClickListener {
                onClick.invoke(user.name , user.thumbImage , user.uid)
            }
        }
    }
}