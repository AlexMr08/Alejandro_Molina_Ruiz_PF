package com.example.practica_final.admin

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.animation.doOnEnd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.practica_final.elementos.Carta
import com.example.practica_final.aleLib.ControlDB
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminAddCardBinding
import com.example.practica_final.elementos.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CountDownLatch

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
                Glide.with(ma).load(urlPortadaLocal).transform(CenterCrop(),RoundedCorners(20)).into(binding.fancImgCarta)
                flipCard(binding.fancImgCarta, binding.fancReverso)
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
        Glide.with(ma).load(R.drawable.magic_card_back).transform(RoundedCorners(20)).into(binding.fancReverso)
        binding.fancReverso.setOnClickListener {
            obtenerUrl.launch("image/*")
        }
        binding.fancImgCarta.setOnClickListener {
            flipCard(binding.fancReverso, binding.fancImgCarta)
            urlPortadaLocal = null
        }

        actualizarAdapter(Carta.categorias)

        binding.fancSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    override fun onResume() {
        super.onResume()
        ma.FAB_manager(3, this::subirCarta)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun flipCard( visibleView: View, inVisibleView: View) {
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
        if (urlPortadaLocal == null ) {
            Toast.makeText(ma, "Selecciona una imagen", Toast.LENGTH_SHORT).show()
        }else{
            if (isValid()){
                GlobalScope.launch(Dispatchers.IO) {
                if (existeCarta(binding.fancNom.text.toString())){
                    ma.runOnUiThread {
                        binding.tilNombre.error = "El nombre de la carta ya existe"

                    }
                }else{

                        nuevaCarta()
                        ma.runOnUiThread {
                            ma.navController.navigate(R.id.adminHomeFragment)
                        }
                    }
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
        val nom = binding.fancNom.text.toString().trim()
        var pre = binding.fancPre.text.toString().toFloat()
        val cat = Carta.categorias[pos_spi]
        val dis = binding.fancDis.isChecked
        val id = ControlDB.rutacartas.push().key
        val img = subirImagenCarta(id!!, urlPortadaLocal!!)
        val carta = Carta(id, nom, cat, img, pre, dis)
        ControlDB.rutacartas.child(id).setValue(carta)
    }

    fun actualizarAdapter(lista: List<String>){
        val spi_adap = ArrayAdapter(ma, android.R.layout.simple_spinner_item, lista)
        spi_adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.fancSpinner.adapter=spi_adap
    }

        fun isValid():Boolean{
            	var validated = true
            	val checkers = listOf(
            		Pair(binding.fancNom, this::validName),
                    Pair(binding.fancPre, this::validNum)
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

    fun validName(e: EditText):Boolean{
        var valid = true
        if (e.text.toString().trim().length<=2){
            e.error = "El nombre de la carta debe tener al menos 3 caracteres"
            valid = false
        }
        return valid
    }

    fun validNum(e: EditText):Boolean{
        var valid = true
        val num = e.text.toString().trim().toFloatOrNull()
        if (num==null || num<=0){
            e.error = "Introduce un precio valido"
            valid = false
        }
        return valid
    }

    fun existeCarta(nombre: String):Boolean{
        var res:Boolean?= false
        val sem = CountDownLatch(1)
        ControlDB.rutacartas.orderByChild("nombre").equalTo(nombre).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.getValue(Carta::class.java)!=null){
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