package com.blueiobase.api.android.chiplayoutmanager.main.model

import android.content.res.Configuration
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.util.SparseArray
import androidx.annotation.IntRange
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.annotation.DeviceOrientation
import com.blueiobase.api.android.chiplayoutmanager.cache.model.CacheParcelableContainer

class ParcelableContainer : Parcelable {
    var anchorViewState: AnchorViewState? = null
        private set
    private var orientationCacheMap = SparseArray<Any>()
    private var cacheNormalizationPositionMap = SparseArray<Any>()

    //store previous orientation
    var orientation = 0
        @DeviceOrientation get
        private set

    constructor() {
        //initial values. We should normalize cache when scrolled to zero in case first time of changing layoutOrientation state
        cacheNormalizationPositionMap.apply {
            put(Configuration.ORIENTATION_PORTRAIT, 0)
            put(Configuration.ORIENTATION_LANDSCAPE, 0)
        }
    }

    fun putAnchorViewState(anchorViewState: AnchorViewState) {
        this.anchorViewState = anchorViewState
    }

    fun putOrientation(@DeviceOrientation orientation: Int) {
        this.orientation = orientation
    }

    private constructor(parcel: Parcel) {
        anchorViewState = AnchorViewState.createFromParcel(parcel)
        orientationCacheMap = parcel.readSparseArray(CacheParcelableContainer::class.java.classLoader)!!
        cacheNormalizationPositionMap = parcel.readSparseArray(Int::class.java.classLoader)!!
        orientation = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        anchorViewState!!.writeToParcel(parcel, i)
        parcel.apply {
            writeSparseArray(orientationCacheMap)
            writeSparseArray(cacheNormalizationPositionMap)
            writeInt(orientation)
        }
    }

    fun putPositionsCache(@DeviceOrientation orientation: Int, parcelable: Parcelable) {
        orientationCacheMap.put(orientation, parcelable)
    }

    fun putNormalizationPosition(@DeviceOrientation orientation: Int, normalizationPosition: Int) {
        cacheNormalizationPositionMap.put(orientation, normalizationPosition)
    }

    fun getPositionsCache(@DeviceOrientation orientation: Int) = orientationCacheMap[orientation] as Parcelable

    @IntRange(from = 0)
    fun getNormalizationPosition(@DeviceOrientation orientation: Int) = cacheNormalizationPositionMap[orientation] as Int

    override fun describeContents() = 0

    companion object CREATOR : Creator<ParcelableContainer> {

        override fun createFromParcel(parcel: Parcel) = ParcelableContainer(parcel)

        override fun newArray(size: Int) = arrayOfNulls<ParcelableContainer?>(size)

    }
}
