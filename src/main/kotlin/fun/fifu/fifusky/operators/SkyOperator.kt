package `fun`.fifu.fifusky.operators

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
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
import java.lang.StringBuilder
import org.bukkit.Bukkit


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
        val xxx = isLand.X + Sky.SIDE / 2 + xx.toDouble()
        val yyy = 64 + yy.toDouble()
        val zzz = isLand.Y + Sky.SIDE / 2 + zz.toDouble()
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
            world.getBlockAt(ic.first, 64, ic.second).setType(Material.BEDROCK, true)
            FiFuSky.fs.logger.info("复制完毕！$isLand 耗时 ${System.currentTimeMillis() - t} ms。")
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
     * @param isLand 目标岛屿
     * @return 目标岛屿的主人列表
     */
    fun getOwnersList(isLand: IsLand, u: Boolean = false): String {
        val sb = StringBuilder()
        val owner = SQLiteer.getIsLandData(isLand).Privilege.Owner
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
     * @param isLand 目标岛屿
     * @return 目标岛屿的成员列表
     */
    fun getMembersList(isLand: IsLand, u: Boolean = false): String {
        val sb = StringBuilder()
        val owner = SQLiteer.getIsLandData(isLand).Privilege.Member
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
     * 判断一个玩家是否可以领取岛
     * @param player 要判断的玩家
     * @return 第一个：是否可以领取岛，第二个：什么时间后可以领取
     */
    fun canGet(player: Player): Pair<Boolean, String> {
        val uuid = player.uniqueId.toString()
        val time = System.currentTimeMillis() - Jsoner.getPlayerLastGet(uuid)
        val lgy = DateUtils.MILLIS_PER_DAY * 30 * 2
        return Pair(time > lgy, DateUtil.formatBetween(lgy - time))
    }

    /**
     * 当玩家get岛屿成功后执行该方法
     * @param player 领取完岛屿的玩家
     */
    fun playerGetOver(player: Player) = Jsoner.setPlayerLastGet(player.uniqueId.toString(), System.currentTimeMillis())

    /**
     * 获取玩家的岛屿列表
     * @param player 要查询的玩家
     * @return 第一个：玩家所拥有的岛屿    第二个：玩家所加入的岛屿
     */
    fun getHomes(player: Player): Pair<String, String> {
        val sb = StringBuilder()
        val homes = SQLiteer.getHomes(player.uniqueId.toString())
        homes.first.forEach {
            sb.append(it.IsLand).append(' ')
        }
        val forOwner: String = sb.toString()
        sb.clear()
        homes.second.forEach {
            sb.append(it.IsLand).append(' ')
        }
        val forMember: String = sb.toString()
        sb.clear()
        return Pair(forOwner, forMember)
    }

    /**
     * 把玩家主人从岛屿移除
     * @param player 玩家主人
     * @param isLand 要操作的岛屿
     */
    fun removeOwner(player: Player, isLand: IsLand) {
        val uuid = player.uniqueId.toString()
        val isLandData = SQLiteer.getIsLandData(isLand)
        val owners = isLandData.Privilege.Owner
        owners.remove(PlayerData(uuid, SQLiteer.getPlayerName(uuid)))
        SQLiteer.saveIslandData(isLandData)
    }

    /**
     * 判断玩家是否拥有岛屿
     * @param isLand 要检测的岛屿
     * @return 玩家是否是岛屿的所有者
     */
    fun Player.isOwnerIsland(isLand: IsLand): Boolean {
        val isLandData = SQLiteer.getIsLandData(isLand)
        isLandData.Privilege.Owner.forEach {
            if (this.uniqueId.toString() == it.UUID) return true
        }
        return false
    }

    /**
     * 获取实体当前所在的岛屿
     * @return 当前实体所在的岛屿
     */
    fun Entity.currentIsland(): IsLand = Sky.getIsLand(this.location.blockX, this.location.blockZ)

    /**
     * 获取区块的允许爆炸属性
     * @return 是否允许爆炸
     */
    fun Chunk.getAllowExplosion() = SQLiteer.getChunkData(this.toChunkLoc()).AllowExplosion

    /**
     * 更改区块的允许爆炸属性
     * @param switch 是否允许爆炸
     */
    fun Chunk.setAllowExplosion(switch: Boolean) {
        val chunkData = SQLiteer.getChunkData(this.toChunkLoc())
        chunkData.AllowExplosion = switch
        SQLiteer.saveChunkData(chunkData)
    }

    /**
     * 将该区块转换成ChunkLoc
     * @return 该区块的ChunkLoc
     */
    fun Chunk.toChunkLoc() = "[${this.x},${this.z}]"

}