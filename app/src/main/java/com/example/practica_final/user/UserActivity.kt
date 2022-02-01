package com.example.practica_final.user

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.practica_final.Carta
import com.example.practica_final.ControlDB
import com.example.practica_final.R
import com.example.practica_final.databinding.ActivityUserBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class UserActivity : AppCompatActivity() {
    lateinit var lista_cartas : MutableList<Carta>
    private lateinit var binding: ActivityUserBinding
    val navController by lazy { findNavController(R.id.nav_host_fragment_activity_user) }
    val adap_carta by lazy { UserCardAdapter(lista_cartas, this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.setBackgroundColor(Color.parseColor("#55ffffff"))

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.userHomeFragment, R.id.userEventFragment, R.id.userProfileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        lista_cartas = mutableListOf()
        ControlDB.rutacartas.addChildEventListener( object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val carta = snapshot.getValue(Carta::class.java)
                if (carta?.disponible == true){
                    lista_cartas.add(carta)
                }
                adap_carta.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {/*como no vamos a borrar nada no lo usamos*/}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {/*como no vamos a cambiar la posicion nada no lo usamos*/}

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.user, menu)
        return true
    }


}