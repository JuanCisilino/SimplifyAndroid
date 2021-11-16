package com.example.pokereloaded.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon(
    var id: Long?,
    var name: String?,
    var nickName: String?,
    var baseUrl: String?,
    var listimg: String?,
    var detimg: String?,
    var strongAgainst: String?,
    var weakAgainst: String?,
    var noDamageTo: String?,
    var noDamageFrom: String?,
    var type: String?,
    var evolvesTo: String?,
    var evolvesFrom: String?,
    var flavor: String?,
    var favorite: Boolean?,
    var caracteristicas: List<String>?,
    var tipos: List<String>?,
    var fuerteContra: List<String>?,
    var debilContra: List<String>?,
    var noDanioA: List<String>?,
    var noDanioDe: List<String>?
): Parcelable{

    fun split() {
        caracteristicas = this.flavor?.split(";")
        tipos = this.type?.split(";")
        fuerteContra = this.strongAgainst?.split(";")?.toSet()?.toList()
        debilContra = this.weakAgainst?.split(";")?.toSet()?.toList()
        noDanioA = this.noDamageTo?.split(";")?.toSet()?.toList()
        noDanioDe = this.noDamageFrom?.split(";")?.toSet()?.toList()
    }
}
data class PokemonList(
    @SerializedName("items")
    val pokemonList: List<Pokemon>)
