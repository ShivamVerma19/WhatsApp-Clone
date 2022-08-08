package com.example.whatsappclone.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.*
import com.example.whatsappclone.Models.User
import com.example.whatsappclone.Models.UserViewHolder
import com.example.whatsappclone.fragment.EmptyViewHolder.EmptyViewHolder
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragments_chats.*


private const val DELETED_VIEW = 1
private const val NORMAL_VIEW = 2
class PeopleFragment : Fragment() {


    lateinit var mAdapter : FirestorePagingAdapter<User, RecyclerView.ViewHolder>

    val auth by lazy{
        FirebaseAuth.getInstance()
    }


    //query
    val database by lazy{
        FirebaseFirestore.getInstance().collection(KEY)
            .orderBy("name", Query.Direction.ASCENDING)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setUpAdapter()

        //yaha pr error aaya tha yaad rkhna
        return inflater.inflate(R.layout.fragments_chats , container , false)
    }

    private fun setUpAdapter() {

        val config = PagingConfig(10,2,false)


        val options = FirestorePagingOptions.Builder<User>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(database , config , User::class.java)
            .build()


        mAdapter = object : FirestorePagingAdapter<User, RecyclerView.ViewHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val view = layoutInflater
                    .inflate(R.layout.list_item, parent, false)


                val emptyView = layoutInflater
                    .inflate(R.layout.empty_item, parent, false)

                return when(viewType){
                    NORMAL_VIEW -> UserViewHolder(view)
                    else -> EmptyViewHolder(emptyView)
                }
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: User) {

                if(holder is UserViewHolder){
                    holder.bind(user = model){ name : String , photo : String , id : String ->
                          val intent = Intent(requireContext() , ChatActivity::class.java)
                          intent.putExtra(ID ,id )
                          intent.putExtra(NAME ,name )
                          intent.putExtra(PHOTO , photo)
                          startActivity(intent)
                    }
                }
                else{
                    //Todo - Left Empty
                }
            }

            override fun getItemViewType(position: Int): Int {

                val item = getItem(position)?.toObject(User::class.java)

                if(item!!.uid == auth.uid){
                    return DELETED_VIEW
                }
                else{
                    return NORMAL_VIEW
                }
            }


        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }
}