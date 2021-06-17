package `fun`.fifu.fifusky.operators

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.Island
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.IslandData
import `fun`.fifu.fifusky.data.Jsoner
import `fun`.fifu.fifusky.data.PlayerData
import `fun`.fifu.fifusky.data.SQLiteer
import cn.hutool.core.date.DateUtil
import org.apache.commons.lang.time.DateUtils
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

/**
 * 岛屿操作者单例，内聚了很多操作以及方法扩展
 * @author NekokeCore
 */
object SkyOperator {

    /**
     * 把玩家传送至某岛屿
     * @param island 岛屿
     */
    fun Player.tpIsland(island: Island) {
        val isLandCenter = Sky.getIslandCenter(island)
        teleport(
            Location(
                Bukkit.getWorld("world"),
                isLandCenter.first.toDouble(),
                65.0,
                isLandCenter.second.toDouble(),
                location.yaw,
                location.pitch
            )
        )
        sendTitle(island.toString(), "主人 ${island.getOwnersList()}", 2, 20 * 3, 6)
        SoundPlayer.playCat(this)
    }

    /**
     * 构建一个岛屿，将模板岛屿复制到目标岛屿
     */
    fun Island.build() {
        val t = System.currentTimeMillis()
        //准备工作
        val ic = Sky.getIslandCenter(this)
        val world = Bukkit.getWorld(Sky.WORLD)

        //原点偏移
        val xx = -3
        val yy = -4
        val zz = -1
        //顶点1
        val x1 = 508
        val y1 = 60
        val z1 = 510
        //顶点2
        val x2 = 515
        val y2 = 69
        val z2 = 516
        //目标原点
        val xxx = X + Sky.SIDE / 2 + xx.toDouble()
        val yyy = 64 + yy.toDouble()
        val zzz = Y + Sky.SIDE / 2 + zz.toDouble()
        //生成执行命令
        val command = "clone $x1 $y1 $z1 $x2 $y2 $z2 ${xxx.toInt()} ${yyy.toInt()} ${zzz.toInt()}"
        //自动加载区块
        FiFuSky.fs.logger.info(
            "chunk.load:" + world!!.getChunkAt(Location(world, xxx, yyy, zzz))
                .load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx + 16), yyy,
                    zzz
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx - 16), yyy,
                    zzz
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, xxx, yyy,
                    (zzz + 16)
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, xxx, yyy,
                    (zzz - 16)
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx + 16), yyy,
                    (zzz + 16)
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx - 16), yyy,
                    (zzz - 16)
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx + 16), yyy,
                    (zzz + 16)
                )
            ).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk.load:" + world.getChunkAt(
                Location(
                    world, (xxx - 16), yyy,
                    (zzz - 16)
                )
            ).load(true)
        )

        FiFuSky.fs.logger.info("chunk0.load:" + world.getChunkAt(Location(world, 511.0, 64.0, 511.0)).load(true))
        FiFuSky.fs.logger.info("chunk0.load:" + world.getChunkAt(Location(world, 511.0 + 16, 64.0, 511.0)).load(true))
        FiFuSky.fs.logger.info("chunk0.load:" + world.getChunkAt(Location(world, 511.0 - 16, 64.0, 511.0)).load(true))
        FiFuSky.fs.logger.info("chunk0.load:" + world.getChunkAt(Location(world, 511.0, 64.0, 511.0 + 16)).load(true))
        FiFuSky.fs.logger.info("chunk0.load:" + world.getChunkAt(Location(world, 511.0, 64.0, 511.0 - 16)).load(true))
        FiFuSky.fs.logger.info(
            "chunk0.load:" + world.getChunkAt(Location(world, 511.0 + 16, 64.0, 511.0 + 16)).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk0.load:" + world.getChunkAt(Location(world, 511.0 - 16, 64.0, 511.0 - 16)).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk0.load:" + world.getChunkAt(Location(world, 511.0 + 16, 64.0, 511.0 + 16)).load(true)
        )
        FiFuSky.fs.logger.info(
            "chunk0.load:" + world.getChunkAt(Location(world, 511.0 - 16, 64.0, 511.0 - 16)).load(true)
        )
        //开始拷贝初始空岛
        Bukkit.getScheduler().runTask(FiFuSky.fs, Runnable {
            FiFuSky.fs.logger.info("开始拷贝初始空岛:$command")
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command)
            world.getBlockAt(ic.first, 60, ic.second).setType(Material.BEDROCK, true)
            FiFuSky.fs.logger.info("复制完毕！$this 耗时 ${System.currentTimeMillis() - t} ms。")
        })
