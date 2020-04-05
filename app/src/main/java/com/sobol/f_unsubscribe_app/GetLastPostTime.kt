package com.sobol.f_unsubscribe_app

import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GetLastPostTime(gid: Int): VKRequest<Int>("wall.get") {

    init {
        addParam("group_id", -gid)
        addParam("count", 1)
    }

    override fun parse(r: JSONObject): Int {
        return r.getInt("date")
    }

}
