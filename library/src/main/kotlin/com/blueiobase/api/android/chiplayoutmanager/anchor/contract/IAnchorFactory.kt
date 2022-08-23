package com.blueiobase.api.android.chiplayoutmanager.anchor.contract

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState

/**
 * Interface for the management and creation of [AnchorViewState] for anchored [View] objects.
 *
 * This interface sets the base requirements for implementers that should manage the anchoring of [View] objects
 * at the extreme ends of the screen.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
interface IAnchorFactory {

    /**
     * Gets the [View] at the extreme left/start in the [layout manager][RecyclerView.LayoutManager].
     *
     * @return [AnchorViewState] representing the [View] closest to the left/start of the screen.
     */
    fun getAnchor(): AnchorViewState

    /**
     * Creates an empty [AnchorViewState] for the left-most/right-most [View] if none is present.
     *
     * @return An [AnchorViewState] with null parameters.
     */
    fun createNotFound(): AnchorViewState

    /**
     * Restores changes made to the provided [anchorViewState].
     *
     * @param anchorViewState The [AnchorViewState] object whose row coordinates should be reset.
     */
    fun resetRowCoordinates(anchorViewState: AnchorViewState)

}