package com.example.practica_final.user

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica_final.*
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.aleLib.ControlNotif
import com.example.practica_final.aleLib.ControlSP
import com.example.practica_final.databinding.ActivityUserBinding
import com.example.practica_final.elementos.*
import com.example.practica_final.user.adapters.UserCardAdapter
import com.example.practica_final.user.adapters.UserEventAdapter
import com.example.practica_final.user.adapters.UserOrderAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

class UserActivity : AppCompatActivity() {
    lateinit var menu: Menu
    private lateinit var generador: AtomicInteger
    private val navView by lazy { binding.navView }

    var lista_cartas = mutableListOf<Carta>()
    var lista_eventos = mutableListOf<Evento>()
    var lista_reserva = mutableListOf<Reserva>()
    var lista_pedidos = mutableListOf<Pedido>()
    private lateinit var binding: ActivityUserBinding
    var carta_sel = Carta()
    var evento_sel = Evento()
    val navController by lazy { findNavController(R.id.nav_host_fragment_activity_user) }
    val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.userHomeFragment,
                R.id.userEventFragment,
                R.id.userProfileFragment,
                R.id.userOrdersFragment
            )
        )
    }

    val controlSP by lazy { ControlSP(applicationContext) }
    lateinit var lista_monedas: List<Moneda>
    lateinit var moneda_sel: Moneda
    var usuario = Usuario()

    lateinit var adap_pedidos: UserOrderAdapter
    lateinit var adap_carta: UserCardAdapter
    lateinit var adap_evento: UserEventAdapter


    var cartas_usuario = 0

    override fun onSupportNavigateUp(): Boolean {
//        if (navController.currentDestination == navController.findDestination(R.id.userViewCardFragment)){
//            navController.navigate(R.id.action_userViewCardFragment_to_userHomeFragment)
//        }
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val nom_mon = resources.getStringArray(R.array.nom_monedas).toList()
        val sig_mon = resources.getStringArray(R.array.sig_monedas).toList()
        val val_mon = listOf<Float>(1.0f, controlSP.eur_usd)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (controlSP.tipo != 1 || controlSP.id == "") {
            Intent(this, MainActivity::class.java).also { startActivity(it) }
            finish()
        }
        lista_monedas = nom_mon.mapIndexed { index, s -> Moneda(s, sig_mon[index], val_mon[index]) }
        moneda_sel = lista_monedas[controlSP.moneda_sel]
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        buscarUsuario()
        generador = AtomicInteger(0)
        lista_cartas = buscarCartas()
        lista_eventos = buscarEventos()
        lista_pedidos = buscarPedidos()



        adap_carta = UserCardAdapter(lista_cartas, this)
        adap_pedidos = UserOrderAdapter(lista_pedidos, this)
        adap_evento = UserEventAdapter(lista_eventos, this)

        navView.setOnItemSelectedListener(this::bottomNavNavigation)

        navView.setOnItemReselectedListener { }
    }

    override fun onStart() {
        super.onStart()
        navView.setBackgroundResource(R.drawable.gradient_bg)
        moneda_sel = lista_monedas[controlSP.moneda_sel]
        adap_carta.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        this.menu = menu
        (menu.findItem(R.id.app_bar_search).actionView as SearchView)
            .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    adap_carta.filter.filter(p0)
                    return false
                }
            })
        (menu.findItem(R.id.user_menu_creador)).setOnMenuItemClickListener {
            Intent(this, Creador::class.java).also { intent -> startActivity(intent) }
            return@setOnMenuItemClickListener true
        }

        (menu.findItem(R.id.user_menu_ajustes)).setOnMenuItemClickListener {
            val intent = Intent(this, UserSettingsActivity::class.java)
            intent.putExtra("MONEDAS", ArrayList(lista_monedas))
            startActivity(intent)
            return@setOnMenuItemClickListener true
        }

        (menu.findItem(R.id.user_menu_sesion)).setOnMenuItemClickListener {
            controlSP.id = ""
            controlSP.tipo = 1
            controlSP.moneda_sel = 0
            controlSP.tema = false
            Intent(this, MainActivity::class.java).also { intent -> startActivity(intent) }
            finish()
            return@setOnMenuItemClickListener true
        }

        return true
    }

    private fun buscarUsuario() {
        ControlDB.rutaUsuario.child(controlSP.id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuario = snapshot.getValue(Usuario::class.java) ?: Usuario()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun buscarCartas(): MutableList<Carta> {
        val lista_cartas = mutableListOf<Carta>()
        ControlDB.rutacartas.addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java) ?: Carta()
                if (carta.disponible == true) {
                    lista_cartas.add(carta)
                }
                adap_carta.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java) ?: Carta()
                if (carta.disponible == true) {
                    lista_cartas.add(carta)
                } else if (carta.disponible == false) {
                    val cr = lista_cartas.filter { it.id == carta.id }.get(0)
                    lista_cartas.removeAt(lista_cartas.indexOf(cr))
                }
                adap_carta.notifyDataSetChanged()
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
        return lista_cartas
    }

    private fun buscarPedidos(): MutableList<Pedido> {
        val lista = mutableListOf<Pedido>()
        ControlDB.rutaResCartas.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                GlobalScope.launch(Dispatchers.IO) {
                    val sem = CountDownLatch(1)
                    val pedido = snapshot.getValue(Pedido::class.java) ?: Pedido()
                    if (pedido.idCliente == controlSP.id) {
                        ControlDB.rutacartas.child(pedido.idCarta ?: "")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val carta = snapshot.getValue(Carta::class.java) ?: Carta()
                                    pedido.imgCarta = carta.imagen
                                    pedido.nombreCarta = carta.nombre
                                    pedido.categoria = carta.categoria
                                    sem.countDown()
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        sem.await()
                        if (pedido.estadoNotificacion == EstadoNotificaciones.CREADO_CLIENTE
                            && pedido.idCliente == controlSP.id
                        ) {
                            ControlNotif.generarNotificacion(
                                this@UserActivity,
                                generador.incrementAndGet(),
                                getString(R.string.texto_noti_usuario),
                                getString(R.string.titulo_noti_usuario),
                                UserOrdersFragment::class.java
                            )
                            ControlDB.rutaResCartas.child(pedido.id ?: "")
                                .child("estadoNotificacion")
                                .setValue(EstadoNotificaciones.NOTIFICADO_CLIENTE)
                        }
                        lista.add(pedido)
                    }
                }
                adap_pedidos.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val pedido = snapshot.getValue(Pedido::class.java) ?: Pedido()
                val index = lista_pedidos.map { it.id }.indexOf(pedido.id)
                lista_pedidos[index].estado = pedido.estado
                if (pedido.idCliente == controlSP.id
                    && pedido.estadoNotificacion == EstadoNotificaciones.CREADO_CLIENTE
                ) {
                    ControlNotif.generarNotificacion(
                        this@UserActivity,
                        generador.incrementAndGet(),
                        getString(R.string.texto_noti_usuario),
                        "Compra aceptada",
                        UserProfileFragment::class.java
                    )
                    ControlDB.rutaResCartas.child(pedido.id ?: "")
                        .child("estadoNotificacion")
                        .setValue(EstadoNotificaciones.NOTIFICADO_CLIENTE)
                }
                adap_pedidos.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
        return lista
    }

    private fun buscarEventos(): MutableList<Evento> {
        val lista_evento = mutableListOf<Evento>()
        ControlDB.rutaEvento.addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java) ?: Evento()
                lista_evento.add(evento)
                adap_evento.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val evento = snapshot.getValue(Evento::class.java) ?: Evento()
                val index = lista_evento.map { it.id }.indexOf(evento.id)
                lista_eventos[index] = evento
                adap_evento.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {/*como no vamos a borrar nada no lo usamos*/
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                previousChildName: String?
            ) {/*como no vamos a cambiar la posicion nada no lo usamos*/
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        return lista_evento
    }

    fun bottomNavNavigation(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.userHomeFragment -> {
                navView
                navController.navigate(R.id.userHomeFragment)
                true
            }
            R.id.userEventFragment -> {
                navController.navigate(R.id.userEventFragment)
                true
            }
            R.id.userOrdersFragment -> {
                navController.navigate(R.id.userOrdersFragment)
                true
            }
            R.id.userProfileFragment -> {
                navController.navigate(R.id.userProfileFragment)
                true
            }
            else -> true
        }
    }
}