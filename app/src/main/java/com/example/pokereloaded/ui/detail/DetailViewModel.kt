package com.example.pokereloaded.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokereloaded.models.NickNameRequest
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo
import com.example.pokereloaded.network.RetroInstance
import rx.schedulers.Schedulers

class DetailViewModel: ViewModel() {

    var pokemonEnEvol = MutableLiveData<Pokemon?>()
    var pokemonDeEvol = MutableLiveData<Pokemon?>()
    var favoriteLiveData = MutableLiveData<Unit?>()
    private val instance = RetroInstance.getRetrofitInstance().create(PokeRepo::class.java)

    fun searchForEvolved(name: String){
        instance.getPokemonById(name)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {pokemonEnEvol.postValue(it)},
                {pokemonEnEvol.postValue(null)})
    }

    fun searchForToEvolve(name: String){
        instance.getPokemonById(name)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {pokemonDeEvol.postValue(it)},
                {pokemonDeEvol.postValue(null)})
    }

    fun favorite(name: String?){
        name?.let{
            instance.favorite(name)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {favoriteLiveData.postValue(Unit)},
                    {favoriteLiveData.postValue(null)})
        }
    }

    fun setNickName(name: String, nickname: String){
        instance.setNickname(NickNameRequest(name, nickname))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                {},
                {}
            )
    }
}