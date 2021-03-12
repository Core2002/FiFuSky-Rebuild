package `fun`.fifu.fifusky.operators

import SoundPlayer
import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.Jsoner
import `fun`.fifu.fifusky.data.PlayerData
import `fun`.fifu.fifusky.data.SQLiteer
import cn.hutool.core.date.DateUtil
import org.apache.commons.lang.time.DateUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.block.data.BlockData
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable


object SkyOperator {

    /**
     * 把玩家传送至某岛屿
     * @param player 玩家
     * @param isLand 岛屿
     */
    fun tpIsLand(player: Player, isLand: IsLand) {
        val isLandCenter = Sky.getIsLandCenter(isLand)
        player.teleport(
            Location(
                Bukkit.getWorld("world"),
                isLandCenter.first.toDouble(),
                65.0,
                isLandCenter.second.toDouble()
            )
        )
        SoundPlayer.playCat(player)
    }

    /**
     * 构建一个岛屿，将模板岛屿复制到目标岛屿
     * @param isLand 要构建的目标岛屿
     */
    fun buildIsLand(isLand: IsLand) {
        val t = System.currentTimeMillis()
        //准备工作
        val ic = Sky.getIsLandCenter(isLand)
        val world = Bukkit.getWorld("world")
        world?.getBlockAt(ic.first, 64, ic.second)?.setType(Material.BEDROCK, true)

        //上界
        val sj = Triple(3, 82, -114)
        //下界
        val xj = Triple(12, 70, -120)
        //基址
        val jz = Triple(8, 75, -117)

        //存储模板岛屿偏移
        val ib = HashMap<Triple<Int, Int, Int>, BlockData>()
        for (t in blp(sj, xj)) {
            ib[Triple(
                t.first - jz.first,
                t.second - jz.second,
                t.third - jz.third
            )] = world?.getBlockAt(t.first, t.second, t.third)!!.blockData
        }

        //目标基址
        val mb = Sky.getIsLandCenter(isLand)

        //开始复制
        for ((t, b) in ib) {
            world?.getBlockAt(
                mb.first + t.first,
                64 + t.second,
                mb.second + t.third
            )!!.blockData = b
        }

        FiFuSky.fs.logger.info("复制完毕！$isLand 耗时 ${System.currentTimeMillis() - t} ms。")
    }


    /**
     * 遍历一个三维空间
     * @param sj 空间上界顶点
     * @param xj 空间下界顶点
     * @return 该空间内的点集列表
     */
    fun blp(sj: Triple<Int, Int, Int>, xj: Triple<Int, Int, Int>): ArrayList<Triple<Int, Int, Int>> {
        fun bl(f: Int, c: Int) = if (f <= c) f..c else f downTo c
        val arr = ArrayList<Triple<Int, Int, Int>>()
        for (xx in bl(sj.first, xj.first))
            for (yy in bl(sj.second, xj.second))
                for (zz in bl(sj.third, xj.third))
                    arr.add(Triple(xx, yy, zz))
        return arr
    }

    /**
     * 判断玩家当前是否有权限
     * @param player 要查验权限的玩家
     * @return 是否有权限
     */
    fun havePermission(player: Player): Boolean {
        val location = player.location
        if (!isSkyWorld(location.world))
            return true
        val uuid = player.uniqueId.toString()

        val privilege = SQLiteer.getIsLandData(Sky.getIsLand(location.blockX, location.blockZ)).Privilege
        privilege.Owner.forEach {
            if (uuid == it.UUID)
                return true
        }
        privilege.Member.forEach {
            if (uuid == it.UUID)
                return true
        }

        return false
    }

    /**
     * 查询区块是否允许爆炸
     * @param chunckLoc 区块坐标
     * @return 该区块是否允许爆炸
     */
    fun canExplosion(chunckLoc: String): Boolean = SQLiteer.getChunkData(chunckLoc).AllowExplosion


    /**
     * 判断世界是否是空岛世界
     * @param world 待检测的世界
     * @return 世界是否是空岛世界
     */
    fun isSkyWorld(world: World) = world.name == Sky.WORLD

    /**
     * 显示一个实体的血量给玩家
     * @param player 要显示的玩家
     * @param livingEntity 有血量的实体
     */
    fun showDamage(player: Player, livingEntity: LivingEntity) {
        object : BukkitRunnable() {
            override fun run() {
                val i = livingEntity.health.toInt()
                val j = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value.toInt()
                var color = "§f"
                val c = i / 1.0 / j
                if (c in 0.825..1.0) {
                    color = "§a"
                } else if (c < 0.825 && c >= 0.66) {
                    color = "§2"
                } else if (c < 0.66 && c >= 0.495) {
                    color = "§e"
                } else if (c < 0.495 && c >= 0.33) {
                    color = "§6"
                } else if (c < 0.33 && c >= 0.165) {
                    color = "§c"
                } else if (c < 0.165) {
                    color = "§4"
                }
                player.sendTitle("", color + livingEntity.name + "->HP:" + i + "/" + j, 2, 20, 6)
            }
        }.runTaskLater(FiFuSky.fs, 1)
    }

    /**
     * 检查岛屿是否是无人认领的
     * @param isLand 瑶检查的岛屿
     * @return 是否无人认领
     */
    fun isUnclaimed(isLand: IsLand): Boolean {
        val privilege = SQLiteer.getIsLandData(isLand).Privilege
        if (privilege.Owner.isNullOrEmpty())
            return true
        return false
    }

    /**
     * 获取岛屿的主人列表
     * @param 目标岛屿
     * @return 目标岛屿的主人列表
     */
    fun getOwnersList(isLand: IsLand) {
        val sb = StringBuffer()
        val owner = SQLiteer.getIsLandData(isLand).Privilege.Owner
        owner.forEach {
            sb.append(it.LastName).append(" ")
        }
    }

    /**
     * 给目标岛屿添加一位主人
     * @param isLand 目标岛屿
     * @param player 要添加的主人
     */
    fun addOwener(isLand: IsLand, player: Player) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(isLand)
        isLandData.Privilege.Owner.forEach {
            if (uuid == it.UUID)
                return
        }
        isLandData.Privilege.Owner.add(PlayerData(UUID = uuid, LastName = player.name))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 判断一个文件是否可以领取岛
     * @param player 要判断的玩家
     * @return 第一个：是否可以领取岛，第二个：什么时间后可以领取
     */
    fun canGet(player: Player): Pair<Boolean, String> {
        val uuid = player.uniqueId.toString()
        val time = System.currentTimeMillis() - Jsoner.getPlayerLastGet(uuid)
        return Pair(time > DateUtils.MILLIS_PER_DAY * 30 * 2, DateUtil.formatBetween(-time))
    }
}