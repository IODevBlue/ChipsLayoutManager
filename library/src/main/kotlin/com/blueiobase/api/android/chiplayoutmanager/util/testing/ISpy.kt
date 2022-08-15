package com.blueiobase.api.android.chiplayoutmanager.util.testing

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


interface ISpy {
    fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State)
}