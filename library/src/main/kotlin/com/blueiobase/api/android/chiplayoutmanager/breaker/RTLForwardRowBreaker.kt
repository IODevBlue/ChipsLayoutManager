package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker


/**
 * This class is responsible for breaking the [View] object anchored at the left/start for the [RTLRowBreakerFactory].
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
class RTLForwardRowBreaker : ILayoutRowBreaker {
    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.viewRight < abstractLayouter.getCanvasRightBorder()
                && abstractLayouter.viewRight - abstractLayouter.currentViewWidth < abstractLayouter.getCanvasLeftBorder())
    }
}
