package com.example.apiproject_pokedex

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject_pokedex.Adapter.PokemonListAdapter
import com.example.apiproject_pokedex.Common.Common
import com.example.apiproject_pokedex.Common.Common.pokemonList
import com.example.apiproject_pokedex.Common.ItemOffsetDecoration
import com.example.apiproject_pokedex.Model.Pokemon
import com.example.apiproject_pokedex.Retrofit.IPokemonList
import com.example.apiproject_pokedex.Retrofit.RetrofitClient
import com.mancj.materialsearchbar.MaterialSearchBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class PokemonList : Fragment() {

    internal var compositeDisposable=CompositeDisposable()
    internal var iPokemonList:IPokemonList

    internal lateinit var recycler_view:RecyclerView

    internal lateinit var adapter:PokemonListAdapter
    internal lateinit var search_adapter:PokemonListAdapter
    internal lateinit var last_suggest:MutableList<String>


    internal lateinit var search_bar:MaterialSearchBar


    init{
        val retrofit=RetrofitClient.instance
        iPokemonList=retrofit.create(IPokemonList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val itemView= inflater.inflate(R.layout.fragment_pokemon_list, container, false)

        recycler_view=itemView.findViewById(R.id.pokemon_recyclerview) as RecyclerView
        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager=GridLayoutManager(activity,3)
        //val itemDecoration= ItemOffsetDecoration(requireActivity(),R.dimen.spacing)
        //recycler_view.addItemDecoration(itemDecoration)

        //Setup Search Bar
        search_bar=itemView.findViewById(R.id.search_bar) as MaterialSearchBar

        last_suggest=arrayListOf()
        search_bar.setHint("Enter Pokemon name: ")
        search_bar.setCardViewElevation(10)
        search_bar.addTextChangeListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val suggest=ArrayList<String>()
                for(search in last_suggest)
                    if(search.toLowerCase().contains(search_bar.text.toLowerCase()))
                        suggest.add(search)
                search_bar.lastSuggestions=suggest
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        search_bar.setOnSearchActionListener(object:MaterialSearchBar.OnSearchActionListener{
            override fun onSearchStateChanged(enabled: Boolean) {
                if(!enabled)
                    recycler_view.adapter=adapter
            }

            override fun onSearchConfirmed(text: CharSequence?) {
                startSearch(text.toString())
            }

            override fun onButtonClicked(buttonCode: Int) {}

        })
        fetchData()

        return itemView
    }

    private fun startSearch(text:String) {
        if (Common.pokemonList.size > 0) {
            val result=ArrayList<Pokemon>()
            for (pokemon in Common.pokemonList)
                if (pokemon.name!!.toLowerCase().contains(text.toLowerCase()))
                    result.add(pokemon)
            search_adapter=PokemonListAdapter(requireActivity(),result)
            recycler_view.adapter=search_adapter

        }
    }

    private fun fetchData() {
       compositeDisposable.add(iPokemonList.listPokemon
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe{pokemonDex ->
                Common.pokemonList=pokemonDex.pokemon!!
               adapter = PokemonListAdapter(requireActivity(),Common.pokemonList)

               recycler_view.adapter=adapter

               last_suggest.clear()
                for(pokemon in Common.pokemonList)
                    last_suggest.add(pokemon.name!!)
               search_bar.visibility=View.VISIBLE
               search_bar.lastSuggestions=last_suggest
               }
        );
    }

}