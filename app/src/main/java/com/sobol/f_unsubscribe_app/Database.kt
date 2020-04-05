package com.sobol.f_unsubscribe_app

import com.sobol.f_unsubscribe_app.model.Group

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

}