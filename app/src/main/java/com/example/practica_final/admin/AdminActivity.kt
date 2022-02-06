package com.example.practica_final.admin

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.practica_final.*
import com.example.practica_final.admin.adapters.AdminCardAdapter
import com.example.practica_final.admin.adapters.AdminEventAdapter
import com.example.practica_final.databinding.ActivityAdminBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    val navController by lazy { findNavController(R.id.nav_host_fragment_content_admin) }
    lateinit var lista_cartas: MutableList<Carta>
    val adap_carta by lazy { AdminCardAdapter(lista_cartas, this) }
    val categorias by lazy { resources.getStringArray(R.array.categorias).toList() }
    lateinit var lista_pedidos: MutableList<Pedido>
    val adap_pedido by lazy { AdminOrdersAdapter(lista_pedidos, this) }
    val adap_eventos by lazy { AdminEventAdapter(lista_eventos, this) }
    lateinit var lista_eventos: MutableList<Evento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.adminHomeFragment, R.id.adminEventFragment, R.id.adminOrderFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        lista_cartas = mutableListOf()
        ControlDB.rutacartas.addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java)
                lista_cartas.add(carta ?: Carta())
                adap_carta.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java)
                val modificada = lista_cartas.indexOf(lista_cartas.filter { it.id == carta?.id }[0])
                lista_cartas[modificada].disponible = carta?.disponible
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {/*como no vamos a borrar nada no lo usamos*/
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {/*como no vamos a cambiar la posicion nada no lo usamos*/
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        lista_pedidos = mutableListOf()
        ControlDB.rutaResCartas.orderByChild("fecha").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var pedido = snapshot.getValue(Pedido::class.java)
                GlobalScope.launch(Dispatchers.IO) {
                    val sem = CountDownLatch(2)
                    if (pedido != null) {
                        //Consulta carta
                        ControlDB.rutacartas.child(pedido.idCarta?:"").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val carta = snapshot.getValue(Carta::class.java)
                                pedido.imgCarta = carta?.imagen
                                pedido.nombreCarta = carta?.nombre
                                sem.countDown()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })

                        //Consulta usuario
                        ControlDB.rutaUsuario.child(pedido.idCliente?:"").addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val cliente = snapshot.getValue(Usuario::class.java)
                                pedido.imgCliente = cliente?.img
                                pedido.nombreCliente = cliente?.nombre
                                sem.countDown()
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                        sem.await()
                        lista_pedidos.add(pedido)
                        runOnUiThread {
                            adap_pedido.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    var pedido = snapshot.getValue(Pedido::class.java)
                    val index = lista_pedidos.map { it.id }.indexOf(pedido?.id)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        lista_eventos = buscarEventos()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun FAB_manager(mode: Int, listener: (View) -> Unit) {
        //si no necesitas añadir cosas a la lista desde un fragmento quita el listener
        when (mode) {
            1 -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_post_add_24)
                    setOnClickListener {
                        navController.navigate(R.id.adminAddCardFragment)
                    }
                }
            }
            2 -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_add_location_24)
                    setOnClickListener {
                        navController.navigate(R.id.action_adminEventFragment_to_adminAddEventFragment)
                    }
                }
            }
            3 -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.VISIBLE
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener(listener)
                }
            }
            else -> {
                (binding.appBarAdmin.fab).apply {
                    visibility = View.INVISIBLE
                }
            }
        }
    }

    fun buscarEventos():MutableList<Evento>{
        var lista = mutableListOf<Evento>()
        ControlDB.rutaEvento.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (snapshot!=null){
                    val evento = snapshot.getValue(Evento::class.java)
                    lista.add(evento?: Evento())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java)
                val modificado = lista_eventos.indexOf(lista_eventos.filter { it.id == evento?.id }[0])
                lista_eventos[modificado].disponible = evento?.disponible
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
        return lista
    }
}