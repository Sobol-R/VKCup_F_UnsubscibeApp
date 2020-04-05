package com.sobol.f_unsubscribe_app.ui.communities

import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.community_item.view.*

class CommunityItemHolder(
    itemView: CommunityItemView
) : RecyclerView.ViewHolder(itemView) {

    val communityView = itemView
    val imageContent = itemView.image_content
    val card = itemView.card
    val icon = itemView.icon
    val selectedItem = itemView.item_selected

}