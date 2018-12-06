package com.example.ifpb.earphonego

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class FormActivity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var btSalvar: Button
    private lateinit var btCancelar: Button
    private lateinit var musica: Musica

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        this.etNome = findViewById(R.id.etFormNome)
        this.btSalvar = findViewById(R.id.btFormSalvar)
        this.btCancelar = findViewById(R.id.btFormCancelar)

        if (intent.getSerializableExtra("MUSICA") != null){
            this.musica = intent.getSerializableExtra("MUSICA") as Musica
            this.etNome.text.append(this.musica.nome)
        }else{
            this.musica = Musica()
        }

        this.btSalvar.setOnClickListener({ salvar(it) })

        this.btCancelar.setOnClickListener({
            this.finish()
        })

    }

    private fun salvar(view: View){
        val nome = this.etNome.text.toString()
        this.musica.nome = nome

        val intent = Intent()
        intent.putExtra("MUSICA", this.musica)
        setResult(Activity.RESULT_OK, intent)

        finish()
    }
}
