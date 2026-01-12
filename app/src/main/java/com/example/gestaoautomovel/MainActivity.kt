package com.example.gestaoautomovel

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var databaseVeiculos: DatabaseReference
    private lateinit var listViewVeiculos: ListView
    private val veiculoList = mutableListOf<Veiculo>()
    private val veiculoNomes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Adicione esta linha com o link que aparece na sua imagem do Firebase
        val urlFirebase = "https://gestaoautomovel-8947e-default-rtdb.europe-west1.firebasedatabase.app/"


        val database = FirebaseDatabase.getInstance(urlFirebase).getReference("veiculos")

        databaseVeiculos = FirebaseDatabase.getInstance().getReference("veiculos")
        listViewVeiculos = findViewById(R.id.listViewVeiculos)
        val fab: FloatingActionButton = findViewById(R.id.fabAdd)

        fab.setOnClickListener {
            startActivity(Intent(this, AddVeiculoActivity::class.java))
        }

        listViewVeiculos.setOnItemClickListener { _, _, position, _ ->
            val v = veiculoList[position]
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("VEICULO_ID", v.id)
            intent.putExtra("VEICULO_NOME", "${v.marca} ${v.modelo}")
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        databaseVeiculos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                veiculoList.clear()
                veiculoNomes.clear()
                for (postSnapshot in snapshot.children) {
                    val v = postSnapshot.getValue(Veiculo::class.java)
                    if (v != null) {
                        veiculoList.add(v)
                        veiculoNomes.add("${v.marca} ${v.modelo} (${v.matricula})")
                    }
                }

                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, veiculoNomes)
                listViewVeiculos.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}