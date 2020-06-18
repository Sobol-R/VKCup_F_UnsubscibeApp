package com.sobol.f_unsubscribe_app.ui.pop_up_view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.design.widget.CoordinatorLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.R
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity

class OpenButtonView(
    context: Context
) : FrameLayout(context) {

    private lateinit var activity: UnsubscribeActivity

    fun init(screenName: String) {
        LayoutInflater.from(context).inflate(R.layout.open_button_view, this, true)

        activity = context as UnsubscribeActivity

        val params = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.BOTTOM
        layoutParams = params

        setOnClickListener {
            activity.openWebPage(screenName)
        }

        alpha = 0f
    }

    fun appear() {
        animate()
            .alpha(1f)
            .duration = 150
    }

    fun disappear(withDelay: Boolean) {
        if (withDelay)
            Handler(Looper.getMainLooper()).postDelayed({
                close()
            }, 150)
        else
            close()
    }

    private fun close() {
        animate()
            .y(AndroidUtils.getScreenHeight(activity).toFloat())
            .duration = 58
    }

}