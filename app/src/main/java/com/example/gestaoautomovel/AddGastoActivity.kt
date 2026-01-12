package com.example.gestaoautomovel
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddGastoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_gasto)

        val veiculoId = intent.getStringExtra("VEICULO_ID") ?: ""
        val dbGastos = FirebaseDatabase.getInstance().getReference("gastos").child(veiculoId)

        val editLitros = findViewById<EditText>(R.id.editLitros)
        val radioCombustivel = findViewById<RadioButton>(R.id.radioCombustivel)

        findViewById<RadioButton>(R.id.radioOutro).setOnClickListener { editLitros.visibility = View.GONE }
        radioCombustivel.setOnClickListener { editLitros.visibility = View.VISIBLE }

        findViewById<Button>(R.id.btnSalvarGasto).setOnClickListener {
            val tipo = if (radioCombustivel.isChecked) "Combustivel" else "Despesa"
            val valor = findViewById<EditText>(R.id.editValor).text.toString().toDoubleOrNull() ?: 0.0
            val km = findViewById<EditText>(R.id.editKmAtuais).text.toString().toDoubleOrNull() ?: 0.0
            val litros = if (tipo == "Combustivel") editLitros.text.toString().toDoubleOrNull() ?: 0.0 else 0.0

            val id = dbGastos.push().key ?: ""
            val gasto = Gasto(id, tipo, valor, litros, km)
            dbGastos.child(id).setValue(gasto)
            finish()
        }
    }
}