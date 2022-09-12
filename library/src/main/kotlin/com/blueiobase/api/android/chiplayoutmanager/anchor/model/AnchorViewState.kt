package com.blueiobase.api.android.chiplayoutmanager.anchor.model

import android.graphics.Rect
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * This class represents a [View] which is anchored/positioned at the extreme left/start of the [layout manager][RecyclerView.LayoutManager].
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
data class AnchorViewState constructor(var position: Int?, var anchorViewRect: Rect?): Parcelable {

    constructor(parcel: Parcel) : this() {
        val parcelPosition = parcel.readInt()
        position = parcelPosition.let{ if (it == -1) null else it}
        anchorViewRect = parcel.readParcelable(Rect::class.java.classLoader)
    }

    private constructor(): this(null, null)

    companion object CREATOR : Parcelable.Creator<AnchorViewState> {

        /** Returns a new instance of [AnchorViewState] with null parameters.*/
        fun getNotFoundState() =  AnchorViewState()

        override fun createFromParcel(parcel: Parcel) = AnchorViewState(parcel)

        override fun newArray(size: Int) = arrayOfNulls<AnchorViewState?>(size)

    }

    /**
     * Validates if there is any [View] anchored to the extreme left/start.
     *
     * @return `true` if there is an anchored [View] to the extreme left/start of screen, `false` if otherwise.
     */
    fun isNotFoundState() =  anchorViewRect == null

    /**
     * Validates if the [View] at the extreme left/start is being removed or has been removed.
     *
     * @return `true` if removed, `false` if otherwise.
     */
    fun isRemoving() = position == -1

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(position?:-1)
        parcel.writeParcelable(anchorViewRect, flags)
    }

    override fun describeContents() = 0

    override fun toString() = String.format(Locale.getDefault(), "AnchorViewState. Position = %d, Rect = %s", position, anchorViewRect.toString())

}




