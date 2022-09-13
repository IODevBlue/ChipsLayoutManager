package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/**
 * This class is responsible for breaking the [View] object anchored at the left/start for the [DecoratorBreakerFactory].
 *
 * @author IO DevBlue
 * @since 1.0.0
 */
class LTRForwardRowBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.viewLeft > abstractLayouter.getCanvasLeftBorder()
                && abstractLayouter.viewLeft + abstractLayouter.currentViewWidth > abstractLayouter.getCanvasRightBorder())
    }
}