package com.sobol.f_unsubscribe_app

import com.sobol.f_unsubscribe_app.model.Group
import com.sobol.f_unsubscribe_app.model.VKUser
import com.vk.api.sdk.requests.VKRequest
import org.json.JSONObject

class GroupsRequest(uid: Int = 0): VKRequest<List<Group>>("groups.get") {

    init {
        if (uid != 0) {
            addParam("user_id", uid)
        }
        addParam("extended", 1)
        addParam("fields", arrayOf("description", "members_count"))
    }

    override fun parse(r: JSONObject): List<Group> {
        val users = r.getJSONObject("response").getJSONArray("items")
        val result = ArrayList<Group>()
        for (i in 0 until users.length()) {
            result.add(Group.parse(users.getJSONObject(i)))
        }
        return result
    }
}