package com.example.apiproject_pokedex.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject_pokedex.Common.Common
import com.example.apiproject_pokedex.Model.Evolution
import com.example.apiproject_pokedex.R
import com.robertlevonyan.views.chip.Chip

class PokemonEvolutionAdapter(internal var context: Context, var evolutionList: List<Evolution>) :
    RecyclerView.Adapter<PokemonEvolutionAdapter.MyViewHolder>() {

    init {
        if (evolutionList == null)
            evolutionList = ArrayList()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var chip: Chip

        init {
            chip = itemView.findViewById(R.id.chip) as Chip
            chip.setOnChipClickListener {
                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(
                        Intent(Common.KEY_NUM_EVOLUTION).putExtra(
                            "num",
                            evolutionList[adapterPosition].num
                        )
                    )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.chip.chipText = evolutionList[position].name
        holder.chip.changeBackgroundColor(
            Common.getColorByType(
                Common.findPokemonByNum(
                    evolutionList[position].num!!
                )!!.type!![0]
            )
        )
    }

    override fun getItemCount(): Int {
        return evolutionList.size
    }
}