package com.example.ratnikov4bilet

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//Название базы данных и таблиц в ней
val DATABASE_NAME = "MASTERSKAYA"

val FIRST_TABLE_NAME = "DEVELOPERS"
val SECOND_TABLE_NAME = "CATEGORYAUTO"
val THIRD_TABLE_NAME = "CLIENTS"
val FORTH_TABLE_NAME = "CATEGORYREMONT"
val FIVE_TABLE_NAME = "REMONTS"

//1 Таблица
val DEVELOPERS_ID = "id"
val DEVELOPERS_NAME = "name"

//2 Таблица
val CATEGORY_AUTO_ID = "id"
val CATEGORY_AUTO_NAME = "name"

//3 Таблица
val CLIENTS_ID = "id"
val CLIENTS_FIO = "name"
val CLIENTS_ADRES = "adres"

//4 Таблица
val CATEGORY_REMONT_ID = "id"
val CATEGORY_REMONT_NAME = "name"

//5 Таблица
val REMONTS_ID = "id"
val REMONTS_ID_CLIENT = "idclient"
val REMONTS_ID_AUTO = "idauto"
val REMONTS_ID_REMONT = "idremont"
val REMONTS_AUTO_NAME = "autoname"
val REMONTS_OBRASHENIE = "obrashenie"
val REMONTS_ISPOLNENIE = "ispolnenie"

