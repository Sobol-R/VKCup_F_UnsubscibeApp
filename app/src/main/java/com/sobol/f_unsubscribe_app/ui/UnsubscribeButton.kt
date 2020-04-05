package com.sobol.f_unsubscribe_app.ui

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.Database
import com.sobol.f_unsubscribe_app.LeaveGroupRequest
import com.sobol.f_unsubscribe_app.R
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.unsub_button.view.*

class UnsubscribeButton(
    context: Context
) : FrameLayout(context) {

    private lateinit var activity: UnsubscribeActivity
    private var count = 0

    object SetValue {
        const val ADD = 1
        const val DELETE = -1
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.unsub_button, this, true)
        activity = context as UnsubscribeActivity
        alpha = 0f

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                appear()
                activity.updateRecyclerViewMargin(height)
            }
        })

        count_container.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                count_container.viewTreeObserver.removeOnGlobalLayoutListener(this)
                count_container.layoutParams.width = count_container.height
                setCount(0)
            }
        })

        setOnClickListener {
            leaveGroups()
        }
    }

    private fun leaveGroups() {
        activity.showWaitingView()
        val unsubGroups = Database.instance.getUnsubCommunitites()
        for (i in 0 until unsubGroups.size) {
            VK.execute(LeaveGroupRequest(unsubGroups[i].id), object : VKApiCallback<Int> {
                override fun success(result: Int) {
                    if (i == unsubGroups.size-1) {
                        unsubGroups.clear()
                        activity.updateRecyclerViewMargin(0)
                        activity.closeUnsubButton()
                        activity.requestGroups()
                    }
                }

                override fun fail(error: VKApiExecutionException) {
                    if (i == unsubGroups.size-1) {
                        unsubGroups.clear()
                        activity.updateRecyclerViewMargin(0)
                        activity.closeUnsubButton()
                        activity.requestGroups()
                    }
                    println(error.detailMessage)
                }
            })
        }
    }

    private fun processRequestRes() {

    }

    fun setCount(value: Int) {
        count += value
        count_text.text = count.toString()
        if (count == 0)
            disappear()
    }

    fun appear() {
        animate()
            .alpha(1f)
            .duration = 150
    }

    private fun disappear() {
        activity.updateRecyclerViewMargin(0)
        animate()
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    activity.closeUnsubButton()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .alpha(0f)
            .duration = 150
    }

}