package com.sobol.f_unsubscribe_app

import com.sobol.f_unsubscribe_app.api.LeaveGroupRequest
import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.ui.UnsubscribeActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.exceptions.VKApiExecutionException

class Database {

    private object Holder {
        val INSTANCE = Database()
    }

    companion object {
        val instance: Database by lazy { Holder.INSTANCE }
    }

    private val unsubCommunities = ArrayList<Group>()


    fun addUnsubCommuniy(group: Group) {
        unsubCommunities.add(group)
    }

    fun removeUnsubCommunity(group: Group) {
        unsubCommunities.remove(group)
    }

    fun getUnsubCommunitites(): ArrayList<Group> {
        return unsubCommunities
    }

    fun leaveGroup(activity: UnsubscribeActivity) {
        activity.showWaitingView()
        for (i in 0 until unsubCommunities.size) {
            VK.execute(LeaveGroupRequest(unsubCommunities[i].id), object : VKApiCallback<Int> {
                override fun success(result: Int) {
                    if (i == unsubCommunities.size-1) {
                        unsubCommunities.clear()
                        activity.updateRecyclerViewMargin(0)
                        activity.closeUnsubButton()
                        activity.requestGroups()
                    }
                }

                override fun fail(error: VKApiExecutionException) {
                    if (i == unsubCommunities.size-1) {
                        unsubCommunities.clear()
                        activity.updateRecyclerViewMargin(0)
                        activity.closeUnsubButton()
                        activity.requestGroups()
                    }
                    println(error.detailMessage)
                }
            })
        }
    }

}