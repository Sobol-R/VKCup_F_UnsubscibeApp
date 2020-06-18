package com.sobol.f_unsubscribe_app.ui.pop_up_view

import android.animation.Animator
import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.api.FriendsCountRequest
import com.sobol.f_unsubscribe_app.api.GetLastPostTime
import com.sobol.f_unsubscribe_app.R
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.community_info_view.view.*

private const val APPEAR_DURATION = 250L

class CommunityInfoView (
    context: Context
) : FrameLayout(context) {

    private lateinit var container: PopUpContainerView

    private var membersCount = "Загрузка..."
    private var friendsCount = "Загрузка..."
    private var lastPost = "Загрузка..."

    fun init(container: PopUpContainerView) {
        this.container = container

        LayoutInflater.from(context).inflate(R.layout.community_info_view, this, true)

        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        params.behavior = BottomSheetBehavior<FrameLayout>()
        layoutParams = params

        close_button.setOnClickListener {
            val unsubscribeActivity = context as UnsubscribeActivity
            unsubscribeActivity.onBackPressed()
        }

        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                container.setPeekHeight(height)
            }
        })

    }

    fun setData(group: Group) {
        loadFriendsCount(group.id)
        loadPostTime(group.id)

        membersCount = "${group.membersCount} подписчиков"

        title.text = group.name
        setSubsInfo()
        if (TextUtils.isEmpty(group.description))
            description_content.visibility = View.GONE
        else
            article_info.text = group.description
        news_info.text = lastPost
    }

    private fun setSubsInfo() {
        subs_info.text = "$membersCount · $friendsCount"
    }

    private fun loadFriendsCount(id: Int) {
        VK.execute(FriendsCountRequest(id), object : VKApiCallback<Int> {
            override fun success(result: Int) {
                friendsCount = "$result друзей"
                setSubsInfo()
            }

            override fun fail(error: VKApiExecutionException) {
                println("ON FAIL")
            }
        })
    }

    private fun loadPostTime(id: Int) {
        VK.execute(GetLastPostTime(id), object : VKApiCallback<Int> {
            override fun success(result: Int) {
                lastPost = "Последний пост был $result"
                setSubsInfo()
            }

            override fun fail(error: VKApiExecutionException) {
                println("ON FAIL")
            }
        })
    }

    fun close() {
        animate()
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    container.disappear()
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }

            })
            .y(AndroidUtils.getScreenHeight(context as UnsubscribeActivity).toFloat())
            .duration = APPEAR_DURATION
    }

}
