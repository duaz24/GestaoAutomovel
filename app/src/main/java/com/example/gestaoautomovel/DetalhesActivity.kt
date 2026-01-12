package com.example.gestaoautomovel

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class DetalhesActivity : AppCompatActivity() {
    private lateinit var dbGastos: DatabaseReference
    private val historicoLista = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes)

        val veiculoId = intent.getStringExtra("VEICULO_ID") ?: ""
        val nomeVeiculo = intent.getStringExtra("VEICULO_NOME") ?: ""

        dbGastos = FirebaseDatabase.getInstance().getReference("gastos").child(veiculoId)
        findViewById<TextView>(R.id.txtDetalheTitulo).text = nomeVeiculo

        findViewById<Button>(R.id.btnAddGasto).setOnClickListener {
            val intent = Intent(this, AddGastoActivity::class.java)
            intent.putExtra("VEICULO_ID", veiculoId)
            startActivity(intent)
        }

        carregarDadosEBasculharConsumo()
    }

    private fun carregarDadosEBasculharConsumo() {
        dbGastos.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historicoLista.clear()
                var totalLitros = 0.0
                var minKm = Double.MAX_VALUE
                var maxKm = 0.0

                for (postSnapshot in snapshot.children) {
                    val gasto = postSnapshot.getValue(Gasto::class.java) ?: continue
                    historicoLista.add("${gasto.tipo}: ${gasto.valor}€ (${gasto.kmAtuais}km)")

                    if (gasto.tipo == "Combustivel") {
                        totalLitros += gasto.litros
                        if (gasto.kmAtuais < minKm) minKm = gasto.kmAtuais
                        if (gasto.kmAtuais > maxKm) maxKm = gasto.kmAtuais
                    }
                }

                // Cálculo de consumo: (Litros / Distância) * 100
                if (maxKm > minKm && totalLitros > 0) {
                    val media = (totalLitros / (maxKm - minKm)) * 100
                    findViewById<TextView>(R.id.txtMediaConsumo).text = "Média: %.2f L/100km".format(media)
                } else {
                    findViewById<TextView>(R.id.txtMediaConsumo).text = "Média: (insira 2 ou + abastecimentos)"
                }

                val adapter = ArrayAdapter(this@DetalhesActivity, android.R.layout.simple_list_item_1, historicoLista)
                findViewById<ListView>(R.id.listHistorico).adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}