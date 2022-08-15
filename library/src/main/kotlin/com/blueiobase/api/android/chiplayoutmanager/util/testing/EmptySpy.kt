package com.blueiobase.api.android.chiplayoutmanager.util.testing

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler


class EmptySpy : ISpy {
    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        //do nothing
    }
}