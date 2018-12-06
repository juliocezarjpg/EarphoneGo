package com.example.ifpb.earphonego

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BancoHelper(context: Context):
        SQLiteOpenHelper(context, "musicas.db", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "create table if not exists musica(" +
                "id integer primary key autoincrement," +
                " nome text," +
                " tempo timestamp)"
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}