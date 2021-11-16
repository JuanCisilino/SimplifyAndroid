package com.example.pokereloaded.ui.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokereloaded.R
import com.example.pokereloaded.models.Pokemon
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
            view.pokemonNameTextView.text = pokemon.name

            Glide.with(view.pokemonImageView)
                .load(pokemon.listimg)
                .circleCrop()
                .into(view.pokemonImageView)

            itemView.setOnClickListener { onPokemonClickCallback?.invoke(pokemon) }
        }
    }

    fun setList(list: List<Pokemon>){
        pokemonList = list as ArrayList<Pokemon>
        this.notifyDataSetChanged()
    }
}