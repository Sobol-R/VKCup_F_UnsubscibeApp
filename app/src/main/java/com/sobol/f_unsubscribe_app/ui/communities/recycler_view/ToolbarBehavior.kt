package com.sobol.f_unsubscribe_app.ui.communities.recycler_view

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewTreeObserver

private const val ANIMATION_DURATION = 150L

class ToolbarBehavior(
    private val toolbar: CardView,
    private val appBarLayout: AppBarLayout
) : RecyclerView.OnScrollListener() {

    private var fromStart = true
    private var appBarExpanded = true
    private var scrolled = 0
    private var elevationRemoved = true
    private var cardElevation = 0f

    init {
        toolbar.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                toolbar.viewTreeObserver.removeOnGlobalLayoutListener(this)
                toolbar.y = -toolbar.height.toFloat()
                cardElevation = toolbar.cardElevation
                toolbar.cardElevation = 0f
                toolbar.visibility = View.VISIBLE
                fromStart = false
            }
        })

        appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
                if (state.name == "EXPANDED" && !fromStart) {
                    closeToolbar(true)
                    appBarExpanded = true
                } else if (state.name == "COLLAPSED") {
                    appBarExpanded = false
                    animateElevation()
                }
            }
        })
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        when {
            toolbar.y - dy > 0f -> toolbar.y = 0f
            toolbar.y - dy < -toolbar.height -> toolbar.y = -toolbar.height.toFloat()
            else -> toolbar.y -= dy
        }
    }

    private fun closeToolbar(removeElevation: Boolean) {
        toolbar.animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (removeElevation) {
                        toolbar.cardElevation = 0f
                        elevationRemoved = true
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .y(-toolbar.height.toFloat())
            .duration = ANIMATION_DURATION
    }

    private fun openToolbar() {
        if (elevationRemoved)
            toolbar.cardElevation = cardElevation
        toolbar.animate()
            .y(0f)
            .duration = ANIMATION_DURATION
    }

    private fun animateElevation() {
        elevationRemoved = false
        val animator = ValueAnimator.ofFloat(0f, cardElevation)
        animator.addUpdateListener {
            toolbar.cardElevation = it.animatedValue as Float
        }
        animator.duration = 150
        animator.start()
    }

}