package com.sobol.f_unsubscribe_app.ui.communities

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.R
import kotlinx.android.synthetic.main.item_selected_view.view.*

class ItemSelectedView(
    context: Context,
    attributeSet: AttributeSet
) : FrameLayout(context, attributeSet) {

    companion object {
        const val ANIMATION_DURATION = 125L
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_selected_view, this, true)
        check_bg.alpha = 0f
        check_ic.scaleX = 0f
        check_ic.scaleY = 0f
    }

    fun reveal() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            check_bg.alpha = value
            check_ic.scaleX = value
            check_ic.scaleY = value
        }
        animator.duration = ANIMATION_DURATION
        animator.start()
    }

    fun dismiss(instant: Boolean) {
        if (instant) {
            check_bg.alpha = 0f
            check_ic.scaleX = 0f
            check_ic.scaleY = 0f
        } else {
            val animator = ValueAnimator.ofFloat(1f, 0f)
            animator.addUpdateListener {
                val value = it.animatedValue as Float
                check_bg.alpha = value
                check_ic.scaleX = value
                check_ic.scaleY = value
            }
            animator.duration = ANIMATION_DURATION
            animator.start()
        }
    }

}