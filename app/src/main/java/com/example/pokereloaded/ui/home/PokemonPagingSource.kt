package com.example.pokereloaded.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo
import rx.schedulers.Schedulers
import java.lang.Exception

class PokemonPagingSource(val repo: PokeRepo): PagingSource<Int, Pokemon>() {

    companion object{
        private const val FIRST_PAGE_INDEX = 1
        private var size = 1
    }

    private fun setSize(){
        repo.getList()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { size = it.pokemonList.size },
                { size = 1 })
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>) : Int?{
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        if (size == 1 ) setSize()
        return try {
            var nextPage = params.key ?: FIRST_PAGE_INDEX
            val loadedSoFar = nextPage * 50
            val response = repo.getList(nextPage)
            nextPage++
            return when {
                nextPage == 0 -> LoadResult.Error(Throwable("no more pages"))
                loadedSoFar.compareTo(size) == 1 ->
                    LoadResult.Page(
                        data = response.pokemonList,
                        prevKey = null,
                        nextKey = -1)
                else ->
                    LoadResult.Page(
                        data = response.pokemonList,
                        prevKey = null,
                        nextKey = nextPage)
            }
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}