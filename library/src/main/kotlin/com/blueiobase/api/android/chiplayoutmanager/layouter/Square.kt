package com.blueiobase.api.android.chiplayoutmanager.layouter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blueiobase.api.android.chiplayoutmanager.layouter.contract.ICanvas
import com.blueiobase.api.android.chiplayoutmanager.main.ChildViewsIterable
import com.blueiobase.api.android.chiplayoutmanager.main.ChipsLayoutManager


abstract class Square (private val layoutManager: RecyclerView.LayoutManager): ICanvas {

    private val childViews = ChildViewsIterable(layoutManager)

    /**
     * highest view in layout. Have always actual value, because it set in [ChipsLayoutManager.onLayoutChildren]
     */
    private var topView: View? = null

    /**
     * lowest view in layout. Have always actual value, because it set in [ChipsLayoutManager.onLayoutChildren]
     */
    private var bottomView: View? = null

    /**
     * The view have placed in the closest to the left border. Have always actual value, because it set in [ChipsLayoutManager.onLayoutChildren]
     */
    private var leftView: View? = null

    /** The view have placed in the closest to the right border. Have always actual value, because it set in [ChipsLayoutManager.onLayoutChildren]  */
    private var rightView: View? = null


    /** minimal position visible on screen */
    private var minPositionOnScreen: Int? = null
    private var maxPositionOnScreen: Int? = null

    private var isFirstItemAdded = false

    override fun getCanvasRect(): Rect {
        return Rect(
            getCanvasLeftBorder(),
            getCanvasTopBorder(),
            getCanvasRightBorder(),
            getCanvasBottomBorder()
        )
    }

    override fun getViewRect(view: View): Rect {
        layoutManager.apply {
            val left: Int = getDecoratedLeft(view)
            val top: Int = getDecoratedTop(view)
            val right: Int = getDecoratedRight(view)
            val bottom: Int = getDecoratedBottom(view)
            return Rect(left, top, right, bottom)
        }

    }

    override fun isInside(rectCandidate: Rect): Boolean {
        //intersection changes rect!!!
        val intersect = Rect(rectCandidate)
        return getCanvasRect().intersect(intersect)
    }

    override fun isInside(viewCandidate: View) = isInside(getViewRect(viewCandidate))

    override fun isFullyVisible(view: View): Boolean {
        val rect: Rect = getViewRect(view)
        return isFullyVisible(rect)
    }

    override fun isFullyVisible(rect: Rect) =
        rect.top >= getCanvasTopBorder()
                && rect.bottom <= getCanvasBottomBorder()
                && rect.left >= getCanvasLeftBorder() && rect.right <= getCanvasRightBorder()

    /**
     * find highest & lowest views among visible attached views
     */
    override fun findBorderViews() {
        topView = null
        bottomView = null
        leftView = null
        rightView = null
        minPositionOnScreen = RecyclerView.NO_POSITION
        maxPositionOnScreen = RecyclerView.NO_POSITION
        isFirstItemAdded = false
        layoutManager.apply {
            if (childCount > 0) {
                val initView = getChildAt(0)
                topView = initView
                bottomView = initView
                leftView = initView
                rightView = initView
                for (view in childViews) {
                    val position: Int = getPosition(view)
                    if (!isInside(view)) continue
                    if (getDecoratedTop(view) < getDecoratedTop(topView!!)) {
                        topView = view
                    }
                    if (getDecoratedBottom(view) > getDecoratedBottom(bottomView!!)) {
                        bottomView = view
                    }
                    if (getDecoratedLeft(view) < getDecoratedLeft(leftView!!)) {
                        leftView = view
                    }
                    if (getDecoratedRight(view) > getDecoratedRight(rightView!!)) {
                        rightView = view
                    }
                    if (minPositionOnScreen == RecyclerView.NO_POSITION || position < minPositionOnScreen!!) {
                        minPositionOnScreen = position
                    }
                    if (maxPositionOnScreen == RecyclerView.NO_POSITION || position > maxPositionOnScreen!!) {
                        maxPositionOnScreen = position
                    }
                    if (position == 0) {
                        isFirstItemAdded = true
                    }
                }
            }

        }
    }

    override fun getTopView() = topView

    override fun getBottomView() = bottomView

    override fun getLeftView() = leftView

    override fun getRightView() = rightView

    override fun getMinPositionOnScreen() = minPositionOnScreen

    override fun getMaxPositionOnScreen() = maxPositionOnScreen

    override fun isFirstItemAdded() = isFirstItemAdded

}