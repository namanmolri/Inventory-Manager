package com.example.semesterproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class CustomerLogged : AppCompatActivity() {

    lateinit var database: DataBase
    lateinit var userName: String
    lateinit var toggleOnList: MutableList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_logged)
        val color = Integer.parseInt("bdbdbd", 16)+0xFF000000
        title = "Customer Page"
        database = DataBase(this)
        intent.extras!!.getString("UserName").toString().let { userName = it }
        toggleOnList = ArrayList()

        findViewById<RecyclerView>(R.id.recycle1).layoutManager = LinearLayoutManager(this)

        findViewById<RecyclerView>(R.id.recycle1).adapter = DashboardAdapter1(this, database.getListItem())



        val list = database.getListItem()
        if(database.checkUserInNoteList(userName)) {
            val notifList = database.getNoteFromOneUser(userName)
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.notify_list, null)
            val linearLayout = view.findViewById<LinearLayout>(R.id.linear_layout)

            for(i in list) {
                if(notifList.contains(i.itemName)) {
                    if(i.qty != 0.toLong()) {
                        val tv_dynamic = TextView(this)
                        tv_dynamic.textSize = 30f
                        tv_dynamic.setTextColor(ContextCompat.getColor(this, R.color.purple_700))
                        tv_dynamic.text = i.itemName
                        linearLayout.addView(tv_dynamic)
                        database.deleteNote(userName, i.itemName)

                    } else {
                        toggleOnList.add(i.itemName)
                    }
                }
            }
            if(!linearLayout.isEmpty()) {
                dialog.setView(view)
                dialog.show()
            }


        }



    }

    @SuppressLint("SetTextI18n")
    fun dialogMaker (itemlist: Itemlist) {
        Log.i("They", this.toString())
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.customer_dialog, null)



        dialog.setTitle("Item Data")
        dialog.setView(view)

        view.findViewById<TextView>(R.id.price_value_cust).text = "$" +itemlist.price.toString()
        view.findViewById<TextView>(R.id.quantity_value_cust).text = itemlist.qty.toString()
        view.findViewById<TextView>(R.id.catergory_name_cust).text = itemlist.cate
        view.findViewById<TextView>(R.id.item_name_cust).text = itemlist.itemName


        dialog.show()
    }

    override fun onResume() {
        findViewById<RecyclerView>(R.id.recycle1).layoutManager = LinearLayoutManager(this)
        super.onResume()
    }
    class DashboardAdapter1(val activity: CustomerLogged, val list: MutableList<Itemlist>) :
            RecyclerView.Adapter<DashboardAdapter1.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_list, p0, false))
        }
        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mainListName.text = list[position].itemName
            holder.mainListName.setOnClickListener{
                Log.i("This", "Yay")
                activity.dialogMaker(list[position])
            }
            if(activity.toggleOnList.contains(list[position].itemName)) {
                holder.toggleButton2.isChecked = true
            }
            if(list[position].qty == 0.toLong()) {
                holder.toggleButton2.isEnabled = true
            }

            holder.toggleButton2.setOnClickListener {
                if (holder.toggleButton2.isChecked) {
                    Toast.makeText(activity, "Notification has been turned on for: "+list[position].itemName, Toast.LENGTH_LONG)
                        .show()
                    if (!activity.database.getNoteFromOneUser(activity.userName).contains(list[position].itemName)) {
                        activity.database.addNote(activity.userName, list[position].itemName)

                    }

                 } else {
                    Toast.makeText(activity, "Notification has been turned off for: "+list[position].itemName, Toast.LENGTH_LONG)
                        .show()
                    activity.database.deleteNote(activity.userName, list[position].itemName)
                }


            }


        }
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val mainListName: TextView = v.findViewById(R.id.tv_mainlist_name)
            val toggleButton2 = v.findViewById<ToggleButton>(R.id.toggleButton2)!!

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.logout, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.item1) {
            startActivity(Intent(this, MainLoginActivity::class.java))
        }
        if(id == R.id.item2) {
           val intent = Intent(this, Settings::class.java)

            intent.putExtra("UserName", getIntent().extras?.getString("UserName"))
            intent.putExtra("Password", getIntent().extras?.getString("Password"))
            Log.i("hey", getIntent().extras?.getString("ID").toString())
            intent.putExtra("ID", getIntent().extras?.getString("ID").toString())
            startActivity(intent)

        }

        return true
    }
}