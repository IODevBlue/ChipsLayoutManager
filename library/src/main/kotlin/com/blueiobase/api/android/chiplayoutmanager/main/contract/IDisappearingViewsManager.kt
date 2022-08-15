package com.blueiobase.api.android.chiplayoutmanager.main.contract

import androidx.recyclerview.widget.RecyclerView.Recycler
import com.blueiobase.api.android.chiplayoutmanager.main.DisappearingViewsManager


interface IDisappearingViewsManager {

     fun getDisappearingViews(recycler: Recycler): DisappearingViewsManager.DisappearingViewsContainer?

     fun calcDisappearingViewsLength(recycler: Recycler): Int

     fun getDeletingItemsOnScreenCount(): Int

     fun reset()
}