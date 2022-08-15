package com.blueiobase.api.android.chiplayoutmanager.cache.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class CacheParcelableContainer(var startsRow: NavigableSet<Int>, var endsRow: NavigableSet<Int>): Parcelable {

    constructor(parcel: Parcel) : this(TreeSet<Int>(), TreeSet<Int>()) {
        val startsRowList: List<Int> = LinkedList()
        val endsRowList: List<Int> = LinkedList()
        parcel.apply {
            readList(startsRowList, Int::class.java.classLoader)
            readList(endsRowList, Int::class.java.classLoader)
        }

        startsRow = TreeSet(startsRowList)
        endsRow = TreeSet(endsRowList)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        val startRowList: List<Int> = LinkedList(startsRow)
        val endRowList: List<Int> = LinkedList(endsRow)

        parcel.writeList(startRowList)
        parcel.writeList(endRowList)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<CacheParcelableContainer> {
        override fun createFromParcel(parcel: Parcel) = CacheParcelableContainer(parcel)

        override fun newArray(size: Int) = arrayOfNulls<CacheParcelableContainer?>(size)
    }
}