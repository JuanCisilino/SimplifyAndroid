package com.example.pokereloaded.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokereloaded.R
import com.example.pokereloaded.models.Pokemon
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_pokemon.view.*
import java.util.ArrayList

class PokemonAdapter: RecyclerView.Adapter<PokemonAdapter.MyViewHolder>() {

    private var pokemonList = ArrayList<Pokemon>()
    var onPokemonClickCallback : ((pokemon: Pokemon) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return MyViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount():Int = pokemonList.size

    inner class MyViewHolder(private val view: View): RecyclerView.ViewHolder(view){

        fun bind(pokemon: Pokemon) {
            pokemon.split()
            if (!pokemon.nickName.isNullOrBlank()) setNickName(pokemon) else view.pokemonNickNameTextView.text = ""
            view.pokemonNameTextView.text = pokemon.name
            pokemon.listimg?.let { glideImage(view.pokemonImageView, it) }
            view.pokemonfavoriteImageView.visibility =
                if (pokemon.favorite == true) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onPokemonClickCallback?.invoke(pokemon) }
        }

        private fun setNickName(pokemon: Pokemon){
            view.pokemonNickNameTextView.visibility = View.VISIBLE
            view.pokemonNickNameTextView.text = "(${pokemon.nickName})"
        }

        private fun glideImage(imageView: ImageView, url: String){
            Glide.with(imageView)
                .load(url)
                .circleCrop()
                .into(imageView)
        }
    }

    fun setList(list: List<Pokemon>){
        pokemonList = list as ArrayList<Pokemon>
        this.notifyDataSetChanged()
    }
}