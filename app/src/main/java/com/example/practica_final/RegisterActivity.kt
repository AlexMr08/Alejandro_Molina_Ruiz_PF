package com.example.practica_final

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.aleLib.ControlSP
import com.example.practica_final.databinding.ActivityRegisterBinding
import com.example.practica_final.elementos.Usuario
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
import java.util.regex.Pattern

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

        bind.registerIvPp.setOnClickListener {
            obtener_url.launch("image/*")
        }


        bind.registerBtnReg.setOnClickListener {
            var id_user : String? = null
            val nom = bind.registerTieNom.text.toString().trim()
            val correo = bind.registerTieMail.text.toString().trim()
            val pass1 = bind.registerTiePass1.text.toString().trim()
            val pass2 = bind.registerTiePass2.text.toString().trim()
            val fecha = Calendar.getInstance()
            val formateador = SimpleDateFormat("yyyy-MM-dd")
            val hoy = formateador.format(fecha.time)
            if (isValid()){
                if (contraseñasIguales(pass1,pass2)){
                    if (pass1.length>=8){
                        GlobalScope.launch(Dispatchers.IO) {
                            if (!existeUsuario(nom)) {
                                id_user = ControlDB.rutaUsuario.push().key
                                var url_portada = ""
                                if (url_perfil_local!=null){
                                    url_portada = subirImagen(id_user!!, url_perfil_local!!)
                                }
                                val nuevo_usuario = Usuario(nom,correo,pass1,1,id_user,hoy,url_portada,0)
                                ControlDB.rutaUsuario.child(id_user?:"").setValue(nuevo_usuario)
                                controlSp.id = nuevo_usuario.id?:""
                                controlSp.tipo = nuevo_usuario.tipo?:1

                                val intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                runOnUiThread {
                                    bind.registerTieNom.error = "El nombre de usuario ya existe"
                                }
                            }
                        }
                    }
                }
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

        fun isValid():Boolean{
            	var validated = true
            	val checkers = listOf(
            		Pair(bind.registerTieNom, this::usuarioValido),
                    Pair(bind.registerTieMail, this::correoValido),
                    Pair(bind.registerTiePass1, this::contraseñaValida)
                    //Pair(binding.textview, this::funcion)
            	)
            	for(c in checkers){
            		val x = c.first
            		val f = c.second
            		val y = f(x)
            		validated = y
            		if(!validated) break
            	}
            	return validated
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

    fun usuarioValido(e: EditText):Boolean{
        var valid = true
        if (e.text.toString().trim().length<=2 || e.text.toString().trim().length>16){
            e.error = "El nombre de usuario debe tener entre 3 y 16 caracteres"
            valid = false
        }
        return valid
    }

    fun correoValido(e: EditText):Boolean{
        var valid = true
        if (e.text.isNullOrEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e.text).matches()){
            e.error = "Introduce un correo valido"
            valid=false
        }
        return valid
    }

    fun contraseñaValida(e: EditText):Boolean{
        var valid = true
        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +        //at least 1 lower case letter
                    "(?=.*[A-Z])" +        //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 4 characters
                    "$"
        )

        if (!passwordRegex.matcher(e.text.toString()).matches()){
            bind.tilPass1.error = "La contraseña debe tener al menos 8 caracteres y tener un numero, una minuscula y una mayuscula."
            valid=false
        }
        return valid
    }

    fun contraseñasIguales(c1 : String, c2: String):Boolean{
        var valid = true
        if (!(c1==c2)){
            bind.tilPass2.error = "Las contraseña no coinciden."
            valid=false
        }
        return valid
    }
}
