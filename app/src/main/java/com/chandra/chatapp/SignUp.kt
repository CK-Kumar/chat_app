package com.chandra.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mStore: FirebaseFirestore
    private lateinit var mdb: DatabaseReference

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        name = findViewById(R.id.etName)
        signup = findViewById(R.id.btnSignUp)
        mAuth = FirebaseAuth.getInstance()
        mStore = FirebaseFirestore.getInstance()


        signup.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            val userName = name.text.toString()
            signUp(email, password, userName)

        }
    }


    private fun signUp(email: String, password: String, userName: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {

                addUserToDatabase(email, userName, mAuth.currentUser?.uid!!)
                val data = hashMapOf(
                    "userName" to userName, "email" to email, "uid" to mAuth.currentUser?.uid!!
                )
                mStore.collection("user").add(data)
                intent = Intent(this@SignUp, MainActivity::class.java)
                finish()
                startActivity(intent)

            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addUserToDatabase(email: String, userName: String, uid: String) {
        mdb = FirebaseDatabase.getInstance().reference
        mdb.child("user").child(uid).setValue(User(userName, email, uid))
    }
}