class DataBaseHandler(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(p0: SQLiteDatabase?) {
        val createFirst = "CREATE TABLE " + FIRST_TABLE_NAME + " (" +
                DEVELOPERS_ID + " INTEGER PRIMARY KEY," +
                DEVELOPERS_NAME + " TEXT)";
        p0?.execSQL(createFirst)

        val createSecond = "CREATE TABLE " + SECOND_TABLE_NAME + " (" +
                CATEGORY_AUTO_ID + " INTEGER PRIMARY KEY," +
                CATEGORY_AUTO_NAME + " TEXT)";
        p0?.execSQL(createSecond)

        val createThird = "CREATE TABLE " + THIRD_TABLE_NAME + " (" +
                CLIENTS_ID + " INTEGER PRIMARY KEY," +
                CLIENTS_FIO + " TEXT," +
                CLIENTS_ADRES + " TEXT)";
        p0?.execSQL(createThird)

        val createFour = "CREATE TABLE " + FORTH_TABLE_NAME + " (" +
                CATEGORY_REMONT_ID + " INTEGER PRIMARY KEY," +
                CATEGORY_REMONT_NAME + " TEXT)";
        p0?.execSQL(createFour)

        val createFive = "CREATE TABLE " + FIVE_TABLE_NAME + " (" +
                REMONTS_ID + " INTEGER PRIMARY KEY," +
                REMONTS_ID_CLIENT + " INTEGER," +
                REMONTS_ID_AUTO + " INTEGER," +
                REMONTS_ID_REMONT + " INTEGER," +
                REMONTS_AUTO_NAME + " TEXT," +
                REMONTS_OBRASHENIE + " TEXT," +
                REMONTS_ISPOLNENIE + " TEXT)";
        p0?.execSQL(createFive)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS" + FIRST_TABLE_NAME)
        onCreate(p0)
    }

    //Чтение данных из первой таблицы
    @SuppressLint("Range")
    fun readFirst():MutableList<Developers>{
        val list = mutableListOf<Developers>()
        val db = this.readableDatabase

        val query = "SELECT * FROM " + FIRST_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var developer = Developers()
                developer.Id = result.getString(result.getColumnIndex(DEVELOPERS_ID)).toInt()
                developer.Name = result.getString(result.getColumnIndex(DEVELOPERS_NAME))
                list.add(developer)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    //Чтение данных из второй таблицы
    @SuppressLint("Range")
    fun readSecond():MutableList<Auto>{
        val list = mutableListOf<Auto>()
        val db = this.readableDatabase

        val query = "SELECT * FROM " + SECOND_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var auto = Auto()
                auto.Id = result.getString(result.getColumnIndex(CATEGORY_AUTO_ID)).toInt()
                auto.Name = result.getString(result.getColumnIndex(CATEGORY_AUTO_NAME))
                list.add(auto)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    //Чтение данных из третьей таблицы
    @SuppressLint("Range")
    fun readThird():MutableList<Clients>{
        val list = mutableListOf<Clients>()
        val db = this.readableDatabase

        val query = "SELECT * FROM " + THIRD_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var client = Clients()
                client.Id = result.getString(result.getColumnIndex(CLIENTS_ID)).toInt()
                client.Name = result.getString(result.getColumnIndex(CLIENTS_FIO))
                client.Adres= result.getString(result.getColumnIndex(CLIENTS_ADRES))
                list.add(client)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    //Чтение данных из четвёртой таблицы
    @SuppressLint("Range")
    fun readFour():MutableList<CategoryRemont>{
        val list = mutableListOf<CategoryRemont>()
        val db = this.readableDatabase

        val query = "SELECT * FROM " + FORTH_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var category = CategoryRemont()
                category.Id = result.getString(result.getColumnIndex(CATEGORY_REMONT_ID)).toInt()
                category.Name = result.getString(result.getColumnIndex(CATEGORY_REMONT_NAME))
                list.add(category)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    //Чтение данных из пятой таблицы
    @SuppressLint("Range")
    fun readFive():MutableList<Remont>{
        val list = mutableListOf<Remont>()
        val db = this.readableDatabase

        val query = "SELECT * FROM " + FIVE_TABLE_NAME
        val result = db.rawQuery(query,null)
        if(result.moveToFirst()){
            do{
                var remont = Remont()
                remont.Id = result.getString(result.getColumnIndex(REMONTS_ID)).toInt()
                remont.Id_Client = result.getString(result.getColumnIndex(REMONTS_ID_CLIENT)).toInt()
                remont.Id_Auto = result.getString(result.getColumnIndex(REMONTS_ID_AUTO)).toInt()
                remont.Id_Category = result.getString(result.getColumnIndex(REMONTS_ID_REMONT)).toInt()
                remont.Id_Auto_Name = result.getString(result.getColumnIndex(REMONTS_AUTO_NAME))
                remont.DataObrashenie = result.getString(result.getColumnIndex(REMONTS_OBRASHENIE))
                remont.DataIspolnenia = result.getString(result.getColumnIndex(REMONTS_ISPOLNENIE))
                list.add(remont)
            }while(result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    //Удаление в первой таблице
    fun firstDelete(developers: Developers){
        val db = this.writableDatabase
        db.delete(FIRST_TABLE_NAME, DEVELOPERS_ID + "=" + developers.Id, null)
        db.close()
    }

    //Удаление в второй таблице
    fun secondDelete(auto: Auto){
        val db = this.writableDatabase
        db.delete(SECOND_TABLE_NAME, CATEGORY_AUTO_ID + "=" + auto.Id, null)
        db.close()
    }

    //Удаление в третьей таблице
    fun thirdDelete(clients: Clients){
        val db = this.writableDatabase
        db.delete(THIRD_TABLE_NAME, CLIENTS_ID + "=" + clients.Id, null)
        db.close()
    }

    //Удаление в четвёртой таблице
    fun fourDelete(categoryRemont: CategoryRemont){
        val db = this.writableDatabase
        db.delete(FORTH_TABLE_NAME, CATEGORY_REMONT_ID + "=" + categoryRemont.Id, null)
        db.close()
    }

    //Удаление в пятой таблице
    fun fiveDelete(remont: Remont){
        val db = this.writableDatabase
        db.delete(FIVE_TABLE_NAME, REMONTS_ID + "=" + remont.Id, null)
        db.close()
    }

    //Обновление и сохранение первой таблицы
    fun firstUpdate(data:MutableList<Developers>){
        val db = this.writableDatabase
        db.delete(FIRST_TABLE_NAME,null,null)

        val cv = ContentValues()

        data.forEach(){
            cv.put(DEVELOPERS_NAME, it.Name)
            db.insert(FIRST_TABLE_NAME,null, cv)
        }
    }

    //Обновление и сохранение второй таблицы
    fun secondUpdate(data:MutableList<Auto>){
        val db = this.writableDatabase
        db.delete(SECOND_TABLE_NAME,null,null)

        val cv = ContentValues()

        data.forEach(){
            cv.put(CATEGORY_AUTO_NAME, it.Name)
            db.insert(SECOND_TABLE_NAME,null, cv)
        }
    }

    //Обновление и сохранение третьей таблицы
    fun thirdUpdate(data:MutableList<Clients>){
        val db = this.writableDatabase
        db.delete(THIRD_TABLE_NAME,null,null)

        val cv = ContentValues()

        data.forEach(){
            cv.put(CLIENTS_FIO, it.Name)
            cv.put(CLIENTS_ADRES, it.Adres)
            db.insert(THIRD_TABLE_NAME,null, cv)
        }
    }

    //Обновление и сохранение первой таблицы
    fun fourUpdate(data:MutableList<CategoryRemont>){
        val db = this.writableDatabase
        db.delete(FORTH_TABLE_NAME,null,null)

        val cv = ContentValues()

        data.forEach(){
            cv.put(CATEGORY_REMONT_NAME, it.Name)
            db.insert(FORTH_TABLE_NAME,null, cv)
        }
    }

    //Обновление и сохранение первой таблицы
    fun fiveUpdate(data:MutableList<Remont>){
        val db = this.writableDatabase
        db.delete(FIVE_TABLE_NAME,null,null)

        val cv = ContentValues()

        data.forEach(){
            cv.put(REMONTS_ID_CLIENT, it.Id_Client)
            cv.put(REMONTS_ID_AUTO, it.Id_Auto)
            cv.put(REMONTS_ID_REMONT, it.Id_Category)
            cv.put(REMONTS_AUTO_NAME, it.Id_Auto_Name)
            cv.put(REMONTS_OBRASHENIE, it.DataObrashenie)
            cv.put(REMONTS_ISPOLNENIE, it.DataIspolnenia)
            db.insert(FIVE_TABLE_NAME,null, cv)
        }
    }

}