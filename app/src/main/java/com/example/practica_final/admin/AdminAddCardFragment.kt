package com.example.practica_final.admin

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.example.practica_final.Carta
import com.example.practica_final.ControlDB
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminAddCardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminAddCardFragment : Fragment() {

    private var _binding: FragmentAdminAddCardBinding? = null
    lateinit var menu: Menu
    private var pos_spi = 0
    val ma by lazy {
        activity as AdminActivity
    }

    val obtenerUrl = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        when (uri) {
            null -> println("No hay imagen")
            else -> {
                urlPortadaLocal = uri
                Glide.with(ma).load(urlPortadaLocal).into(binding.fancImgCarta)
                flipCard(ma, binding.fancImgCarta, binding.fancReverso)
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
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAdminAddCardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        binding.fancReverso.setOnClickListener {
            obtenerUrl.launch("image/*")
        }
        binding.fancImgCarta.setOnClickListener {
            flipCard(ma, binding.fancReverso, binding.fancImgCarta)
            urlPortadaLocal = null
        }

        actualizarAdapter(ma.categorias)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pos_spi = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //hay que ponerlo pero no lo hemos usado :(
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        //esto indica que se muestra en la appbar
        //this.menu.findItem(R.id.app_bar_search).setVisible(true)
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        //esto indica que se quita de la appbar
        //menu.findItem(R.id.app_bar_search).setVisible(false)
    }

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(3, this::subirCarta)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun flipCard(ma: AdminActivity, visibleView: View, inVisibleView: View) {
        try {
            visibleView.visibility = View.VISIBLE
            val flipOutAnimatorSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_out
                ) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimationSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.flip_in
                ) as AnimatorSet
            flipInAnimationSet.setTarget(visibleView)
            flipOutAnimatorSet.start()
            flipInAnimationSet.start()
            flipInAnimationSet.doOnEnd {
                inVisibleView.visibility = View.INVISIBLE
            }
        } catch (e: Exception) {
        }
    }

    fun subirCarta(v: View) {

        GlobalScope.launch(Dispatchers.IO) {

            if (urlPortadaLocal != null) {
                nuevaCarta()
                ma.runOnUiThread {
                    ma.navController.navigate(R.id.adminHomeFragment)
                }
            }
        }
    }

    private suspend fun subirImagenCarta(id: String, imagen: Uri): String {
        val urlPortadaFirebase: Uri? =
            ControlDB.stoRutaCartas.child(id).putFile(imagen).await().storage.downloadUrl.await()
        return urlPortadaFirebase.toString()
    }

    suspend fun nuevaCarta() {
        val nom = binding.fancNom.text.toString()
        var pre = binding.fancPre.text.toString().toFloatOrNull()
        if (pre == null) {
            pre = 0.0f
        }
        val cat = ma.categorias[pos_spi]
        val dis = binding.fancDis.isChecked
        val id = ControlDB.rutacartas.push().key
        val img = subirImagenCarta(id!!, urlPortadaLocal!!)
        val carta = Carta(id, nom, cat, img, pre, dis)
        ControlDB.rutacartas.child(id ?: "").setValue(carta)
    }

    fun actualizarAdapter(lista: List<String>){
        val spi_adap = ArrayAdapter(ma, android.R.layout.simple_spinner_item, lista)
        spi_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter=spi_adap
    }

}