package com.example.semesterproject


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// A few online services and github posts were referenced to build this database in the best possible way.

class DataBase(context: Context): SQLiteOpenHelper(context, "436_SEMESTER", null, 1) {

    override fun onCreate(db:SQLiteDatabase){
        val ID = "_id"
        val createUserTable = "CREATE TABLE User_LIST (" +
                "$ID integer PRIMARY KEY AUTOINCREMENT," +
                "Username varchar, " +
                "Password varchar, " +
                "Email varchar);"

        val createCateList = "CREATE TABLE Item_LIST (" +
                "$ID integer PRIMARY KEY AUTOINCREMENT," +
                "Category varchar, " +
                "ItemName varchar, " +
                "Quantity integer, " +
                "Price BIGINT);"

        val createNoteList = "CREATE TABLE Note_LIST (" +
                "$ID integer PRIMARY KEY AUTOINCREMENT," +
                "User varchar, " +
                "Item varchar);"

        db.execSQL(createUserTable)
        db.execSQL(createCateList)
        db.execSQL(createNoteList)
    }


    fun addNote(User : String, Item : String): Boolean {
        val  db :SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put("User", User)
        cv.put("Item", Item)
        val result = db.insert("Note_LIST", null, cv)
        return result != (-1).toLong()
    }

    fun deleteNote(user : String, item : String)
    {
        val  db :SQLiteDatabase = writableDatabase
        db.delete("Note_LIST", "User=? and Item=?", arrayOf(user, item))
    }

    //SELECT N.Item FROM Note_LIST AS N WHERE N.User = User
    fun getNoteFromOneUser(user : String): MutableList<String> {
        val result : MutableList<String> = ArrayList()
        val db:SQLiteDatabase = readableDatabase
        val queryResult : Cursor = db.rawQuery("SELECT * FROM Note_LIST", null)
        if (queryResult.moveToFirst())
        {
            do {
                if(queryResult.getString(queryResult.getColumnIndex("User")) == user){
                    result.add(queryResult.getString(queryResult.getColumnIndex("Item")))
                }

            }while(queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }
    @SuppressLint("Recycle")
    fun checkUserInNoteList(Username : String): Boolean {
        val db:SQLiteDatabase = readableDatabase
        val queryResult : Cursor = db.rawQuery("SELECT * FROM Note_LIST WHERE User = ?", arrayOf(Username))

        return queryResult.columnCount != 0
    }

    //userlist


    fun addUser(Userlist : Userlist):Boolean
    {
        val  db :SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put("Username", Userlist.username)
        cv.put("Password", Userlist.password)
        cv.put("Email", Userlist.email)
        val result = db.insert("User_LIST", null, cv)
        return result != (-1).toLong()
    }

    fun updateUser(Userlist: Userlist){
        val db: SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put("Username", Userlist.username)
        cv.put("Password", Userlist.password)
        db.update("User_List", cv, "_id =?", arrayOf(Userlist.id.toString()))
    }

    fun getUser(): MutableList<Userlist>{
        val result : MutableList<Userlist> = ArrayList()
        val db:SQLiteDatabase = readableDatabase
        val queryResult : Cursor = db.rawQuery("SELECT * FROM User_List", null)
        if (queryResult.moveToFirst())
        {
            do {
                val users = Userlist()
                users.id = queryResult.getLong(queryResult.getColumnIndex("_id"))
                users.username = queryResult.getString(queryResult.getColumnIndex("Username"))
                users.password = queryResult.getString(queryResult.getColumnIndex("Password"))
                users.email = queryResult.getString(queryResult.getColumnIndex("Email"))
                result.add(users)
            }while(queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }

    //itemList

    fun addListItem(item:Itemlist):Boolean{
        val  db :SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put("ItemName", item.itemName)
        cv.put("Category", item.cate)
        cv.put("Quantity", item.qty)
        cv.put("Price", item.price)

        val result = db.insert("Item_LIST", null, cv)
        return result != (-1).toLong()
    }

    fun deleteListItem(itemListId : Long)
    {
        val  db :SQLiteDatabase = writableDatabase
        db.delete("Item_LIST", "_id=?", arrayOf(itemListId.toString()))
    }

    fun updateListItem(item : Itemlist)
    {
        val db :SQLiteDatabase = writableDatabase
        val cv = ContentValues()
        cv.put("ItemName", item.itemName)
        cv.put("Category", item.cate)
        cv.put("Quantity", item.qty)
        cv.put("Price", item.price)

        db.update("Item_LIST", cv, "_id = ?", arrayOf(item.id.toString()))
    }

    fun getListItem() : MutableList<Itemlist>
    {
        val result : MutableList<Itemlist> = ArrayList()
        val db:SQLiteDatabase = readableDatabase
        val queryResult : Cursor = db.rawQuery("SELECT * FROM Item_List", null)
        if (queryResult.moveToFirst())
        {
            do {
                val items = Itemlist()
                items.id = queryResult.getLong(queryResult.getColumnIndex("_id"))
                items.cate = queryResult.getString(queryResult.getColumnIndex("Category"))
                items.itemName = queryResult.getString(queryResult.getColumnIndex("ItemName"))
                items.qty = queryResult.getLong(queryResult.getColumnIndex("Quantity"))
                items.price = queryResult.getLong(queryResult.getColumnIndex("Price"))
                result.add(items)
            }while(queryResult.moveToNext())
        }

        queryResult.close()
        return result
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

}