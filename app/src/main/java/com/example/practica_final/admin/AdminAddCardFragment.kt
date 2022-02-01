package com.example.practica_final.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.example.practica_final.Carta
import com.example.practica_final.ControlDB
import com.example.practica_final.R
import com.example.practica_final.databinding.FragmentAdminAddCardBinding

class AdminAddCardFragment : Fragment() {

    private var _binding: FragmentAdminAddCardBinding? = null
    lateinit var menu: Menu
    val ma by lazy {
        activity as AdminActivity
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        _binding = FragmentAdminAddCardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()

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
        ma.FAB_manager(3, this::nuevaCarta)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun nuevaCarta(v:View){
        val nom = binding.fancNom.text.toString()
        var pre = binding.fancPre.text.toString().toFloatOrNull()
        if (pre==null){
            pre=0.0f
        }
        val cat = "blanca"
        val dis = binding.fancDis.isChecked
        val img = "hola manolo"
        val id = ControlDB.rutacartas.push().key
        val carta = Carta(id,nom,cat,img,pre,dis)
        ControlDB.rutacartas.child(id?:"").setValue(carta)
    }

}