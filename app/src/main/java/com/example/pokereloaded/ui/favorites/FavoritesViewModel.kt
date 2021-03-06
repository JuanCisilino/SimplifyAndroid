package com.example.pokereloaded.ui.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo
import com.example.pokereloaded.network.RetroInstance
import rx.schedulers.Schedulers

class FavoritesViewModel : ViewModel() {

    var pokemonList = MutableLiveData<List<Pokemon>?>()
    private val instance = RetroInstance.getRetrofitInstance().create(PokeRepo::class.java)

    fun fetchList() {
        instance.getFavorites()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {pokemonList.postValue(it)},
                {pokemonList.postValue(null)})
    }
}