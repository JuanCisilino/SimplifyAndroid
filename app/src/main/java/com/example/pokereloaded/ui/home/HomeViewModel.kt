package com.example.pokereloaded.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo
import com.example.pokereloaded.network.RetroInstance
import com.example.pokereloaded.ui.adapters.PokemonPagingSource
import kotlinx.coroutines.flow.Flow
import rx.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    var pokemonList = MutableLiveData<List<Pokemon>?>()
    var list = ArrayList<Pokemon>()
    private val instance = RetroInstance.getRetrofitInstance().create(PokeRepo::class.java)

    fun fetchList() {
        if (list.isEmpty()){
            instance.getList()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        list = it as ArrayList<Pokemon>
                        pokemonList.postValue(it)
                    },
                    {pokemonList.postValue(null)})
        }
    }

    fun fetchPaginatedList(): Flow<PagingData<Pokemon>> {
        return Pager(config = PagingConfig(pageSize = 50, maxSize = 100, prefetchDistance = 25),
        pagingSourceFactory = { PokemonPagingSource(instance) }).flow.cachedIn(viewModelScope)
    }

}