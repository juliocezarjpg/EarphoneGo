package com.example.ifpb.earphonego

import android.app.Activity
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.ContextCompat.startActivity
import android.app.SearchManager
import android.content.ComponentName
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_MEDIA_PLAY
import android.app.NotificationManager









class MainActivity : AppCompatActivity() {
    private lateinit var lista: ListView
    private lateinit var dao: MusicaDAO
    private lateinit var earphoneReceiver: earphoneGoReceiver

    private lateinit var ifEarphone: IntentFilter

    val ADD = 1
    val EDIT = 2
    val NOTIFICATION = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        this.lista = findViewById(R.id.lvMainList)
        this.dao = MusicaDAO(context = this)

        this.lista.adapter = MusicaAdapter(this)

        this.earphoneReceiver = earphoneGoReceiver()

        this.ifEarphone = IntentFilter()
        this.ifEarphone.addAction(Intent.ACTION_HEADSET_PLUG)


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

            val intent = Intent(Intent.ACTION_MAIN)
            intent.action = MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
            intent.setComponent(ComponentName("com.spotify.music", "com.spotify.music.MainActivity"))
            intent.putExtra(SearchManager.QUERY, musica.nome)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

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
            //Log.i("EarphoneGo", musica.toString())

            if (requestCode == ADD){
                this.dao.add(musica)
                //Log.i("EarphoneGo", this.dao.read().toString())
            }else{
                this.dao.update(musica)
            }
            this.atualizar()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(this.earphoneGoReceiver(), this.ifEarphone)
    }

    inner class earphoneGoReceiver: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_HEADSET_PLUG){

                Log.i("EarphoneGo", intent.getIntExtra("state", -1).toString())
                Log.i("EarphoneGo", "Oi")

                val i = Intent(Intent.ACTION_MEDIA_BUTTON)
                i.component = ComponentName("com.spotify.music", "com.spotify.music.internal.receiver.MediaButtonReceiver")
                i.putExtra(Intent.EXTRA_KEY_EVENT,  KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY))
                sendOrderedBroadcast(i, null)


                if (intent.getIntExtra("state", -1) == 1) {
                    val intent = Intent(Intent.ACTION_MAIN)
                    intent.action = MediaStore.INTENT_ACTION_MUSIC_PLAYER // Mudar para Query
                    //intent.action = MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH
                    intent.setComponent(ComponentName("com.spotify.music", "com.spotify.music.MainActivity"))
                    //intent.putExtra(SearchManager.QUERY, trackName)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context!!.startActivity(intent)
                }

            }

        }
    }

}
