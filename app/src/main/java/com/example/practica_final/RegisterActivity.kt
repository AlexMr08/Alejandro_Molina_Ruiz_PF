package com.example.practica_final

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practica_final.databinding.ActivityRegisterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch

class RegisterActivity : AppCompatActivity() {
    private lateinit var bind : ActivityRegisterBinding
    val controlSp by lazy{ ControlSP(this) }
    var nombre = ""
    private var url_perfil_local: Uri?=null
    var pass = ""


    val obtener_url= registerForActivityResult(ActivityResultContracts.GetContent()){
            uri: Uri?->
        when (uri){
            null-> Toast.makeText(applicationContext,"No has seleccionado una imagen", Toast.LENGTH_SHORT).show()
            else->{
                url_perfil_local=uri
                bind.registerIvPp.setImageURI(url_perfil_local)
                Toast.makeText(applicationContext,"Has seleccionado una imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityRegisterBinding.inflate(layoutInflater)
        val view = bind.root
        setContentView(view)
    }

    override fun onStart() {
        super.onStart()

        bind.button2.setOnClickListener {
            finish()
        }

        bind.registerIvPp.setOnClickListener {
            obtener_url.launch("image/*")
        }


        bind.registerBtnReg.setOnClickListener {
            var id_user : String? = null
            val nom = bind.registerTieNom.text.toString().trim()
            val pass1 = bind.registerTiePass1.text.toString().trim()
            val pass2 = bind.registerTiePass2.text.toString().trim()
            val fecha = Calendar.getInstance()
            val formateador = SimpleDateFormat("yyyy-MM-dd")
            val hoy = formateador.format(fecha.time)
            if (nom!="" && pass1!="" && pass2!=""){
                if (pass1==pass2){
                    if (pass1.length>=8){
                        GlobalScope.launch(Dispatchers.IO) {
                            if (!existeUsuario(nom)) {
                                id_user = ControlDB.rutaUsuario.push().key
                                var url_portada = ""
                                if (url_perfil_local!=null){
                                    url_portada = subirImagen(id_user!!, url_perfil_local!!)
                                    tostadaCorrutina(url_portada)
                                }
                                val nuevo_usuario = Usuario(nom,pass1,1,id_user,hoy,url_portada,0)
                                ControlDB.rutaUsuario.child(id_user?:"").setValue(nuevo_usuario)
                                controlSp.id = nuevo_usuario.id?:""
                                controlSp.tipo = nuevo_usuario.tipo?:1

                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                tostadaCorrutina("El nombre de usuario ya existe.")
                            }
                        }
                    }else{
                        Toast.makeText(this, "La contraseña debe tener mas de 8 caracteres", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private  fun tostadaCorrutina(texto:String){
        runOnUiThread{
            Toast.makeText(
                applicationContext,
                texto,
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private suspend fun subirImagen(id:String,imagen:Uri):String{
        val url = ControlDB.stoRutaUsuarios.child(id).putFile(imagen).await().storage.downloadUrl.await().toString()
        return url
    }

    fun existeUsuario(nombre: String):Boolean{
        var res:Boolean?= false

        val sem = CountDownLatch(1)

        ControlDB.rutaUsuario.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(Usuario::class.java)!=null){
                    res=true
                }
                sem.countDown()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        sem.await()
        return res!!
    }
}
