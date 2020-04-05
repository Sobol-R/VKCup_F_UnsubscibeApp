package com.sobol.f_unsubscribe_app.ui.communities

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.sobol.f_unsubscribe_app.model.Community
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity

class CommunityAdapter(
    private val activity: UnsubscribeActivity
) : RecyclerView.Adapter<CommunityItemHolder>() {

    private lateinit var groups: List<Group>
    private lateinit var sortedGroups: ArrayList<Group>

    fun setData(groups: List<Group>) {
        this.groups = groups
        sortedGroups = getSortedGroups()
    }

    private fun getSortedGroups(): ArrayList<Group> {
        val result = ArrayList<Group>()
        for (i in groups.size - 1 downTo 0)
            result.add(groups[i])
        return result
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): CommunityItemHolder {
        return CommunityItemHolder(
            CommunityItemView(
                activity
            )
        )
    }

    override fun onBindViewHolder(holder: CommunityItemHolder, i: Int) {
        val group = sortedGroups[i]
        holder.communityView.setUp(group)
    }

    override fun getItemCount(): Int {
        return groups.size
    }

}