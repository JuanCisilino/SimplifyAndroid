package com.example.pokereloaded.network

import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.models.PokemonList
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface PokeRepo {

    @GET("pokemon/")
    fun getList(@Query("page") page: Int, @Query("limit") size: Int): PokemonList

    @GET("pokemon/?pageNo=1&pageSize=1500")
    fun getList(): Observable<PokemonList>

    @GET("pokemon/{name}")
    fun getPokemonById(@Path ("name") name: String): Observable<Pokemon>

    @GET("pokemon/favorites")
    fun getFavorites(): Observable<List<Pokemon>>

    @PATCH("pokemon/{name}")
    fun favorite(@Path ("name") name: String): Observable<Pokemon>
}