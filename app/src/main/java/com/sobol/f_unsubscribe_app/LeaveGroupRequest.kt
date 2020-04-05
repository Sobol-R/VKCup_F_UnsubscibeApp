package com.sobol.f_unsubscribe_app

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class LeaveGroupRequest(gid: Int): VKRequest<Int>("groups.leave") {

    init {
        addParam("group_id", gid)
    }

    override fun parse(r: JSONObject): Int {
        return r.getInt("count")
    }
}