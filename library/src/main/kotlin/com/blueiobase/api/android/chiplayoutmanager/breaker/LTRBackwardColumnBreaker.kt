package com.blueiobase.api.android.chiplayoutmanager.breaker

import android.view.View
import com.blueiobase.api.android.chiplayoutmanager.layouter.AbstractLayouter
import com.blueiobase.api.android.chiplayoutmanager.breaker.contract.ILayoutRowBreaker
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager

/**
 * This class is responsible for breaking the [View] object anchored at the right/end for the [ColumnBreakerFactory].
 *
 * @author IO DevBlue
 * @since 1.0.0
 *
 */
class LTRBackwardColumnBreaker: ILayoutRowBreaker {

    override fun isRowBroke(abstractLayouter: AbstractLayouter): Boolean {
        return (abstractLayouter.getViewBottom() - abstractLayouter.currentViewHeight < abstractLayouter.getCanvasTopBorder()
                && abstractLayouter.getViewBottom() < abstractLayouter.getCanvasBottomBorder())
    }
}