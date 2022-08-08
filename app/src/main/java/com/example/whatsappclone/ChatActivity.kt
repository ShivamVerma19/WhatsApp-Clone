package com.example.whatsappclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappclone.Adapters.ChatAdapter
import com.example.whatsappclone.Models.*
import com.example.whatsappclone.Utils.isSameDayAs
import com.google.api.Distribution
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import javax.annotation.CheckForNull

const val ID = "ID"
const val NAME = "NAME"
const val PHOTO = "PHOTO"
class ChatActivity : AppCompatActivity() {

    private val friendId by lazy{
        intent.getStringExtra(ID)
    }

    private  val name by lazy{
        intent.getStringExtra(NAME)
    }

    private  val photo  by lazy{
        intent.getStringExtra(PHOTO)
    }

    private val currentUid  by lazy{
        FirebaseAuth.getInstance().uid!!
    }


    private val realTimeDatabase by lazy{
        FirebaseDatabase.getInstance()
    }

    lateinit var currentUser : User

    lateinit var chatAdapter: ChatAdapter
    val chatList = mutableListOf<chatEvent>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        setContentView(R.layout.activity_chat)

        FirebaseFirestore.getInstance().collection(KEY).document(currentUid).get()
            .addOnSuccessListener {
                currentUser = it.toObject(User::class.java)!!
            }
            .addOnFailureListener {
                Toast.makeText(this , "Could not load current user details" , Toast.LENGTH_SHORT).show()
            }

        chatUserName.text = name
        Picasso.get().load(photo).into(chatProfileiv)



        chatAdapter = ChatAdapter(chatList , currentUid)

        msgRv.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }

        listenToMessage()
        sendBtn.setOnClickListener {
            msgEdtv.text?.let{
                if(it.isNotEmpty()){
                    sendMessages(it.toString())
                    it.clear()
                }
            }
        }
    }


    private fun getInbox(toUser : String , fromUser : String) = realTimeDatabase.getReference()
        .child("chats/$toUser/$fromUser")



    private fun getMessages(friendId: String) = realTimeDatabase.getReference()
        .child("messages/${getMessageId(friendId)}")



    //function to get the messageId
    private fun getMessageId(friendId : String) : String{

        if(currentUid > friendId)
            return currentUid + friendId
        else
            return friendId + currentUid

    }



    private fun sendMessages(msg : String) {
        val msgId = friendId?.let { getMessages(it).push().key }

        checkNotNull(msgId) { "Cannot be null" }

        val msgClass = Message(msg, msgId, currentUid)

        friendId?.let {
            getMessages(it).child(msgId).setValue(msgClass)
                .addOnSuccessListener {

            }
                .addOnFailureListener {
                    Log.i("MessageError" , it.localizedMessage)
                }
        }

        updateLastMessage(msgClass)
    }

    private fun updateLastMessage(msgClass: Message) {

        val inboxClass = name?.let {
            photo?.let { it1 ->
                Inbox(
                    msgClass.msg ,
                    currentUid ,
                    it,
                    it1,
                    0
                )
            }
        }




        friendId?.let {
            getInbox(currentUid , it).setValue(inboxClass).addOnSuccessListener {
                     getInbox(friendId!!, currentUid).addListenerForSingleValueEvent(object : ValueEventListener{

                         override fun onDataChange(snapshot: DataSnapshot) {
                              val value = snapshot.getValue(Inbox::class.java)

                             //all messages are read

                              inboxClass.apply{
                                  this!!.from = msgClass.senderId
                                  name = currentUser.name
                                  image = currentUser.thumbImage
                                  count = 1

                                  //messages unread
                                  value?.let{
                                      if(value.count != 0){
                                          count = value.count + 1
                                      }
                                  }
                              }

                             getInbox(friendId!!, currentUid).setValue(inboxClass)
                         }

                         override fun onCancelled(error: DatabaseError) {
                             TODO("Not yet implemented")
                         }

                     })
               }
        }
    }



    private fun markAsRead(){
        friendId?.let {
            getInbox(currentUid , it).child("count").setValue(0)
        }
    }


    private fun listenToMessage(){
        getMessages(friendId!!)
            .orderByKey()
            .addChildEventListener(object : ChildEventListener{

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val message = snapshot.getValue(Message::class.java)!!
                    addMessages(message)
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun addMessages(message: Message) {

        val eventBefore = chatList.lastOrNull()

        if((eventBefore != null && !eventBefore.sentAt.isSameDayAs(message.sentAt)) || eventBefore == null){
            chatList.add(
                DateHeader(message.sentAt , context = this)
            )
        }

        chatList.add(message)

        chatAdapter.notifyItemInserted(chatList.size - 1)
        msgRv.scrollToPosition(chatList.size - 1)
    }
}