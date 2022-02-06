package com.example.practica_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.practica_final.admin.AdminActivity
import com.example.practica_final.databinding.ActivityMainBinding
import com.example.practica_final.user.UserActivity
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    val controlSp by lazy{ ControlSP(this) }

    private lateinit var buscado : Usuario


    var nom = ""
    var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        if (controlSp.id!=""){
            when(controlSp.tipo) {
                0 -> {
                    Intent(this, AdminActivity::class.java).apply { startActivity(this) }
                }
                1 -> {
                    Intent(this, UserActivity::class.java).apply { startActivity(this) }
                }
                else -> {
                }
            }
            finish()
        }

        buscado = Usuario()
        binding.mainIni.setOnClickListener {
            nom = binding.inicioTieUser.text.toString().trim()
            pass = binding.inicioTiePass.text.toString().trim()
            if (nom!="" && pass!="") {
                buscarUsuario(nom, pass)
            }else{
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
        binding.mainReg.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun buscarUsuario(nombre:String, contra:String){
        var pojoId : Usuario
        ControlDB.rutaUsuario.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChildren()){
                    pojoId = snapshot.children.iterator().next().getValue(Usuario::class.java) ?: Usuario()
                    if (pojoId.pass==contra){
                        controlSp.id = pojoId.id?:""
                        controlSp.tipo = pojoId.tipo?:1
                        if (pojoId.tipo==1){
                            Intent(applicationContext,UserActivity::class.java).also{startActivity(it)}
                        }else{
                            Intent(applicationContext, AdminActivity::class.java).also{startActivity(it)}
                        }
                        finish()
                    }else{
                        Toast.makeText(applicationContext, "Nombre de usuario/contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Nombre de usuario/contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}