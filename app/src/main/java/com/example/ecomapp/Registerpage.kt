package com.example.ecomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class Registerpage : AppCompatActivity() {

    lateinit var signupemail: EditText
    lateinit var signuppassword: EditText
    private lateinit var signup: Button
    lateinit var pagelogin: TextView

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registerpage)


        signupemail=findViewById(R.id.signupemail)
        signuppassword=findViewById(R.id.signuppassword)
        signup=findViewById(R.id.signup)
        pagelogin=findViewById(R.id.pagelogin)

        auth = Firebase.auth

        pagelogin.setOnClickListener {
            var intent= Intent(this@Registerpage,Loginpage::class.java)
            startActivity(intent)
            finish()
        }

        signup.setOnClickListener {
            auth.createUserWithEmailAndPassword(signupemail.text.toString(), signuppassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(baseContext, "success.", Toast.LENGTH_SHORT).show()

                        val user = auth.currentUser

                        var intent= Intent(this@Registerpage,Loginpage::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(baseContext, "unsuccess.", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }
}
