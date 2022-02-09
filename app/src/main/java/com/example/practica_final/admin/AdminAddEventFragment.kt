package com.example.practica_final.admin

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.elementos.Evento
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminAddEventBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminAddEventFragment : Fragment() {

    private var _binding: FragmentAdminAddEventBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as AdminActivity
    }
    val obtenerUrl = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        when (uri) {
            null -> println("No hay imagen")
            else -> {
                urlPortadaLocal = uri
                Glide.with(ma).load(urlPortadaLocal).into(binding.faaeImg)

            }
        }
    }
    private var urlPortadaLocal: Uri? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

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
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        //esto indica que se muestra en la appbar
//        this.menu.findItem(R.id.app_bar_search).setVisible(true)
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        //esto indica que se quita de la appbar
//        menu.findItem(R.id.app_bar_search).setVisible(false)
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
        if (urlPortadaLocal==null){
            Toast.makeText(ma, "via subi un evento", Toast.LENGTH_SHORT).show()

        }
        GlobalScope.launch(Dispatchers.IO) {

            if (urlPortadaLocal != null) {
                nuevoEvento()
                ma.runOnUiThread {
                    ma.navController.navigate(R.id.adminEventFragment)
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
        var pre = binding.faaePrecio.text.toString().toFloatOrNull()
        var afo = binding.faaeAforo.text.toString().toIntOrNull()
        var fecha = binding.faaeFecha.text.toString()
        if (pre == null) {
            pre = 0.0f
        }
        if (afo == null){
            afo = 0
        }
        val id = ControlDB.rutaEvento.push().key
        val img = subirImagenEvento(id!!, urlPortadaLocal!!)
        val evento = Evento(id, nom,fecha,img,pre,afo)
        ControlDB.rutaEvento.child(id ?: "").setValue(evento)
    }

}