package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/**
 * This class is responsible for breaking the [View] object anchored at the right/end for the [RTLRowBreakerFactory].
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class RTLBackwardRowBreaker : ILayoutRowBreaker {
    override fun isRowBroke(abstractLayouter: AbstractLayouter) = abstractLayouter.viewLeft + abstractLayouter.currentViewWidth > abstractLayouter.getCanvasRightBorder()
                && abstractLayouter.viewLeft > abstractLayouter.getCanvasLeftBorder()

}