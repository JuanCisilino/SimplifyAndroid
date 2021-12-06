package com.example.pokereloaded.network

import com.example.pokereloaded.models.NickNameRequest
import com.example.pokereloaded.models.Pokemon
import com.example.pokereloaded.models.PokemonList
import retrofit2.http.*
import rx.Observable

interface PokeRepo {

    @GET("pokemon/")
    suspend fun getList(@Query("pageNo") page: Int, @Query("pageSize") size: Int = 50): PokemonList

    @POST("pokemon/")
    fun getList(): Observable<List<Pokemon>>

    @GET("pokemon/{name}")
    fun getPokemonById(@Path ("name") name: String): Observable<Pokemon>

    @GET("pokemon/favorites")
    fun getFavorites(): Observable<List<Pokemon>>

    @PATCH("pokemon/{name}")
    fun favorite(@Path ("name") name: String): Observable<Pokemon>

    @PATCH("pokemon/")
    fun setNickname(@Body nickNameRequest: NickNameRequest): Observable<Pokemon>
}