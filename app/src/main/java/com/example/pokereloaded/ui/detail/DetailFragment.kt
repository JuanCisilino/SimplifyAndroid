package com.example.pokereloaded.ui.detail

import android.app.Dialog
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.pokereloaded.R
import com.example.pokereloaded.databinding.FragmentDetailBinding
import com.example.pokereloaded.extentions.hideKeyboard
import com.example.pokereloaded.models.Pokemon
import kotlinx.android.synthetic.main.item_caracteristic.view.*
import kotlinx.android.synthetic.main.item_pokemon.view.*
import kotlinx.android.synthetic.main.item_tipo.view.*

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var pokemon: Pokemon
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var dialog : Dialog
    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let { pokemon = it.getParcelable(getString(R.string.pokemon))!! }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?) : View {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer.create(context, R.raw.favorite)
        dialog = Dialog(requireActivity())
        setComponents()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.pokemonDeEvol.observe(viewLifecycleOwner, { loadDeEvolve(it) })
        viewModel.pokemonEnEvol.observe(viewLifecycleOwner, { loadEnEvolve(it) })
        viewModel.favoriteLiveData.observe(viewLifecycleOwner, {
            it?.let { parentFragmentManager.popBackStack() }
                ?:run { Toast.makeText(context, R.string.error_list, Toast.LENGTH_LONG).show() }
        })
    }

    private fun loadEnEvolve(evolved: Pokemon?) {
        evolved
            ?.let { setEvolveEnLayout(it) }
            ?:run { binding.evoEnLayout.visibility = View.GONE }
    }

    private fun setEvolveEnLayout(evolved: Pokemon) {
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_pokemon, null)
        binding.evoEnPokemonLayout.removeAllViews()
        evolved.split()
        view.pokemonNameTextView.setTextColor(resources.getColor(R.color.white))
        view.pokemonNameTextView.text = evolved.name
        Glide.with(view.pokemonImageView)
            .load(evolved.listimg)
            .circleCrop()
            .into(view.pokemonImageView)
        view.setOnClickListener { navigateToDetail(evolved) }
        binding.evoEnPokemonLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun getFavorite(): Int{
        return if (pokemon.favorite == true) R.drawable.ic_baseline_favorite_24
        else R.drawable.ic_baseline_favorite_border_24
    }

    private fun loadDeEvolve(toEvolve: Pokemon?) {
        toEvolve
            ?.let { setEvolveDeLayout(it) }
            ?:run { binding.evoDeLayout.visibility = View.GONE }
    }

    private fun setEvolveDeLayout(toEvolve: Pokemon) {
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_pokemon, null)
        binding.evoDePokemonLayout.removeAllViews()
        toEvolve.split()
        view.pokemonNameTextView.setTextColor(resources.getColor(R.color.white))
        view.pokemonNameTextView.text = toEvolve.name
        Glide.with(view.pokemonImageView)
            .load(toEvolve.listimg)
            .circleCrop()
            .into(view.pokemonImageView)
        view.setOnClickListener { navigateToDetail(toEvolve) }
        binding.evoDePokemonLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun navigateToDetail(pokemon: Pokemon) {
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.pokemon), pokemon)
        findNavController().navigate(R.id.navigation_detail, bundle)
    }

    private fun setComponents() {
        setBindingComponents()
        setBackgroundColor()
        pokemon.tipos?.forEach { setTypesLayout(it) }
        setStrongLayout()
        setWeakLayout()
        randomizeCaract()
        setEvolution()
    }

    private fun setBindingComponents(){
        binding.nameTextView.text = pokemon.name
        if (!pokemon.nickName.isNullOrBlank()) setNickName()
        binding.favoriteImageView.setImageResource(getFavorite())
        binding.favoriteImageView.setOnClickListener { setFavorite() }
        Glide.with(requireContext()).load(pokemon.detimg).into(binding.imagenImageView)
    }

    private fun setNickName(){
        binding.nickNameTextView.visibility = View.VISIBLE
        binding.nickNameTextView.text = "(${pokemon.nickName})"
    }

    private fun setFavorite(){
        if (pokemon.favorite == false) setNickNameDialog()
        else setFavoriteFalse()
    }

    private fun setFavoriteFalse() {
        pokemon.name?.let { viewModel.setNickName(it, "") }
        viewModel.favorite(pokemon.name)
    }

    private fun setNickNameDialog() {
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_nickname)
        setButtons(dialog.findViewById(R.id.btnok) as Button, dialog.findViewById(R.id.btncn) as Button,
            dialog.findViewById(R.id.et3) as EditText)
        dialog.show()
    }

    private fun editorNameActionListener(actionId: Int, nickname: Editable): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            setPossitve(nickname)
            dialog.dismiss()
            return true
        }
        return false
    }

    private fun setButtons(btnok: Button, btncn: Button, et3: EditText) {
        et3.setOnEditorActionListener { _, actionId, _ -> editorNameActionListener(actionId, et3.text)  }
        btnok.setOnClickListener {
            setPossitve(et3.text)
            dialog.dismiss() }
        btncn.setOnClickListener { dialog.dismiss() }
    }

    private fun setPossitve(et3: Editable) {
        pokemon.name?.let { viewModel.setNickName(it, et3.toString()) }
        mediaPlayer.start()
        viewModel.favorite(pokemon.name)
    }

    private fun setWeakLayout() {
        pokemon.debilContra?.let {
            if (it[0].isBlank())  binding.debilMasterLayout.visibility = View.GONE
            else it.forEach { tipo -> setWeakAgainst(tipo) }
        }
    }

    private fun setStrongLayout() {
        pokemon.fuerteContra?.let {
            if (it[0].isBlank())  binding.fuerteMasterLayout.visibility = View.GONE
            else it.forEach { tipo -> setStrongAgainst(tipo) }
        }
    }

    private fun setEvolution() {
        pokemon.evolvesFrom?.let { if (it.isNotEmpty()) showEvolutionFrom(it) }
        pokemon.evolvesTo?.let { if (it.isNotEmpty()) showEvolutionTo(it) }
    }

    private fun showEvolutionTo(pokemonName: String) {
        viewModel.searchForEvolved(pokemonName)
        binding.evoEnLayout.visibility = View.VISIBLE
    }

    private fun showEvolutionFrom(pokemonName: String) {
        viewModel.searchForToEvolve(pokemonName)
        binding.evoDeLayout.visibility = View.VISIBLE
    }

    private fun setBackgroundColor() {
        binding.detLayout.setBackgroundColor(
        when {
            pokemon.tipos?.contains(getString(R.string.fire)) == true -> resources.getColor(R.color.fuego)
            pokemon.tipos?.contains(getString(R.string.water)) == true -> resources.getColor(R.color.agua)
            pokemon.tipos?.contains(getString(R.string.grass)) == true -> resources.getColor(R.color.tierra)
            pokemon.tipos?.contains(getString(R.string.electric)) == true -> resources.getColor(R.color.viento)
            pokemon.tipos?.contains(getString(R.string.fairy)) == true -> resources.getColor(R.color.hada)
            else -> resources.getColor(R.color.rest)
        })
    }

    private fun setWeakAgainst(tipo: String) {
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_tipo, null)
        getIcon(tipo)?.let {
            view.tipoImageView.setImageResource(it)
            binding.debilLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setStrongAgainst(tipo: String) {
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_tipo, null)
        getIcon(tipo)?.let {
            view.tipoImageView.setImageResource(it)
            binding.fuerteLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun randomizeCaract() {
        val randomCaract = pokemon.caracteristicas?.random()
        randomCaract?.let { setCaracteristicsLayout(it) }
    }

    private fun setCaracteristicsLayout(caracteristica: String) {
        binding.caracteristicasLayout.removeAllViews()
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_caracteristic, null)
        view.caractTextView.text = caracteristica
        binding.caracteristicasLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun setTypesLayout(tipo: String) {
        val inflater = context?.applicationContext?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_tipo, null)
        getIcon(tipo)?.let {
            view.tipoImageView.setImageResource(it)
            binding.tipoLayout.addView(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun getIcon(tipe: String): Int?{
        return when (tipe){
            getString(R.string.fire) -> R.drawable.fuego
            getString(R.string.water) -> R.drawable.agua
            getString(R.string.steel) -> R.drawable.acero
            getString(R.string.grass) -> R.drawable.planta
            getString(R.string.dragon) -> R.drawable.dragon
            getString(R.string.fighting) -> R.drawable.peleador
            getString(R.string.normal) -> R.drawable.normal
            getString(R.string.poison) -> R.drawable.veneno
            getString(R.string.electric) -> R.drawable.electrico
            getString(R.string.fairy) -> R.drawable.hada
            getString(R.string.ghost) -> R.drawable.fantasma
            getString(R.string.dark) -> R.drawable.oscuro
            getString(R.string.flying) -> R.drawable.volador
            getString(R.string.rock) -> R.drawable.roca
            getString(R.string.ice) -> R.drawable.hielo
            getString(R.string.bug) -> R.drawable.bicho
            getString(R.string.psychic) -> R.drawable.psiquico
            getString(R.string.ground) -> R.drawable.tierra
            else -> null
        }
    }
}