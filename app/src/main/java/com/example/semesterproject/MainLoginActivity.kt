package com.example.semesterproject


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity


class MainLoginActivity : AppCompatActivity() {
    lateinit var database: DataBase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val userName = findViewById<EditText>(R.id.userName)
        userName.hint = "User Name"
        val password = findViewById<EditText>(R.id.password)
        password.hint = "Password"
        val login = findViewById<Button>(R.id.loginBtn)
        val toggleButton1 = findViewById<ToggleButton>(R.id.toggleButton)
        database = DataBase(this)

        login.setOnClickListener {

            if (toggleButton1.isChecked) {
                if (userName.text.toString() == "" && password.text.toString() == "") {
                    Toast.makeText(this@MainLoginActivity, "Empty username or password are not accepted.", Toast.LENGTH_LONG)
                        .show()
                }
                else if(userName.text.toString() == "Admin" && password.text.toString() == "manager1234") {
                    Log.i(TAG, "Ducked")
                    startActivity(Intent(this, OwnerLogged::class.java))
                }
                else {
                    Toast.makeText(this@MainLoginActivity, "Incorrect username or password.", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else {
                if (userName.text.toString() == "" && password.text.toString() == "") {
                    Toast.makeText(this@MainLoginActivity, "Empty username or password are not accepted.", Toast.LENGTH_LONG)
                        .show()
                }

                for(i in database.getUser()) {

                    if (i.username == userName.text.toString()) {
                        if(i.password == password.text.toString()) {
                            val intent = Intent(this, CustomerLogged::class.java)
                            intent.putExtra("UserName", i.username)
                            intent.putExtra("Password", i.password)
                            intent.putExtra("ID", i.id.toString())
                            return@setOnClickListener startActivity(intent)
                        }
                        else {
                            Toast.makeText(this@MainLoginActivity, "Incorrect username or password.", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }

                return@setOnClickListener Toast.makeText(this@MainLoginActivity, "Username does not exist.", Toast.LENGTH_LONG)
                        .show()




            }
        }


   }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.item1) {
            startActivity(Intent(this, SignUp::class.java))
        }

        return true
    }

    private val TAG = "MainActivity"
}

