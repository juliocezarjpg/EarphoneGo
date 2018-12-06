package com.example.ifpb.earphonego

import android.content.ContentValues
import android.content.Context
import android.util.Log
import java.security.AccessControlContext

class MusicaDAO{
    private lateinit var banco: BancoHelper
    private  val TABLE = "musica"

    constructor(context: Context){
        this.banco = BancoHelper(context)
    }

    // Inserir musicas
    fun add(musica: Musica){
        //Log.i("EarphoneGo", "Adicionar no banco")
        val cv = ContentValues()
        cv.put("nome", musica.nome)
        cv.put("tempo", musica.tempo.timeInMillis)
        //Log.i("EarphoneGo", cv.toString())
        this.banco.writableDatabase.insert(TABLE, null, cv)
    }

    // Select
    fun read(): ArrayList<Musica>{
        val colunas = arrayOf("id", "nome", "tempo")
        val lista = ArrayList<Musica>()

        val cursor = this.banco.readableDatabase.query(TABLE, colunas, null, null, null, null, null)

        if (cursor.count > 0){
            cursor.moveToFirst()
            do{
                var musica = Musica()
                musica.id = cursor.getInt(cursor.getColumnIndex("id"))
                musica.nome = cursor.getString(cursor.getColumnIndex("nome"))
                musica.tempo.timeInMillis = cursor.getLong(cursor.getColumnIndex("tempo"))
                lista.add(musica)
            } while (cursor.moveToNext())
        }

        return lista
    }

    // Select com Where
    fun read(id: Int): Musica?{
        val colunas = arrayOf("id", "nome", "tempo")
        val where = "id = ?"
        val pWhere = arrayOf(id.toString())
        val cursor = this.banco.readableDatabase.query(TABLE, colunas, where, pWhere, null, null, null)

        if (cursor.count > 0){
            cursor.moveToFirst()
            var musica = Musica()
            musica.id = cursor.getInt(cursor.getColumnIndex("id"))
            musica.nome = cursor.getString(cursor.getColumnIndex("nome"))
            musica.tempo.timeInMillis = cursor.getLong(cursor.getColumnIndex("tempo"))
            return musica
        }

        return null
    }

    // Atualizar a lista de Musicas
    fun update(musica: Musica){
        val where = "id = ?"
        val pWhere = arrayOf(musica.id.toString())
        val cv = ContentValues()
        cv.put("nome", musica.nome)
        this.banco.writableDatabase.update(TABLE, cv, where, pWhere)
    }

    // Remover alguma musica da lista de Musicas, pelo ID
    fun delete(id: Int){
        val where = "id = ?"
        val pWhere = arrayOf(id.toString())
        this.banco.writableDatabase.delete(TABLE, where, pWhere)
    }


}