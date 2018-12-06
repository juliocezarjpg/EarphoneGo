package com.example.ifpb.earphonego

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class MusicaAdapter(var context: Context): BaseAdapter(){
    private lateinit var dao: MusicaDAO
    private lateinit var lista: ArrayList<Musica>

    init {
        this.dao = MusicaDAO(context)
        this.lista = this.dao.read()
    }

    override fun getCount(): Int {
        return this.lista.size
    }

    override fun getItem(position: Int): Any {
        return this.lista[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val musica = this.lista[position]
        val layout: View

        if (convertView == null){
            layout = View.inflate(context, R.layout.musica_layout, null)
        }else{
            layout = convertView
        }

        if (position % 2 == 0){
            layout.setBackgroundColor(Color.rgb(29,185,84))
        }else{
            layout.setBackgroundColor(Color.WHITE)
        }

        val tvNome = layout.findViewById<TextView>(R.id.tvMusicaNome)
        val tvData = layout.findViewById<TextView>(R.id.tvMusicaData)

        tvNome.text = musica.nome
        tvData.text = musica.tempoStr()

        return layout
    }
}