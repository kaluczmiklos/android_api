package com.example.apiproject_pokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.apiproject_pokedex.Common.Common
import androidx.fragment.app.FragmentManager
import com.example.apiproject_pokedex.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val br = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {

            val num = intent?.getStringExtra("num")
            val pokemon = Common.findPokemonByNum(num)
            val detailsFragment: PokemonDetails = PokemonDetails.getInstance()

            if (intent!!.action!!.toString() == Common.KEY_ENABLE_HOME) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)

                val bundle = Bundle()
                bundle.putString("num", num)
                detailsFragment.arguments = bundle
            } else if (intent.action!!.toString() == Common.KEY_NUM_EVOLUTION) {
                detailsFragment.setDetailsPokemon(pokemon)
            }

            //Replace fragment
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.list_pokemon_fragment, detailsFragment)
            fragmentTransaction.addToBackStack("detail")
            fragmentTransaction.commit()

            //Set Pokemon name for toolbar
            binding.toolbar.title = pokemon!!.name
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.toolbar.setTitle("POKEDEX")
        setSupportActionBar(binding.toolbar)

        //Register broadcasts
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(br, IntentFilter(Common.KEY_ENABLE_HOME))

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(br, IntentFilter(Common.KEY_NUM_EVOLUTION))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.toolbar.title = "POKEDEX"

                //Clear all fragment in the stack with name 'detail'
                supportFragmentManager.popBackStack(
                    "detail",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                supportActionBar!!.setDisplayShowHomeEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            }
        }
        return true
    }

}