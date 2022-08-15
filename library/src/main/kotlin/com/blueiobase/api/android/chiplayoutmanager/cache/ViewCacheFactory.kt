package com.blueiobase.api.android.chiplayoutmanager.cache

import androidx.recyclerview.widget.RecyclerView

class ViewCacheFactory(private val layoutManager: RecyclerView.LayoutManager) {

    fun createCacheStorage() =  ViewCacheStorage(layoutManager)

}