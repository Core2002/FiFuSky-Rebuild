package `fun`.fifu.fifusky.data

import `fun`.fifu.fifusky.IsLand
import cn.hutool.cache.file.LFUFileCache
import cn.hutool.core.date.DateUnit
import cn.hutool.json.JSONArray
import cn.hutool.json.JSONObject
import cn.hutool.json.JSONUtil
import java.io.File
import java.util.*

/**
 * 用来管理数据库的单例
 * @author NekokeCore
 */
object Dataer {
    object FileCache : LFUFileCache(1024 * 1024, 1024 * 1024, DateUnit.MINUTE.millis)

    private const val base = "plugins/FiFuSky/"

    private const val config = base + "config.json"
    private const val playersIP = base + "playerIP.json"
    private const val playersName = base + "playerName.json"
    private const val playersIndex = base + "playerIndex.json"
    private const val playersHome = base + "playerHome.json"
    private const val allowExplosionsChunk = base + "allowExplosionChunk.json"

    private const val date = "$base/date/"

    /**
     * 获取指定的岛屿的岛屿信息
     * @param isLand 岛屿
     * @return 岛屿信息
     */
    fun getIsLandData(isLand: IsLand): IsLandData {
        val file = File("$date$isLand.json")
        if (!file.exists())
            return IsLandData(isLand, PrivilegeData(isLand, ArrayList(), ArrayList()))

        val string = String(FileCache.getFileBytes(file))
        val dataObj = JSONUtil.parseObj(string)

        val ownerPlayersData = getPlayersData(dataObj, "Owners")
        val memberPlayersData = getPlayersData(dataObj, "Members")

        val privilegeData = PrivilegeData(isLand, ownerPlayersData, memberPlayersData)
        return IsLandData(isLand, privilegeData)
    }

    /**
     * 获取指定岛屿文件的JSONObject的指定类别的玩家信息列表
     * @param dataObj 岛屿Json文件的JSONObject
     * @param content 要获取的玩家信息的类型，比如Owners/Members
     * @return 指定岛屿文件的JSONObject的指定类别的玩家信息列表
     */
    private fun getPlayersData(dataObj: JSONObject, content: String): ArrayList<PlayerData> {
        val contentsPlayersData = ArrayList<PlayerData>()
        val contentsUuid = dataObj[content] as JSONArray
        contentsUuid.forEach {
            if (it is String) {
                contentsPlayersData.add(PlayerData(it, getPlayerName(it)))
            }
        }
        return contentsPlayersData
    }

    /**
     * 根据UUID返回玩家名，若查询不到则返回玩家UUID
     * @param playerUUID 玩家UUID
     * @return 玩家名
     */
    fun getPlayerName(playerUUID: String): String {
        val string = String(FileCache.getFileBytes(playersName))
        val dataObj = JSONUtil.parseObj(string)
        if (dataObj != null) {
            val any = dataObj[playerUUID]
            if (any != null)
                return any as String
        }
        return playerUUID
    }

    /**
     * 根据玩家名返回玩家UUID，若查询不到则返回玩家名
     * @param playerName 玩家名
     * @return 玩家UUID
     */
    fun getPlayerUuid(playerName: String): String {
        val string = String(FileCache.getFileBytes(playersName))
        val dataObj = JSONUtil.parseObj(string)
        if (dataObj != null) {
            val uuids = dataObj.keys
            uuids.forEach {
                if (it is String && playerName == dataObj[it])
                    return it
            }
        }
        return playerName
    }

}