package com.example.apiproject_pokedex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiproject_pokedex.Adapter.PokemonEvolutionAdapter
import com.example.apiproject_pokedex.Adapter.PokemonTypeAdapter
import com.example.apiproject_pokedex.Common.Common
import com.example.apiproject_pokedex.Model.Evolution
import com.example.apiproject_pokedex.Model.Pokemon

class PokemonDetails : Fragment() {

    internal lateinit var pokemon_img: ImageView

    internal lateinit var pokemon_name: TextView
    internal lateinit var pokemon_height: TextView
    internal lateinit var pokemon_weight: TextView

    lateinit var recycler_type: RecyclerView
    lateinit var recycler_weaknesses: RecyclerView
    lateinit var recycler_prev_evolution: RecyclerView
    lateinit var recycler_next_evolution: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_details, container, false)

        val pokemon = Common.findPokemonByNum(requireArguments().getString("num"))

        pokemon_img = itemView.findViewById(R.id.pokemon_image) as ImageView
        pokemon_name = itemView.findViewById(R.id.name) as TextView
        pokemon_height = itemView.findViewById(R.id.height) as TextView
        pokemon_weight = itemView.findViewById(R.id.weight) as TextView

        recycler_type = itemView.findViewById(R.id.recycler_type) as RecyclerView
        setRecycler(recycler_type)

        recycler_weaknesses = itemView.findViewById(R.id.recycler_weaknesses) as RecyclerView
        setRecycler(recycler_weaknesses)

        recycler_next_evolution =
            itemView.findViewById(R.id.recycler_next_evolution) as RecyclerView
        setRecycler(recycler_next_evolution)

        recycler_prev_evolution =
            itemView.findViewById(R.id.recycler_prev_evolution) as RecyclerView
        setRecycler(recycler_prev_evolution)

        setDetailsPokemon(pokemon)

        return itemView
    }

    fun setDetailsPokemon(pokemon: Pokemon?) {

        //Load image
        Glide.with(requireActivity()).load(pokemon!!.img).into(pokemon_img)
        pokemon_name.text = pokemon.name
        pokemon_height.text = "Height: " + pokemon.height
        pokemon_weight.text = "Weight: " + pokemon.weight

        val typeAdapter = PokemonTypeAdapter(requireActivity(), pokemon.type!!)
        recycler_type.adapter = typeAdapter

        val weaknessesAdapter = PokemonTypeAdapter(requireActivity(), pokemon.weaknesses!!)
        recycler_weaknesses.adapter = weaknessesAdapter

        recycler_prev_evolution.adapter = evolutionSetter(pokemon.prev_evolution)

        recycler_next_evolution.adapter = evolutionSetter(pokemon.next_evolution)

    }

    fun setRecycler(rv: RecyclerView) {
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }


    fun evolutionSetter(evoList: List<Evolution>?): PokemonEvolutionAdapter {
        if (evoList != null) {
            val npEvolution = PokemonEvolutionAdapter(requireActivity(), evoList)
            return npEvolution
        } else {
            val nullList = listOf<Evolution>()
            val nullEvolution = PokemonEvolutionAdapter(requireActivity(), nullList)
            return nullEvolution
        }
    }

    companion object {
        internal var instance: PokemonDetails? = null
        fun getInstance(): PokemonDetails {
            if (instance == null)
                instance = PokemonDetails()
            return instance!!
        }
    }
}