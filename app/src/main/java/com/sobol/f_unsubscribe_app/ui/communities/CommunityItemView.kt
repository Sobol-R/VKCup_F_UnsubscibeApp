package com.sobol.f_unsubscribe_app.ui.communities

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.sobol.f_unsubscribe_app.AndroidUtils
import com.sobol.f_unsubscribe_app.Database
import com.sobol.f_unsubscribe_app.R
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity
import com.sobol.f_unsubscribe_app.ui.UnsubscribeButton
import kotlinx.android.synthetic.main.community_item.view.*

class CommunityItemView(
    context: Context
): FrameLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.community_item, this, true)
    }

    fun setUp(group: Group) {
        setSize()

        val activity = context as UnsubscribeActivity

        if (group.checked)
            setCheckedStyle()
        else
            setUnCheckedStyle(true)

        setOnLongClickListener {
            activity.openCommunityView(group)
            true
        }

        setOnClickListener {
            if (group.checked) {
                group.checked = false
                setUnCheckedStyle(false)
                Database.instance.removeUnsubCommunity(group)
                activity.setUnsubButton(UnsubscribeButton.SetValue.DELETE)
            }
            else {
                group.checked = true
                setCheckedStyle()
                Database.instance.addUnsubCommuniy(group)
                activity.setUnsubButton(UnsubscribeButton.SetValue.ADD)
            }
        }

        if (!TextUtils.isEmpty(group.photo)) {
            Glide
                .with(activity)
                .load(group.photo)
                .into(icon)
        }

        title.text = group.name
    }

    private fun setCheckedStyle() {
        item_selected.reveal()
    }

    private fun setUnCheckedStyle(instant: Boolean) {
        item_selected.dismiss(instant)
    }

    private fun setSize() {
        val activity = context as UnsubscribeActivity
        val layoutParams = image_content.layoutParams as LinearLayout.LayoutParams
        val startMargin = AndroidUtils.dpToPx(activity, 15f)
        val size = AndroidUtils.getScreenWidth(activity) / 3 - startMargin * 1.5
        layoutParams.width = size.toInt()
        layoutParams.height = size.toInt()
        card.radius = (size.toInt() - AndroidUtils.dpToPx(activity, 8f)) / 2f
        image_content.layoutParams = layoutParams
    }

}