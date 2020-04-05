package com.sobol.f_unsubscribe_app.ui

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import android.widget.Toast
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.Database
import com.sobol.f_unsubscribe_app.GroupsRequest
import com.sobol.f_unsubscribe_app.R
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.communities.CommunityAdapter
import com.sobol.f_unsubscribe_app.ui.communities.recycler_view.SpacesItemDecoration
import com.sobol.f_unsubscribe_app.ui.communities.recycler_view.ToolbarBehavior
import com.sobol.f_unsubscribe_app.ui.pop_up_view.PopUpContainerView
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.VKTokenExpiredHandler
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKApiExecutionException
import kotlinx.android.synthetic.main.activity_unsubscribe.*
import android.content.ActivityNotFoundException
import android.net.Uri

class UnsubscribeActivity : AppCompatActivity() {

    private lateinit var adapter: CommunityAdapter
    private var popUpContainer: PopUpContainerView? = null
    private var unsubButton: UnsubscribeButton? = null
    private var waitingView: WaitingView? = null

    private var isOpenWaitingView = false
    private var isOpenShareView = false
    private var loginRequested = false
    private var needToUpdateRecyclerView = false
    private var recyclerViewSetted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpStatusBar()
        setContentView(R.layout.activity_unsubscribe)
        setVKTokeTracker()
        login()
        prepareUiForRequest()
        requestGroups()
    }

    private fun prepareUiForRequest() {
        appBarLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                appBarLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                showWaitingView()
            }
        })
    }

    fun showWaitingView() {
        if (!isOpenWaitingView) {
            isOpenWaitingView = true
            waitingView = WaitingView(this)
            waitingView!!.setUpOnCommunitiesRecylerViewPosition(appBarLayout.height)
            content.addView(waitingView)
            waitingView!!.appear()
        }
    }

    fun removeWaitingView() {
        isOpenWaitingView = false
        content.removeView(waitingView)
        waitingView = null
    }

    fun requestGroups() {
        if (VK.isLoggedIn()) {
            VK.execute(GroupsRequest(), object : VKApiCallback<List<Group>> {
                override fun success(result: List<Group>) {
                    if (!isFinishing && !result.isEmpty()) {
                        waitingView!!.disapper()
                        updateRecyclerView(result)
                    }
                }

                override fun fail(error: VKApiExecutionException) {
                    println("ON FAIL")
                }
            })
        }
    }

    private fun updateRecyclerView(result: List<Group>) {
        if (recyclerViewSetted) {
            adapter.setData(result)
            adapter.notifyDataSetChanged()
        } else
            setRecyclerView(result)
    }

    private fun setRecyclerView(groups: List<Group>) {
        adapter = CommunityAdapter(this)
        adapter.setData(groups)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = GridLayoutManager(this, 3)
        recycler_view.addItemDecoration(
            SpacesItemDecoration(
                AndroidUtils.dpToPx(this, 15f).toInt(),
                AndroidUtils.dpToPx(this, 7.5f).toInt()
            )
        )
        recycler_view.addOnScrollListener(
            ToolbarBehavior(
                toolbar,
                appBarLayout
            )
        )
        recyclerViewSetted = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val context = this
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                requestGroups()
                Toast.makeText(context, resources.getString(R.string.authorization_passed), Toast.LENGTH_LONG).show()
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(context, resources.getString(R.string.authorization_failed), Toast.LENGTH_LONG).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setVKTokeTracker() {
        VK.addTokenExpiredHandler(object : VKTokenExpiredHandler {
            override fun onTokenExpired() {
                login()
            }
        })
    }

    private fun login() {
        if (!VK.isLoggedIn() && !loginRequested) {
            loginRequested = true
            VK.login(this, arrayListOf(VKScope.WALL, VKScope.PHOTOS, VKScope.GROUPS))
        }
    }

    override fun onResume() {
        super.onResume()
        val size = Database.instance.getUnsubCommunitites().size
        if (size > 0 && unsubButton == null) {
            needToUpdateRecyclerView = true
            setUnsubButton(size)
        }
    }

    fun updateRecyclerViewMargin(bottomMargin: Int) {
        val params = recycler_view.layoutParams as CoordinatorLayout.LayoutParams
        params.bottomMargin = bottomMargin
        recycler_view.layoutParams = params
    }

    fun setUnsubButton(value: Int) {
        if (unsubButton == null) {
            unsubButton = UnsubscribeButton(this)
            val params = CoordinatorLayout.LayoutParams(
                CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.BOTTOM
            unsubButton!!.layoutParams = params
            content.addView(unsubButton)
        }
        unsubButton!!.setCount(value)
    }

    fun closeUnsubButton() {
        content.removeView(unsubButton)
        unsubButton = null
    }

    fun openCommunityView(group: Group) {
        isOpenShareView = true
        popUpContainer = PopUpContainerView(this)
        popUpContainer!!.init(group)
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT
        )
        popUpContainer!!.layoutParams = layoutParams
        content.addView(popUpContainer)
        popUpContainer!!.appear()
    }

    fun closeShareView() {
        content.removeView(popUpContainer)
        popUpContainer = null
    }

    fun setUpStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            changeStatusBarColor(ContextCompat.getColor(this, R.color.colorTextDark))
        else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            changeStatusBarColor(ContextCompat.getColor(this, R.color.colorBackground))
        }
    }

    fun setPopUpViewStatusBar() {
        window.decorView.systemUiVisibility = 0
        changeStatusBarColor(ContextCompat.getColor(this, R.color.colorShareStatus))
    }

    private fun changeStatusBarColor(newColor: Int) {
        val animator = ValueAnimator.ofArgb(window.statusBarColor, newColor)
        animator.addUpdateListener {
            window.statusBarColor = it.animatedValue as Int
        }
        animator.duration = 150
        animator.start()
    }

    override fun onBackPressed() {
        if (isOpenShareView) {
            isOpenShareView = false
            popUpContainer!!.closeShareView()
        } else
            super.onBackPressed()
    }

    fun openWebPage(name: String) {
        val url = "https://vk.com/$name"
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                resources.getString(R.string.community_open_failed),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

}
