package com.example.practica_final.user

import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica_final.*
import com.example.practica_final.databinding.ActivityUserBinding
import com.example.practica_final.user.adapters.UserCardAdapter
import com.example.practica_final.user.adapters.UserEventAdapter
import com.example.practica_final.user.adapters.UserProfileCardAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class UserActivity : AppCompatActivity() {
    lateinit var Menu : Menu
    val navView by lazy { binding.navView }
    var lista_cartas = buscarCartas()
    var lista_eventos = buscarEventos()
    private lateinit var binding: ActivityUserBinding
    var carta_sel = Carta()
    val navController by lazy { findNavController(R.id.nav_host_fragment_activity_user) }
    val adap_carta by lazy { UserCardAdapter(lista_cartas, this) }
    val adap_evento by lazy { UserEventAdapter(lista_eventos,this) }
    val appBarConfiguration by lazy { AppBarConfiguration(
        setOf(
            R.id.userHomeFragment,R.id.userViewCardFragment, R.id.userEventFragment, R.id.userProfileFragment))}

    val controlSP by lazy { ControlSP(this) }
    var usuario = Usuario()

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


        navView.setBackgroundResource(R.drawable.gradient_bg)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

         buscarUsuario()

//        lista_cartas = mutableListOf()
//        buscarCartas()


        lista_pedidos = mutableListOf()
        buscarPedidos()

        navView.setOnItemReselectedListener{}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        Menu = menu
        (menu.findItem(R.id.app_bar_search).actionView as SearchView)
            .setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adap_carta.filter.filter(p0)
                    return false
                }
            })

        return true
    }

    fun buscarUsuario(){
            ControlDB.rutaUsuario.child(controlSP.id).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuario = snapshot?.getValue(Usuario::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun buscarCartas():MutableList<Carta>{
        var lista_cartas = mutableListOf<Carta>()
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
        return lista_cartas
    }

    fun buscarPedidos(){
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
                                if (pedido.estado==1){
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

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                GlobalScope.launch(Dispatchers.IO) {
                    var pedido = snapshot.getValue(Pedido::class.java)
                    val index = lista_pedidos.map { it.id }.indexOf(pedido?.id)
                    val sem = CountDownLatch(1)
                    if (pedido != null && pedido.idCliente == controlSP.id) {
                        ControlDB.rutacartas.child(pedido.idCarta?:"").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val carta = snapshot.getValue(Carta::class.java)
                                pedido.imgCarta = carta?.imagen
                                pedido.nombreCarta = carta?.nombre
                                if (pedido.estado==1){
                                    cartas_usuario++
                                }
                                sem.countDown()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                        sem.await()
                        runOnUiThread {
                            lista_pedidos[index]=pedido
                            adaptador_pedidos.notifyItemChanged(index)
                        }
                    }

                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun buscarEventos():MutableList<Evento>{
        var lista_evento = mutableListOf<Evento>()
        ControlDB.rutaEvento.addChildEventListener( object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java)
                if (evento?.disponible == true){
                    lista_evento.add(evento!!)
                }
                adap_evento.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java)
                if (evento?.disponible == true){
                    lista_evento.add(evento!!)
                }else if(evento?.disponible==false){
                    val cr = lista_evento.filter { it.id == evento.id }.get(0)
                    lista_evento.removeAt(lista_evento.indexOf(cr))
                }
                adap_evento.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {/*como no vamos a borrar nada no lo usamos*/}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {/*como no vamos a cambiar la posicion nada no lo usamos*/}

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return lista_evento
    }

    fun algoraro(modo:Int){
        if (modo == 1){
            navView.setOnItemReselectedListener{ item ->
                if (item.itemId == R.id.userHomeFragment) {
                    navController.popBackStack()
                    return@setOnItemReselectedListener
                }

            }
        }else{
            navView.setOnItemReselectedListener{}
        }
    }
}