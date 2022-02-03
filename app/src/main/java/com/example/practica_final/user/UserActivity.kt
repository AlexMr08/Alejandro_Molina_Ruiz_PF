package com.example.practica_final.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica_final.*
import com.example.practica_final.databinding.ActivityUserBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class UserActivity : AppCompatActivity() {
    lateinit var lista_cartas : MutableList<Carta>
    private lateinit var binding: ActivityUserBinding
    var carta_sel = Carta()
    val navController by lazy { findNavController(R.id.nav_host_fragment_activity_user) }
    val adap_carta by lazy { UserCardAdapter(lista_cartas, this) }
    val categorias by lazy { resources.getStringArray(R.array.categorias).toList()}
    val appBarConfiguration by lazy { AppBarConfiguration(
        setOf(
            R.id.userHomeFragment, R.id.userEventFragment, R.id.userProfileFragment))}
    val controlSP by lazy { ControlSP(this) }
    lateinit var usuario : Usuario

    val adaptador_pedidos by lazy {
        UserProfileCardAdapter(lista_pedidos, this)
    }
    lateinit var lista_pedidos: MutableList<Pedido>

    var cartas_usuario = 0

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setBackgroundColor(Color.parseColor("#55ffffff"))

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        buscarUsuario()
        lista_cartas = mutableListOf()
        ControlDB.rutacartas.addChildEventListener( object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java)
                if (carta?.disponible == true){
                    lista_cartas.add(carta!!)
                }
                adap_carta.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java)
                if (carta?.disponible == true){
                    lista_cartas.add(carta)
                }else if(carta?.disponible==false){
                    val cr = lista_cartas.filter { it.id == carta.id }.get(0)
                    lista_cartas.removeAt(lista_cartas.indexOf(cr))
                }
                adap_carta.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {/*como no vamos a borrar nada no lo usamos*/}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {/*como no vamos a cambiar la posicion nada no lo usamos*/}

            override fun onCancelled(error: DatabaseError) {

            }
        })

        lista_pedidos = mutableListOf()
        ControlDB.rutaResCartas.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                GlobalScope.launch(Dispatchers.IO) {
                    val sem = CountDownLatch(1)
                    var pedido = snapshot.getValue(Pedido::class.java)
                    if (pedido != null && pedido.idCliente == controlSP.id) {
                        ControlDB.rutacartas.child(pedido.idCarta?:"").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val carta = snapshot.getValue(Carta::class.java)
                                pedido.imgCarta = carta?.imagen
                                pedido.nombreCarta = carta?.nombre
                                if (pedido.estado==0){
                                    cartas_usuario++
                                }
                                sem.countDown()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                        sem.await()
                        lista_pedidos.add(pedido)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        return true
    }

    fun buscarUsuario(){
        GlobalScope.launch(Dispatchers.IO){
            val sem = CountDownLatch(1)
            ControlDB.rutaUsuario.child(controlSP.id).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuario = snapshot?.getValue(Usuario::class.java)!!
                    sem.countDown()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
            sem.await()
        }
    }
}