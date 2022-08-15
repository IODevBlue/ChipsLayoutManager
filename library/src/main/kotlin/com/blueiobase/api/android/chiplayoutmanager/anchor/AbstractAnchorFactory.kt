package com.blueiobase.api.android.chiplayoutmanager.anchor

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.anchor.contract.IAnchorFactory
import com.blueiobase.api.android.chiplayoutmanager.anchor.model.AnchorViewState
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas

/**
 * This is the base class implementation of the [IAnchorFactory] interface.
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
abstract class AbstractAnchorFactory(private val layoutManager: RecyclerView.LayoutManager, val canvas: ICanvas): IAnchorFactory {

    /**
     * Generates an [AnchorViewState] for the provided [view] parameter.
     *
     * @param view The [View] at either the start or end of the [layout manager][RecyclerView.LayoutManager]
     * @return An [AnchorViewState] for the anchored [View].
     */
    fun createAnchorState(view: View) = AnchorViewState(layoutManager.getPosition(view), canvas.getViewRect(view))

    override fun createNotFound() = AnchorViewState.getNotFoundState()

}