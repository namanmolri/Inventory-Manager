package com.example.semesterproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class SignUp : AppCompatActivity() {

    lateinit var database: DataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val userName = findViewById<EditText>(R.id.signUpUsername)
        userName.hint = "User Name"
        val password = findViewById<EditText>(R.id.signUpPassword)
        password.hint = "Password"
        val login = findViewById<Button>(R.id.ownerLoginBtn)
        val email = findViewById<EditText>(R.id.email)
        email.hint = "Email Address"
        database = DataBase(this)

        fun validEmail(email: String?) : Boolean {
            if (email.isNullOrEmpty()) {
                return false
            }
            // General Email Regex (RFC 5322 Official Standard)
            val emailRegex = Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'" +
                    "*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x" +
                    "5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z" +
                    "0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4" +
                    "][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z" +
                    "0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|" +
                    "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
            return emailRegex.matches(email)
        }

        login.setOnClickListener {
            if (!validEmail(email.text.toString())){
                return@setOnClickListener Toast.makeText(this@SignUp, "Invalid Email Address", Toast.LENGTH_LONG).show()
            }
            for (i in database.getUser()) {
                if (userName.text.toString() == i.username || email.text.toString() == i.email) {
                    return@setOnClickListener Toast.makeText(this@SignUp, "This email or username already exists.", Toast.LENGTH_LONG)
                            .show()

                }
            }

            val userList = Userlist()
            userList.username = userName.text.toString()
            userList.password = password.text.toString()
            userList.email = email.text.toString()

            database.addUser(userList)

            for(i in database.getUser()) {
                var list = database.getUser()
                if (i.username == userName.text.toString()) {

                        val intent = Intent(this, CustomerLogged::class.java)
                        intent.putExtra("UserName", i.username)
                        intent.putExtra("Password", i.password)
                        intent.putExtra("ID", i.id.toString())
                        return@setOnClickListener startActivity(intent)
                }
            }

        }

    }


}