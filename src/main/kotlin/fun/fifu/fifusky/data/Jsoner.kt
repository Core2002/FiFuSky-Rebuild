package `fun`.fifu.fifusky.data

import cn.hutool.cache.file.LFUFileCache
import cn.hutool.json.JSONUtil
import java.io.File

object Jsoner {
    private val cache = LFUFileCache(1000, 50, 5000)
    private val PlayerLastGet = File("plugins/FiFuSky/PlayerLastGet.json")

    /**
     * 获取玩家上次领取岛的时间
     * @param uuid 玩家的uuid
     * @return 该玩家上次获取岛屿的时间的时间戳
     */
    fun getPlayerLastGet(uuid: String): Long {
        if (!PlayerLastGet.isFile) PlayerLastGet.writeText("{}")
        val obj = JSONUtil.parseObj(cache.getFileBytes(PlayerLastGet).toString())
        if (uuid in obj) {
            return obj[uuid].toString().toLong()
        }
        return 0L
    }

    /**
     * 设置玩家上次领取岛的时间的时间戳
     * @param uuid 玩家的uuid
     * @param time 时间戳
     */
    fun setPlayerLastGet(uuid: String, time: Long) {
        val obj = JSONUtil.parseObj(PlayerLastGet.readText())
        obj[uuid] = time
        PlayerLastGet.writeText(obj.toJSONString(4))
    }
}