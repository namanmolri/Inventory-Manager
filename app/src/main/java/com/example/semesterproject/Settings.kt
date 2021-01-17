package com.example.semesterproject
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {

    lateinit var database: DataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_signin_details)
        title = "Settings"
        database = DataBase(this)

        val userName = intent.extras?.getString("UserName")
        val password = intent.extras?.getString("Password")
        val id = intent.extras?.getString("ID")

        val currentUsername = findViewById<EditText>(R.id.current_username).text
        val currentPassword = findViewById<EditText>(R.id.current_password).text



        findViewById<Button>(R.id.check_button).setOnClickListener {

            val manager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)

            if ((userName.toString() == currentUsername.toString())  && (password.toString() == currentPassword.toString())) {
                findViewById<TextView>(R.id.textView2).visibility = View.VISIBLE
                findViewById<TextView>(R.id.new_username).visibility = View.VISIBLE
                findViewById<TextView>(R.id.new_password).visibility = View.VISIBLE
                findViewById<Button>(R.id.change_button).visibility = View.VISIBLE

                val newUsername = findViewById<EditText>(R.id.new_username).text
                val newPassword = findViewById<EditText>(R.id.new_password).text

                findViewById<Button>(R.id.change_button).setOnClickListener {
                    if(newUsername.toString() == "" && newPassword.toString() == "") {
                        Toast.makeText(this@Settings, "No changes were made.", Toast.LENGTH_LONG)
                            .show()
                        startActivity(Intent(this, MainLoginActivity::class.java))
                    }
                    else if (newUsername.toString() != "" && newPassword.toString() == "") {
                        for(i in database.getUser()) {
                            if(newUsername.toString() == i.username) {
                                return@setOnClickListener Toast.makeText(this@Settings, "Username already exists.", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        val userlist = Userlist()
                        userlist.username = newUsername.toString()
                        userlist.password = currentPassword.toString()
                        userlist.id = id!!.toLong()
                        database.updateUser(userlist)
                        startActivity(Intent(this, MainLoginActivity::class.java))
                        Toast.makeText(this@Settings, "Username was changed.", Toast.LENGTH_LONG)
                            .show()
                    }
                    else if (newUsername.toString() == "" && newPassword.toString() != "") {
                        val userlist = Userlist()
                        userlist.username = currentUsername.toString()
                        userlist.password = newPassword.toString()
                        userlist.id = id!!.toLong()
                        database.updateUser(userlist)
                        startActivity(Intent(this, MainLoginActivity::class.java))
                        Toast.makeText(this@Settings, "Password was changed.", Toast.LENGTH_LONG)
                            .show()
                    }

                    else if (newUsername.toString() != "" && newPassword.toString() != "") {
                        for(i in database.getUser()) {
                            if(newUsername.toString() == i.username) {
                                return@setOnClickListener Toast.makeText(this@Settings, "Username already exists.", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        val userlist = Userlist()
                        userlist.username = newUsername.toString()
                        userlist.password = newPassword.toString()
                        userlist.id = id!!.toLong()
                        database.updateUser(userlist)
                        startActivity(Intent(this, MainLoginActivity::class.java))
                        Toast.makeText(this@Settings, "Username and Password was changed.", Toast.LENGTH_LONG)
                            .show()
                    }
                }


            }
            else {
                Toast.makeText(this@Settings, "Username and Password do not match.", Toast.LENGTH_LONG)
                    .show()
            }
        }



    }
}