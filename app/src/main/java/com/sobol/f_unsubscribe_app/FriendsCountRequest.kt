package com.sobol.f_unsubscribe_app

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class FriendsCountRequest(gid: Int): VKRequest<Int>("groups.getMembers") {

    init {
        addParam("group_id", gid)
        addParam("count", 0)
        addParam("filter", "friends")
    }

    override fun parse(r: JSONObject): Int {
        return r.getJSONObject("response").getInt("count")
    }

}