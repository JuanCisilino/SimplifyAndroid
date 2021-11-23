package com.example.pokereloaded.ui.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.network.PokeRepo

class PokemonPagingSource(val repo: PokeRepo): PagingSource<Int, Pokemon>() {

    companion object{
        private const val FIRST_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>) : Int?{
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            var nextPage = params.key ?: FIRST_PAGE_INDEX
            val response = repo.getList(nextPage)
            nextPage++
            return when (nextPage) {
                25 -> LoadResult.Error(Throwable("no more pages"))
                else -> {
                    LoadResult.Page(
                        data = response.pokemonList,
                        prevKey = null,
                        nextKey = nextPage)
                }
            }
        } catch (e: Exception){
            LoadResult.Error(e)
        }
    }
}