/*
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
*/
        FiFuSky.fs.logger.info("调度完毕！")
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
     * @return 是否有权限
     */
    fun Player.havePermission(): Boolean {
        if (!location.world.isSkyWorld())
            return true
        if (gameMode == GameMode.SPECTATOR)
            return true
        val island = location.getIsland()
        if (isOwnedIsland(island) || isJoinedIsland(island))
            return true

        return false
    }

    /**
     * 查询区块是否允许爆炸
     * @return 该区块是否允许爆炸
     */
    fun canExplosion(chunckLoc: String): Boolean = SQLiteer.getChunkData(chunckLoc).AllowExplosion


    /**
     * 判断世界是否是空岛世界
     * @return 世界是否是空岛世界
     */
    fun World.isSkyWorld() = name == Sky.WORLD

    /**
     * 显示一个实体的血量给玩家
     * @param player 要显示的玩家
     */
    fun LivingEntity.showDamage(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                val i = health.toInt()
                val j = getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value.toInt()
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
                player.sendTitle("", "$color$name->HP:$i/$j", 2, 20, 6)
            }
        }.runTaskLater(FiFuSky.fs, 1)
    }

    /**
     * 检查岛屿是否是无人认领的
     * @return 是否无人认领
     */
    fun Island.isUnclaimed(): Boolean {
        val privilege = SQLiteer.getIsLandData(this).Privilege
        if (privilege.Owner.isNullOrEmpty())
            return true
        return false
    }

    /**
     * 获取岛屿的主人列表
     * @return 目标岛屿的主人列表
     */
    fun Island.getOwnersList(u: Boolean = false): String {
        val sb = StringBuilder()
        val owner = SQLiteer.getIsLandData(this).Privilege.Owner
        owner.forEach {
            if (u) {
                sb.append(it.UUID).append(' ')
            } else {
                sb.append(it.LastName).append(' ')
            }
        }
        return sb.toString()
    }

    /**
     * 获取岛屿的成员列表
     * @return 目标岛屿的成员列表
     */
    fun Island.getMembersList(u: Boolean = false): String {
        val sb = StringBuilder()
        val owner = SQLiteer.getIsLandData(this).Privilege.Member
        owner.forEach {
            if (u) {
                sb.append(it.UUID).append(' ')
            } else {
                sb.append(it.LastName).append(' ')
            }
        }
        return sb.toString()
    }

    /**
     * 获取岛屿的信息
     * @return IslandData
     */
    fun Island.getIslandData() = SQLiteer.getIsLandData(this)

    /**
     * 获取坐标所在的岛屿
     * @return 坐标所在的岛屿
     */
    fun Location.getIsland() = Sky.getIsland(blockX, blockZ)

    /**
     * 给目标岛屿添加一位主人
     * @param player 要添加的主人
     */
    fun Island.addOwner(player: Player) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(this)
        if (player.isOwnedIsland(isLandData.Island))
            return
        isLandData.Privilege.Owner.add(PlayerData(UUID = uuid, LastName = player.name))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 给目标岛屿添加一位成员
     * @param player 要添加的成员
     */
    fun Island.addMember(player: Player) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(this)
        if (player.isJoinedIsland(isLandData.Island))
            return
        isLandData.Privilege.Member.add(PlayerData(UUID = uuid, LastName = player.name))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 从目标岛屿删除一位成员
     * @param player 要移除的成员
     */
    fun Island.removeMember(player: Player) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(this)
        if (!player.isJoinedIsland(isLandData.Island))
            return
        isLandData.Privilege.Member.remove(PlayerData(UUID = uuid, LastName = player.name))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 判断一个玩家是否可以领取岛
     * @return 第一个：是否可以领取岛，第二个：什么时间后可以领取
     */
    fun Player.canGetIsland(): Pair<Boolean, String> {
        val uuid = uniqueId.toString()
        val time = System.currentTimeMillis() - Jsoner.getPlayerLastGet(uuid)
        val lgy = DateUtils.MILLIS_PER_DAY * 30 * 2
        return Pair(time > lgy, DateUtil.formatBetween(lgy - time))
    }

    /**
     * 当玩家get岛屿成功后执行该方法
     * @param player 领取完岛屿的玩家
     */
    fun playerGetOver(player: Player, islandData: IslandData) {
        SQLiteer.saveIslandData(islandData)
        SQLiteer.savePlayerIndex(player.uniqueId.toString(), islandData.Island.toString())
        Jsoner.setPlayerLastGet(player.uniqueId.toString(), System.currentTimeMillis())
    }

    /**
     * 获取玩家的岛屿列表
     * @return 第一个：玩家所拥有的岛屿    第二个：玩家所加入的岛屿
     */
    fun Player.getIslandHomes(): Pair<String, String> {
        val sb = StringBuilder()
        val homes = SQLiteer.getHomes(uniqueId.toString())
        homes.first.forEach {
            sb.append(it.Island).append(' ')
        }
        val forOwner: String = sb.toString()
        sb.clear()
        homes.second.forEach {
            sb.append(it.Island).append(' ')
        }
        val forMember: String = sb.toString()
        sb.clear()
        return Pair(forOwner, forMember)
    }

    /**
     * 把玩家主人从岛屿移除
     * @param player 玩家主人
     */
    fun Island.removeOwner(player: Player) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(this)
        if (!player.isOwnedIsland(isLandData.Island))
            return
        val owners = isLandData.Privilege.Owner
        owners.remove(PlayerData(uuid, SQLiteer.getPlayerName(uuid)))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 判断玩家是否拥有岛屿
     * @param island 要检测的岛屿
     * @return 玩家是否是岛屿的所有者
     */
    fun Player.isOwnedIsland(island: Island): Boolean {
        val isLandData = SQLiteer.getIsLandData(island)
        isLandData.Privilege.Owner.forEach {
            if (uniqueId.toString() == it.UUID) return true
        }
        return false
    }

    /**
     * 判断玩家是否是岛屿成员
     * @param island 要检测的岛屿
     * @return 玩家是否是岛屿的成员
     */
    fun Player.isJoinedIsland(island: Island): Boolean {
        val isLandData = SQLiteer.getIsLandData(island)
        isLandData.Privilege.Member.forEach {
            if (uniqueId.toString() == it.UUID) return true
        }
        return false
    }

    /**
     * 获取实体当前所在的岛屿
     * @return 当前实体所在的岛屿
     */
    fun Entity.currentIsland(): Island = Sky.getIsland(location.blockX, location.blockZ)

    /**
     * 获取区块的允许爆炸属性
     * @return 是否允许爆炸
     */
    fun Chunk.getAllowExplosion() = SQLiteer.getChunkData(toChunkLoc()).AllowExplosion

    /**
     * 更改区块的允许爆炸属性
     * @param switch 是否允许爆炸
     */
    fun Chunk.setAllowExplosion(switch: Boolean) {
        val chunkData = SQLiteer.getChunkData(toChunkLoc())
        chunkData.AllowExplosion = switch
        SQLiteer.saveChunkData(chunkData)
    }

    /**
     * 将该区块转换成ChunkLoc
     * @return 该区块的ChunkLoc
     */
    fun Chunk.toChunkLoc() = "[${x},${z}]"


    /**
     * 判断坐标是否在主城
     * @return 坐标是否在主城
     */
    fun Location.inSpawn(): Boolean {
        if (!world.isSkyWorld()) return false
        if (Sky.isInIsland(blockX, blockZ, Sky.SPAWN)) return true
        return false
    }

}