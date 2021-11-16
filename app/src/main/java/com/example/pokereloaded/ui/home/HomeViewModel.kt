package com.example.pokereloaded.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo
import com.example.pokereloaded.network.RetroInstance
import rx.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    var pokemonList = MutableLiveData<List<Pokemon>?>()
    lateinit var list : ArrayList<Pokemon>
    private val instance = RetroInstance.getRetrofitInstance().create(PokeRepo::class.java)

    fun fetchList() {
        instance.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    list = it.pokemonList as ArrayList<Pokemon>
                    pokemonList.postValue(it.pokemonList)
                },
                {pokemonList.postValue(null)})
    }

}