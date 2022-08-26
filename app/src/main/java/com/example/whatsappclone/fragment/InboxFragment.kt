package com.example.whatsappclone.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.*
import com.example.whatsappclone.Models.Inbox
import com.example.whatsappclone.Models.InboxViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import kotlinx.android.synthetic.main.fragments_chats.*

class InboxFragment : Fragment() {


    lateinit var mAdapter : FirebaseRecyclerAdapter<Inbox, InboxViewHolder>
    lateinit var viewManager: RecyclerView.LayoutManager

    private val mDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewManager = LinearLayoutManager(requireContext())
        setUpAdapter()
        return inflater.inflate(R.layout.fragments_chats , container , false)
    }

    private fun setUpAdapter() {

        val baseQuery : Query = mDatabase.reference.child("chats").child(auth.uid!!)

        val options = FirebaseRecyclerOptions.Builder<Inbox>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(baseQuery , Inbox::class.java)
            .build()


        mAdapter = object  : FirebaseRecyclerAdapter<Inbox , InboxViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InboxViewHolder {
                val itemView = layoutInflater.inflate(R.layout.list_item , parent , false)
                return InboxViewHolder(itemView)
            }

            override fun onBindViewHolder(holder: InboxViewHolder, position: Int, model: Inbox) {


                    holder.bind(item = model) { name: String, photo: String, id: String ->

                        val intent = Intent(requireContext(), ChatActivity::class.java)
                        intent.putExtra(ID, id)
                        intent.putExtra(NAME, name)
                        intent.putExtra(PHOTO, photo)

                        startActivity(intent)
                    }
                }


        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = viewManager
        }
    }

}
