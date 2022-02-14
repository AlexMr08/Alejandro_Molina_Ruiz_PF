package com.example.practica_final.admin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.di_tema7formularios.DatePickerFragment
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.elementos.Evento
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminAddEventBinding
import com.example.practica_final.elementos.Carta
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.concurrent.CountDownLatch

class AdminAddEventFragment : Fragment() {

    private var _binding: FragmentAdminAddEventBinding? = null
    lateinit var menu: Menu
    val formatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    var date = LocalDate.now()
    val ma by lazy {
        activity as AdminActivity
    }
    private var urlPortadaLocal: Uri? = null

    val obtenerUrl = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        when (uri) {
            null -> println("No hay imagen")
            else -> {
                urlPortadaLocal = uri
                Glide.with(ma).load(urlPortadaLocal).into(binding.faaeImg)

            }
        }
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminAddEventBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.faaeImg.setOnClickListener {
            obtenerUrl.launch("image/*")
        }
        binding.faaeFecha.setOnClickListener {
            abrirDatePicker()
        }
    }


    override fun onResume() {
        super.onResume()
        ma.FAB_manager(3, this::subirEvento)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun subirEvento(v: View) {
        if (urlPortadaLocal == null) {
            Toast.makeText(ma, "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        } else {
            if (isValid()) {
                GlobalScope.launch(Dispatchers.IO) {
                    if (existeEvento(
                            binding.faaeNom.text.toString(),
                            binding.faaeFecha.text.toString()
                        )
                    ) {
                        ma.runOnUiThread {
                            binding.faaeTilFec.error = "El evento ya existe en la fecha dada"
                        }
                    } else {
                        nuevoEvento()
                        ma.runOnUiThread {
                            ma.navController.navigate(R.id.adminEventFragment)
                        }
                    }
                }
            }
        }
    }

    private suspend fun subirImagenEvento(id: String, imagen: Uri): String {
        val urlPortadaFirebase: Uri? =
            ControlDB.stoRutaEvento.child(id).putFile(imagen).await().storage.downloadUrl.await()
        return urlPortadaFirebase.toString()
    }

    suspend fun nuevoEvento() {
        val nom = binding.faaeNom.text.toString()
        var pre = binding.faaePrecio.text.toString().toFloat()
        var afo = binding.faaeAforo.text.toString().toInt()
        var fecha = binding.faaeFecha.text.toString()
        val id = ControlDB.rutaEvento.push().key
        val img = subirImagenEvento(id!!, urlPortadaLocal!!)
        val evento = Evento(id, nom, fecha, img, pre, afo)
        ControlDB.rutaEvento.child(id).setValue(evento)
    }

    fun isValid(): Boolean {
        var validated = true
        val checkers = listOf(
            Pair(binding.faaeNom, this::validName),
            Pair(binding.faaeFecha, this::validFec),
            Pair(binding.faaeAforo, this::validAfo),
            Pair(binding.faaePrecio, this::validPre)
            //Pair(binding.textview, this::funcion)
        )
        for (c in checkers) {
            val x = c.first
            val f = c.second
            val y = f(x)
            validated = y
            if (!validated) break
        }
        return validated
    }

    fun validName(e: EditText): Boolean {
        var valid = true
        if (e.text.toString().trim().length <= 2) {
            e.error = "El nombre del evento debe tener al menos 3 caracteres"
            valid = false
        }
        return valid
    }

    fun validPre(e: EditText): Boolean {
        var valid = true
        val num = e.text.toString().trim().toFloatOrNull()
        if (num == null || num <= 0) {
            e.error = "Introduce un precio valido"
            valid = false
        }
        return valid
    }

    fun validFec(e: EditText): Boolean {
        var valid = true
        if (e.text.toString() == "") {
            e.error = "Introduce una fecha valida"
            valid = false
        }
        return valid
    }

    fun validAfo(e: EditText): Boolean {
        var valid = true
        val num = e.text.toString().trim().toIntOrNull()
        if (num == null || num <= 0) {
            e.error = "Introduce un aforo valido"
            valid = false
        }
        return valid
    }

    fun abrirDatePicker() {
        val newFragment = DatePickerFragment { day: Int, month: Int, year: Int ->
            fechaSeleccionada(day, month, year)
        }
        newFragment.show(ma.supportFragmentManager, "datePicker")
    }

    fun fechaSeleccionada(day: Int, month: Int, year: Int) {
        val birthDate = "$day/${month + 1}/$year"
        binding.faaeFecha.setText(birthDate)
        date = LocalDate.parse(birthDate, formatter)
    }

    fun existeEvento(nombre: String, fecha: String): Boolean {
        var res: Boolean? = false
        val sem = CountDownLatch(1)
        ControlDB.rutaEvento.orderByChild("nombre").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.children.filter {
                        it.getValue(Evento::class.java)?.nombre == nombre && it.getValue(Evento::class.java)?.fecha == fecha
                    }.isNotEmpty()) {
                    res = true
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