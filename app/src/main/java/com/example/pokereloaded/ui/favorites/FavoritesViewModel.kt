package com.example.pokereloaded.ui.favorites

import androidx.lifecycle.LiveData
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
        instance.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {pokemonList.postValue(it.pokemonList.filter { it.favorite == true })},
                {pokemonList.postValue(null)})
    }
}