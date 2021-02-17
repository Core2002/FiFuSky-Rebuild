package `fun`.fifu.fifusky.data

import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import cn.hutool.db.Db
import cn.hutool.db.Entity
import cn.hutool.json.JSONArray
import cn.hutool.json.JSONUtil
import java.util.*

/**
 * 用来管理数据库的单例
 * @author NekokeCore
 */
object Dataer {

//    private const val base = "plugins/FiFuSky/"
//
//    private const val date = "$base/date/"
//
//    private const val config = base + "config.json"
//    private const val playersIP = base + "playerIP.json"
//    private const val playersName = base + "playerName.json"
//    private const val playersIndex = base + "playerIndex.json"
//    private const val playersHome = base + "playerHome.json"
//    private const val allowExplosionsChunk = base + "allowExplosionChunk.json"
//
//    private const val config = "Config"
//    private const val playersIP = "PlayerIP"
//    private const val playersName = "PlayerUUID-Name"
//    private const val playersIndex = "PlayerIndex"
//    private const val playersHome = "PlayerHome"
//    private const val allowExplosionsChunk = "AllowExplosionChunk"

//    object FileCache : LFUFileCache(1024 * 1024, 1024 * 1024, DateUnit.SECOND.millis * 5)

    private const val ChunkData = "ChunkData"
    private const val PlayerData = "PlayerData"
    private const val SkyIsLand = "SkyIsLand"


    /**
     * 获取指定的岛屿的岛屿信息
     * @param isLand 岛屿
     * @return 岛屿信息
     */
    fun getIsLandData(isLand: IsLand): IsLandData {
        val temp = Db.use().findAll(Entity.create(SkyIsLand).set("SkyLoc", isLand.toString()))
        return if (temp.isNotEmpty()) {
            val ownerPlayersData = getPlayersData(temp[0].getStr("OwnersList").toString())
            val memberPlayersData = getPlayersData(temp[0].getStr("MembersList").toString())

            val privilegeData = PrivilegeData(isLand, ownerPlayersData, memberPlayersData)

            IsLandData(isLand, privilegeData)
        } else {
            IsLandData(isLand, PrivilegeData(isLand, ArrayList(), ArrayList()))
        }


    }

