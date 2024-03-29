package com.chandra.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var login: Button
    private lateinit var signup: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        login = findViewById(R.id.btnLogin)
        signup = findViewById(R.id.btnSignUp)
        mAuth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
            intent = Intent(this,SignUp::class.java)
            startActivity(intent)
        }

        login.setOnClickListener {
            val email = email.text.toString()
            val password = password.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
       mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
           task->
           if (task.isSuccessful){
               intent = Intent(this@Login, MainActivity::class.java)
               finish()
               startActivity(intent)
           }
           else
           {
               Toast.makeText(this, "User no exist", Toast.LENGTH_SHORT).show()
           }
       }
    }
}