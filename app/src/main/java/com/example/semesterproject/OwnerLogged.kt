package com.example.semesterproject

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class OwnerLogged : AppCompatActivity() {
    lateinit var database: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_logged)

        title = "Owner Page"
        database = DataBase(this)

        findViewById<RecyclerView>(R.id.recycle).layoutManager = LinearLayoutManager(this)


        val floatButton = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        floatButton.setOnClickListener {


            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_list, null)
            val mainListName = view.findViewById<EditText>(R.id.customer_list)


            dialog.setTitle("Add to list")
            dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                if (mainListName.text.isNotEmpty()) {
                    val itemlist = Itemlist()
                    if(view.findViewById<EditText>(R.id.price_value_cust).text.toString() != ""
                        && view.findViewById<EditText>(R.id.catergory_name_cust).text.toString() != ""
                        && view.findViewById<EditText>(R.id.quantity_value_cust).text.toString() != "") {

                        itemlist.itemName = mainListName.text.toString().toUpperCase()
                        itemlist.price = view.findViewById<EditText>(R.id.price_value_cust).text.toString().toLong()
                        itemlist.cate = view.findViewById<EditText>(R.id.catergory_name_cust).text.toString().toUpperCase()
                        itemlist.qty = view.findViewById<EditText>(R.id.quantity_value_cust).text.toString().toLong()
                        database.addListItem(itemlist)
                        findViewById<RecyclerView>(R.id.recycle).adapter = DashboardAdapter(this, database.getListItem())
                    } else {
                        Toast.makeText(this@OwnerLogged, "Cannot leave any fields empty.", Toast.LENGTH_LONG)
                            .show()
                    }

                } else {
                    Toast.makeText(this@OwnerLogged, "Cannot leave name empty.", Toast.LENGTH_LONG)
                        .show()
                }
            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->

            }
            dialog.show()
        }
    }

    @SuppressLint("WrongViewCast")
    fun updateItem(itemlist: Itemlist) {
        val dialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_list, null)
        val mainListName = view.findViewById<EditText>(R.id.customer_list)

        mainListName.setText(itemlist.itemName)
        view.findViewById<EditText>(R.id.price_value_cust).setText("$" + itemlist.price.toString())
        view.findViewById<EditText>(R.id.catergory_name_cust).setText(itemlist.cate)
        view.findViewById<EditText>(R.id.quantity_value_cust).setText(itemlist.qty.toString())

        dialog.setTitle("Update List")
        dialog.setView(view)

        dialog.setPositiveButton("Update") { _: DialogInterface, _: Int ->
            if (mainListName.text.isNotEmpty()) {
                if(view.findViewById<EditText>(R.id.price_value_cust).text.toString() != ""
                    && view.findViewById<EditText>(R.id.catergory_name_cust).text.toString() != ""
                    && view.findViewById<EditText>(R.id.quantity_value_cust).text.toString() != "") {

                    itemlist.itemName = mainListName.text.toString().toUpperCase()
                    itemlist.price = view.findViewById<EditText>(R.id.price_value_cust).text.removePrefix("$").toString().toLong()
                    itemlist.cate = view.findViewById<EditText>(R.id.catergory_name_cust).text.toString().toUpperCase()
                    itemlist.qty = view.findViewById<EditText>(R.id.quantity_value_cust).text.toString().toLong()
                    database.updateListItem(itemlist)
                    Log.i(TAG, database.getListItem()[0].price.toString())
                    findViewById<RecyclerView>(R.id.recycle).adapter = DashboardAdapter(this, database.getListItem())
                }
                else {
                    Toast.makeText(this@OwnerLogged, "Cannot leave any fields empty.", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else {
                Toast.makeText(this@OwnerLogged, "Cannot leave name empty.", Toast.LENGTH_LONG)
                    .show()
            }
        }
        dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
        }
        dialog.show()
    }

    fun deleteItem (id: Long) {
        database.deleteListItem(id)
        findViewById<RecyclerView>(R.id.recycle).adapter = DashboardAdapter(this, database.getListItem())
    }


    override fun onResume() {
        findViewById<RecyclerView>(R.id.recycle).adapter = DashboardAdapter(this, database.getListItem())
        super.onResume()
    }

    class DashboardAdapter(val activity: OwnerLogged, val list: MutableList<Itemlist>) :
            RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_list_other, p0, false))
        }
        override fun getItemCount(): Int {
            return list.size
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mainListName.text = list[position].itemName

            holder.mainListName.setOnClickListener {
                Log.i("Hello WOlrd", "Click happened")
            }

            holder.menu.setOnClickListener{
                val popup = PopupMenu(activity,holder.menu)
                popup.inflate(R.menu.logged_in_menu)

                popup.setOnMenuItemClickListener {

                    when(it.itemId){
                        R.id.item1->{
                            activity.updateItem(list[position])
                            holder.mainListName.text = list[position].itemName
                        }
                        R.id.item2->{
                            activity.deleteItem(list[position].id)

                        }
                    }

                    true
                }
                popup.show()
            }
        }
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val mainListName: TextView = v.findViewById(R.id.tv_mainlist_name)
            val menu : ImageView = v.findViewById((R.id.iv_menu))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.owner_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.item1) {
            startActivity(Intent(this, MainLoginActivity::class.java))
        }

        return true
    }
    private val TAG = "OwnerLogged"
}