package com.example.apiproject_pokedex

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject_pokedex.Common.Common
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.apiproject_pokedex.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    //Create Broadcast Handlers
    private val showDetails=object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent!!.action!!.toString()== Common.KEY_ENABLE_HOME)
            {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)

                //Replace Fragment
                val detailFragment:PokemonDetails=PokemonDetails.getInstance()
                val num=intent.getStringExtra("num")
                val bundle=Bundle()
                bundle.putString("num",num)
                detailFragment.arguments=bundle

                val fragmentTransaction=supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.list_pokemon_fragment,detailFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                //Set Pokemon Name for Toolbar
                val pokemon=Common.findPokemonByNum(num)
                binding.toolbar.title=pokemon!!.name
            }
        }
    }

    private val showEvolutions=object:BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            if(intent!!.action!!.toString()== Common.KEY_NUM_EVOLUTION)
            {
                //Replace Fragment
                val num=intent.getStringExtra("num")
                val pokemon=Common.findPokemonByNum(num)
                println(num)
                val detailsFragment:PokemonDetails=PokemonDetails.getInstance()
                detailsFragment.setDetailsPokemon(pokemon)

                //unnecessary
                //val bundle:Bundle=Bundle()
                //bundle.putString("num",num)
                //detailsFragment.arguments=bundle

                val fragmentTransaction=supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.list_pokemon_fragment,detailsFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                //Set Pokemon Name for Toolbar

                println("next "+pokemon!!.next_evolution)
                println("prev "+pokemon!!.prev_evolution)
                binding.toolbar.title=pokemon!!.name
           }
        }
    }

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.setTitle("POKEDEX")
        setSupportActionBar(binding.toolbar)

        //Register Broadcast
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showDetails, IntentFilter(Common.KEY_ENABLE_HOME))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showEvolutions, IntentFilter(Common.KEY_NUM_EVOLUTION))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item!!.itemId)
        {
            android.R.id.home -> {
                binding.toolbar.title="POKEDEX"

                //Clear all fragment in stack with name 'detail'
                supportFragmentManager.popBackStack("detail",FragmentManager.POP_BACK_STACK_INCLUSIVE)

                supportActionBar!!.setDisplayShowHomeEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            }
        }
        return true
    }

}