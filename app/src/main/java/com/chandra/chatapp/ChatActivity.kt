package com.chandra.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdaptor: MessageAdapter
    var receiverRoom: String? = null
    var senderRoom: String? = null
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        messageRecyclerView = findViewById(R.id.rvChat)
        messageBox = findViewById(R.id.txtMessage)
        sendBtn = findViewById(R.id.btnSend)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid
        dbRef = FirebaseDatabase.getInstance().getReference()
        supportActionBar?.title = name
        messageList =ArrayList()
        messageAdaptor = MessageAdapter(this,messageList)


        sendBtn.setOnClickListener {
            val message = messageBox.text.toString()
            val messageObj = Message(message, senderUid)
            dbRef.child("chats").child(senderRoom!!).child("messages").push().
                    setValue(messageObj).addOnSuccessListener {
                dbRef.child("chats").child(receiverRoom!!).child("messages").push().
                setValue(messageObj)

            }
            messageBox.setText("")
        }

    }
}