package com.example.gestaoautomovel

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddVeiculoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_veiculo)
// Adicione esta linha com o link que aparece na sua imagem do Firebase
        val urlFirebase = "https://gestaoautomovel-8947e-default-rtdb.europe-west1.firebasedatabase.app/"

// Altere esta linha para usar o URL
        val database = FirebaseDatabase.getInstance(urlFirebase).getReference("veiculos")

        val btnGuardar = findViewById<Button>(R.id.btnGuardarVeiculo)

        btnGuardar.setOnClickListener {
            val marca = findViewById<EditText>(R.id.editMarca).text.toString()
            val modelo = findViewById<EditText>(R.id.editModelo).text.toString()
            val matricula = findViewById<EditText>(R.id.editMatricula).text.toString()

            if (marca.isNotEmpty() && matricula.isNotEmpty()) {
                val id = database.push().key ?: ""
                val veiculo = Veiculo(id, marca, modelo, matricula)
                database.child(id).setValue(veiculo)
                Toast.makeText(this, "Veículo Guardado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}