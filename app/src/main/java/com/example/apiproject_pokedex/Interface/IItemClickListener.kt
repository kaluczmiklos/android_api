package com.example.apiproject_pokedex.Interface

import android.view.View

interface IItemClickListener {
    fun onClick(view: View, position: Int)
}