package com.sobol.f_unsubscribe_app.ui

import android.animation.Animator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.R

class WaitingView(
    context: Context
) : FrameLayout(context) {

    private lateinit var activity: UnsubscribeActivity

    companion object {
        const val ANIMATION_DURATION = 150L
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.waiting_view, this, true)
        activity = context as UnsubscribeActivity
        alpha = 0f
    }

    fun setUpOnCommunitiesRecylerViewPosition(appBarHeight: Int) {
        val height = AndroidUtils.getScreenHeight(activity) - appBarHeight - AndroidUtils.dpToPx(context, 30f).toInt()
        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, height
        )
        params.gravity = Gravity.BOTTOM
        layoutParams = params
    }

    fun appear() {
        animate()
            .alpha(1f)
            .duration = ANIMATION_DURATION
    }

    fun disapper() {
        animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    activity.removeWaitingView()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .alpha(0f)
            .duration = ANIMATION_DURATION
    }

}