package com.example.apiproject_pokedex

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiproject_pokedex.Adapter.PokemonEvolutionAdapter
import com.example.apiproject_pokedex.Adapter.PokemonTypeAdapter
import com.example.apiproject_pokedex.Common.Common
import com.example.apiproject_pokedex.Model.Pokemon

class PokemonDetails : Fragment() {

    internal lateinit var pokemon_img:ImageView

    internal lateinit var pokemon_name:TextView
    internal lateinit var pokemon_height:TextView
    internal lateinit var pokemon_weight:TextView

    lateinit var recycler_type:RecyclerView
    lateinit var recycler_weaknesses:RecyclerView
    lateinit var recycler_prev_evolution:RecyclerView
    lateinit var recycler_next_evolution:RecyclerView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val itemView = inflater.inflate(R.layout.fragment_pokemon_details, container, false)

        val pokemon=Common.findPokemonByNum(requireArguments().getString("num"))

        pokemon_img=itemView.findViewById(R.id.pokemon_image) as ImageView
        pokemon_name=itemView.findViewById(R.id.name) as TextView
        pokemon_height=itemView.findViewById(R.id.height) as TextView
        pokemon_weight=itemView.findViewById(R.id.weight) as TextView

        recycler_type=itemView.findViewById(R.id.recycler_type)as RecyclerView
        recycler_type.setHasFixedSize(true)
        recycler_type.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        recycler_weaknesses=itemView.findViewById(R.id.recycler_weaknesses)as RecyclerView
        recycler_weaknesses.setHasFixedSize(true)
        recycler_weaknesses.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)



        recycler_next_evolution=itemView.findViewById(R.id.recycler_next)as RecyclerView
        recycler_next_evolution.setHasFixedSize(true)
        recycler_next_evolution.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        recycler_prev_evolution=itemView.findViewById(R.id.recycler_prev)as RecyclerView
        recycler_prev_evolution.setHasFixedSize(true)
        recycler_prev_evolution.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)

        setDetailsPokemon(pokemon)

        return itemView
    }

    private fun setDetailsPokemon(pokemon: Pokemon?) {
        //Load image
        Glide.with(requireActivity()).load(pokemon!!.img).into(pokemon_img)
        pokemon_name.text = pokemon.name
        pokemon_height.text = "Height: " + pokemon.height
        pokemon_weight.text = "Weight: " + pokemon.weight

        val typeAdapter = PokemonTypeAdapter(requireActivity(), pokemon.type!!)
        recycler_type.adapter = typeAdapter

        val weaknessesAdapter = PokemonTypeAdapter(requireActivity(), pokemon.weaknesses!!)
        recycler_weaknesses.adapter = weaknessesAdapter

        if (pokemon.prev_evolution != null)
        {
            val prevEvolution=PokemonEvolutionAdapter(requireActivity(),pokemon.prev_evolution!!)
            recycler_prev_evolution.adapter=prevEvolution
        }

        if (pokemon.next_evolution != null)
        {
            val nextEvolution=PokemonEvolutionAdapter(requireActivity(),pokemon.next_evolution!!)
            recycler_next_evolution.adapter=nextEvolution
        }
    }

    companion object {
        internal var instance:PokemonDetails?=null
        fun getInstance():PokemonDetails{
            if(instance==null)
                instance=PokemonDetails()
            return instance!!
        }
    }
}