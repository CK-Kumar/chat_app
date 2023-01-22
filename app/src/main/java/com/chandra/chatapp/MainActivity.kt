package com.chandra.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var emailText: EditText
    private lateinit var search: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore
    private lateinit var rvUser: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var toolbar: Toolbar
    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emailText = findViewById(R.id.etSearchEmail)
        search = findViewById(R.id.btnSearch)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        dbRef = FirebaseDatabase.getInstance().reference
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()
        userList = ArrayList()
        userAdapter = UserAdapter(this, userList)
        rvUser = findViewById(R.id.rvUser)
        rvUser.layoutManager = LinearLayoutManager(this)
        rvUser.adapter = userAdapter

        dbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(s in snapshot.children)
                {
                    val currentUser = s.getValue(User::class.java)
                    userList.add(currentUser!!)
                }
                userAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })


        search.setOnClickListener {
            val email = emailText.text.toString()
            if (email != "") {
                mStore.collection("user").whereEqualTo("email", email)
                    .get().addOnSuccessListener(this) { documents ->
                        // documents contains the query results
                        if (documents.size() == 0) {
                            Toast.makeText(this, "Not exist", Toast.LENGTH_SHORT).show()
                        }
                        for (document in documents) {

                            val name = document.data["userName"].toString()
                            Toast.makeText(this, "$name exist", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Not exist", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            mAuth.signOut()
            val intent = Intent(this@MainActivity, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}