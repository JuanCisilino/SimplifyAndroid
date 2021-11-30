package com.example.pokereloaded.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pokereloaded.R
import com.example.pokereloaded.models.Pokemon

class PaginatedAdapter: PagingDataAdapter<Pokemon, PaginatedAdapter.PokeHolder>(DiffUtilCallback()) {

    var onPokemonClickCallback : ((pokemon: Pokemon) -> Unit)? = null

    override fun onBindViewHolder(holder: PokeHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokeHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokeHolder(inflater)
    }

    class DiffUtilCallback: DiffUtil.ItemCallback<Pokemon>(){

        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon) : Boolean{
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon) : Boolean{
            return oldItem == newItem
        }

    }

    inner class PokeHolder(view: View): RecyclerView.ViewHolder(view){

        private val name: TextView = view.findViewById(R.id.pokemonNameTextView)
        private val image: ImageView = view.findViewById(R.id.pokemonImageView)
        private val favorite: ImageView = view.findViewById(R.id.pokemonfavoriteImageView)

        fun bind(pokemon: Pokemon) {
            pokemon.split()
            name.text = pokemon.name
            pokemon.listimg?.let { glideImage(image, it) }
            favorite.visibility =
                if (pokemon.favorite == true) View.VISIBLE else View.GONE
            itemView.setOnClickListener { onPokemonClickCallback?.invoke(pokemon) }
        }

        private fun glideImage(imageView: ImageView, url: String){
            Glide.with(imageView)
                .load(url)
                .circleCrop()
                .into(imageView)
        }
    }
}