    /**
     * 把玩家主岛存入数据库
     * @param playerUUID 玩家UUID
     * @param playerIsLand 玩家主岛
     */
    fun savePlayerIndex(playerUUID: String, playerIsLand: String) {
//        val string = String(FileCache.getFileBytes(playersIndex))
//        val dataObj = JSONUtil.parseObj(string)
//
//        dataObj[playerUUID] = playerIsLand
//
//        File(playersIndex).writeText(dataObj.toJSONString(4))


        if (Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID)).isEmpty()) {
            Db.use().insert(
                Entity.create(PlayerData)
                    .set("UUID", playerUUID)
                    .set("IndexSkyLoc", playerIsLand)
            )
        } else {
            Db.use().update(
                Entity.create().set("IndexSkyLoc", playerIsLand),
                Entity.create(PlayerData).set("UUID", playerUUID)
            )
        }

    }


    /**
     * 从数据文件获取玩家的主岛
     * @param playerUUID 玩家UUID
     * @return 玩家主岛
     */
    fun getPlayerIndex(playerUUID: String): IsLand {
//        val string = String(FileCache.getFileBytes(playersIndex))
//        val dataObj = JSONUtil.parseObj(string)
//
//        return Sky.getIsLand(dataObj[playerUUID].toString())

        val temp = Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID))
        if (temp.isNotEmpty()) {
            return Sky.getIsLand(temp[0].getStr("IndexSkyLoc"))
        } else {
            throw RuntimeException("查询失败  --  $playerUUID")
        }

    }


    /**
     * 把玩家的ip存入数据库
     * @param playerUUID 玩家UUID
     * @param playerIP 玩家IP
     */
    fun savePlayerIp(playerUUID: String, playerIP: String) {
//        val string = String(FileCache.getFileBytes(playersIP))
//        val dataObj = JSONUtil.parseObj(string)
//
//        val ips = getPlayerIp(playerUUID)
//        if (playerIP !in ips)
//            ips.add(playerIP)
//
//        dataObj[playerUUID] = ips
//
//        File(playersIP).writeText(dataObj.toJSONString(4))

        if (Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID)).isEmpty()) {
            val jsArr = JSONUtil.createArray()
            jsArr.add(playerIP)
            Db.use().insert(
                Entity.create(PlayerData)
                    .set("UUID", playerUUID)
                    .set("IPList", jsArr)
            )
        } else {
            val playerIps = getPlayerIp(playerUUID)
            if (playerIP !in playerIps)
                playerIps.add(playerIP)
            Db.use().update(
                Entity.create().set("IPList", playerIps),
                Entity.create(PlayerData).set("UUID", playerUUID)
            )
        }

    }


    /**
     * 从数据库获取玩家IP列表
     * @param playerUUID 玩家UUID
     * @return 玩家IP列表
     */
    fun getPlayerIp(playerUUID: String): JSONArray {
//        val string = String(FileCache.getFileBytes(playersIP))
//        val dataObj = JSONUtil.parseObj(string)
//        var ip = JSONArray()
//        dataObj.forEach { uuid, v ->
//            if (uuid == playerUUID) {
//                ip = v as JSONArray
//                return@forEach
//            }
//        }
//        return ip

        val temp = Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID))
        if (temp.isNotEmpty()) {
            val jsArrStr = temp[0].getStr("IPList")
            val jsArr = JSONUtil.parseArray(jsArrStr)
            if (jsArr.isNotEmpty())
                return jsArr
        }
        return JSONUtil.createArray()
    }

    /**
     * 把存放玩家信息的JSONArray字符串换成玩家信息列表
     * @param jsArrStr 把存放玩家信息的JSONArray字符串
     * @return 指定岛屿文件的JSONObject的指定类别的玩家信息列表
     */
    private fun getPlayersData(jsArrStr: String): ArrayList<PlayerData> {
        val contentsPlayersData = ArrayList<PlayerData>()
        val playerUuids = JSONUtil.parseArray(jsArrStr)
        playerUuids.forEach {
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
//        val string = String(FileCache.getFileBytes(playersName))
//        val dataObj = JSONUtil.parseObj(string)
//        if (dataObj != null) {
//            val any = dataObj[playerUUID]
//            if (any != null)
//                return any as String
//        }
//        return playerUUID


        val temp = Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID))
        return if (temp.isNotEmpty()) {
            temp[0].getStr("Name")
        } else {
            playerUUID
        }
    }

    /**
     * 根据玩家名返回玩家UUID，若查询不到则返回玩家名
     * @param playerName 玩家名
     * @return 玩家UUID
     */
    fun getPlayerUuid(playerName: String): String {
//
//        val string = String(FileCache.getFileBytes(playersName))
//        val dataObj = JSONUtil.parseObj(string)
//        if (dataObj != null) {
//            val uuids = dataObj.keys
//            uuids.forEach {
//                if (it is String && playerName == dataObj[it])
//                    return it
//            }
//        }


        val temp = Db.use().findAll(Entity.create(PlayerData).set("Name", playerName))
        return if (temp.isNotEmpty()) {
            temp[0].getStr("UUID")
        } else {
            playerName
        }
    }

    /**
     * 存储玩家的UUID-Name数据
     * @param playerUUID 玩家UUID
     * @param playerName 玩家名
     */
    fun savePlayerName(playerUUID: String, playerName: String) {
        if (Db.use().findAll(Entity.create(PlayerData).set("UUID", playerUUID)).isEmpty()) {
            Db.use().insert(
                Entity.create(PlayerData)
                    .set("UUID", playerUUID)
                    .set("Name", playerName)
            )
        } else {
            Db.use().update(
                Entity.create().set("Name", playerName),
                Entity.create(PlayerData).set("UUID", playerUUID)
            )
        }
    }

    /**
     * 把IslandData保存到数据库
     * @param isLandData 岛屿数据
     */
    fun saveIslandData(isLandData: IsLandData) {
        val skyLoc = isLandData.IsLand.toString()

        val membersList = JSONUtil.createArray()
        val ownersList = JSONUtil.createArray()
        isLandData.Privilege.Owner.forEach {
            ownersList.add(it.UUID)
            savePlayerName(it.UUID,it.LastName)
        }
        isLandData.Privilege.Member.forEach {
            membersList.add(it.UUID)
            savePlayerName(it.UUID,it.LastName)
        }

        if (Db.use().findAll(Entity.create(SkyIsLand).set("SkyLoc", skyLoc)).isEmpty()) {
            Db.use().insert(
                Entity.create(SkyIsLand)
                    .set("SkyLoc", skyLoc)
                    .set("OwnersList", ownersList.toString())
                    .set("MembersList", membersList.toString())
            )
        } else {
            Db.use().update(
                Entity.create()
                    .set("SkyLoc", skyLoc)
                    .set("OwnersList", ownersList.toString())
                    .set("MembersList", membersList.toString()),
                Entity.create(SkyIsLand).set("SkyLoc", skyLoc)
            )
        }
    }
}