package com.sobol.f_unsubscribe_app.ui.pop_up_view

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.R
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity
import kotlinx.android.synthetic.main.popup_container.view.*

class PopUpContainerView(
    context: Context
) : FrameLayout(context) {

    val APPEAR_DURATION = 150L
    private lateinit var unsubscribeActivity: UnsubscribeActivity
    private lateinit var behavior: BottomSheetBehavior<CommunityInfoView>
    private val communityView = CommunityInfoView(context)
    private val openButton = OpenButtonView(context)

    private var closeButtonWithDelay = true

    init {
        z = 101f
    }

    fun init(group: Group) {
        LayoutInflater.from(context).inflate(R.layout.popup_container, this, true)
        unsubscribeActivity = context as UnsubscribeActivity
        communityView.init(this)
        communityView.setData(group)
        openButton.init(group.screenName)
        initBottomSheetBehavior()
        content.setOnClickListener {
            unsubscribeActivity.onBackPressed()
        }
        content.addView(communityView)
        content.addView(openButton)
    }

    private fun initBottomSheetBehavior() {
        behavior = BottomSheetBehavior.from(communityView)
        behavior.isHideable = true
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(view: View, state: Int) {
                if (state == BottomSheetBehavior.STATE_HIDDEN) {
                    closeButtonWithDelay = false
                    unsubscribeActivity.onBackPressed()
                }
            }
        })
    }

    fun setPeekHeight(height: Int) {
        val animator = ValueAnimator.ofInt(0, height)
        animator.addUpdateListener {
            behavior.peekHeight = it.animatedValue as Int
        }
        animator.duration = 300
        animator.start()
    }

    fun appear() {
        unsubscribeActivity.setPopUpViewStatusBar()
        content.animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    openButton.appear()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
            .alpha(1f)
            .duration = APPEAR_DURATION
    }

    fun closeShareView() {
        communityView.close()
        openButton.disappear(closeButtonWithDelay)
    }

    fun disappear() {
        unsubscribeActivity.setUpStatusBar()
        content.animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    unsubscribeActivity.closeShareView()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

            })
            .alpha(0f)
            .duration = APPEAR_DURATION
    }

}