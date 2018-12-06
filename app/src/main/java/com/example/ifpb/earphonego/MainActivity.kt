package com.example.ifpb.earphonego

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var lista: ListView
    private lateinit var dao: MusicaDAO

    val ADD = 1
    val EDIT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        this.lista = findViewById(R.id.lvMainList)
        this.dao = MusicaDAO(context = this)

        this.lista.adapter = MusicaAdapter(this)


        //Inserir musica nova
        fab.setOnClickListener { view ->
            val intent = Intent(this, FormActivity::class.java)
            startActivityForResult(intent, ADD)
        }

        //Editar uma musica que ja esta na lista
        this.lista.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, FormActivity::class.java)
            val musica = this.lista.adapter.getItem(position) as Musica
            //Log.i("AMIGO", this.dao.read(amigo.id).toString()) // forçar ler read(id)
            intent.putExtra("MUSICA", musica)
            startActivityForResult(intent, EDIT)
        }

        this.lista.setOnItemLongClickListener { parent, view, position, id ->
            val musica = this.lista.adapter.getItem(position) as Musica
            this.dao.delete(musica.id)
            this.atualizar()
            true
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun atualizar(){
        //(this.lista.adapter as ArrayAdapter<Amigo>).notifyDataSetChanged()
        //val adapter = this.lista.adapter as ArrayAdapter<Amigo>
        //adapter.clear()
        //adapter.addAll(this.dao.read())
        this.lista.adapter = MusicaAdapter(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            val musica = data?.getSerializableExtra("MUSICA") as Musica
            //Log.i("AMIGO", amigo.toString())

            if (requestCode == ADD){
                this.dao.add(musica)
                //Log.i("AMIGO", this.dao.read().toString())
            }else{
                this.dao.update(musica)
            }
            this.atualizar()
        }
    }
}
