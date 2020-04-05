package com.sobol.f_unsubscribe_app.ui.communities.recycler_view

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sobol.f_unsubscribe_app.AndroidUtils

class SpacesItemDecoration(
    private val space: Int,
    private val halfSpace: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {

        val position = parent.getChildLayoutPosition(view)

        when {
            position % 3 == 0 -> {
                outRect.left = space
                outRect.right = halfSpace
            }
            (position - 1) % 3 == 0 -> {
                outRect.left = halfSpace
                outRect.right = halfSpace
            }
            else -> {
                outRect.left = halfSpace
                outRect.right = space
            }
        }

        if (position < 3) {
            outRect.top = AndroidUtils.dpToPx(view.context, 30f).toInt()
        } else {
            outRect.top = 0
        }
    }

}