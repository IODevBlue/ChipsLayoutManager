package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker

/**
 * This class is responsible for breaking the [View] object anchored at the left/start for the [ColumnBreakerFactory].
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class LTRForwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter) = (abstractLayouter.getViewTop() > abstractLayouter.getCanvasTopBorder()
                && abstractLayouter.getViewTop() + abstractLayouter.currentViewHeight > abstractLayouter.getCanvasBottomBorder())

}