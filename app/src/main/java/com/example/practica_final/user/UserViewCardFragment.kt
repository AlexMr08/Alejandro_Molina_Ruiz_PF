package com.example.practica_final.user

import android.os.Bundle
import android.service.controls.Control
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.example.practica_final.ControlDB
import com.example.practica_final.ControlSP
import com.example.practica_final.Pedido
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentUserViewCardBinding
import com.example.practica_final.user.UserActivity
import java.text.SimpleDateFormat
import java.util.*

class UserViewCardFragment : Fragment() {

    private var _binding: FragmentUserViewCardBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as UserActivity
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentUserViewCardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        val elem = ma.carta_sel
        binding.textView2.text = elem.nombre
        binding.textView4.text = elem.categoria
        binding.textView3.text = ma.getString(R.string.carta_precio,elem.precio)
        Glide.with(ma).load(elem.imagen).placeholder(R.drawable.magic_card_back).into(binding.imageView3)
        binding.button3.setOnClickListener {
            val fecha = Calendar.getInstance()
            val formateador = SimpleDateFormat("yyyy-MM-dd")
            val hoy = formateador.format(fecha.time)
            val id = ControlDB.rutaResCartas.push().key
            ControlDB.rutaResCartas.child(id?:"").setValue(Pedido(id?:"",ma.controlSP.id,elem.id?:"",hoy,0,elem.nombre,elem.imagen))
            ma.navController.navigate(R.id.userHomeFragment)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        this.menu = menu
        //esto indica que se muestra en la appbar
        this.menu.findItem(R.id.app_bar_search).setVisible(false)
    }


    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        //esto indica que se quita de la appbar
        menu.findItem(R.id.app_bar_search).setVisible(false)
    }



    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}