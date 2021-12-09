package com.example.apiproject_pokedex.Retrofit


import com.example.apiproject_pokedex.Model.Pokedex
import retrofit2.http.GET
import io.reactivex.Observable

interface IPokemonList {
    @get:GET("pokedex.json")
    val listPokemon: Observable<Pokedex>
}