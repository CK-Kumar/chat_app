package com.chandra.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signup: Button
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        name = findViewById(R.id.etName)
        signup = findViewById(R.id.btnSignUp)
        mAuth = FirebaseAuth.getInstance()

        signup.setOnClickListener {
         val email = email.text.toString()
         val password = password.text.toString()
            signUp(email,password)
        }
    }

    private fun signUp(email: String, password: String) {
       mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener (this){
               task->
           if (task.isSuccessful){
              intent = Intent(this@SignUp, MainActivity::class.java)
              startActivity(intent)

           }
           else
           {
             Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
           }

       }
    }
}