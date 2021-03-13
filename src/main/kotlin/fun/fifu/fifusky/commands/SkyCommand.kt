package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator
import `fun`.fifu.fifusky.operators.SkyOperator.buildIsLand
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.data.PlayerData
import `fun`.fifu.fifusky.operators.SoundPlayer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.random.Random

/**
 * 玩家命令
 */
class SkyCommand : CommandExecutor {
    private val helpMassage = mapOf(
        "help" to "/s help <命令> 查看帮助",
        "get" to "/s get <SkyLoc> 领取一个岛屿，两个月只能领一次",
        "info" to "/s info 查询当前岛屿信息",
        "homes" to "/s homes 查询你有权限的岛屿",
        "go" to "/s go <SkyLoc> 传送到目标岛屿"
    )

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("你必须是一名玩家")
            return true
        }
        if (p3.isNullOrEmpty()) return onS(p0)
        try {
            val re = when (p3[0]) {
                "help" -> onHelp(p0, p3)
                "get" -> onGet(p0, p3)
                "info" -> onInfo(p0)
                "homes" -> onHomes(p0)
                "go" -> onGo(p0, p3)
                else -> false
            }
            if (!re) onHelp(p0, arrayOf("help", p3[0]))
        } catch (e: Exception) {
            onHelp(p0, arrayOf("help", p3[0]))
            FiFuSky.fs.logger.warning("$p0 的命令 /s ${p3.contentToString()} 导致了一个异常： ${e.localizedMessage}")
            return false
        }
        return true
    }

    private fun onGo(p0: Player, p3: Array<out String>): Boolean {
        if (p3.size == 1) return false
        val isLand = Sky.getIsLand(p3[1])
        if (SkyOperator.isUnclaimed(isLand)) {
            p0.sendMessage("没有 $isLand 这个岛屿")
            return true
        } else {
            SkyOperator.tpIsLand(p0, isLand)
        }
        return true
    }

    private fun onHomes(p0: Player): Boolean {
        val isLand = Sky.getIsLand(p0.location.blockX, p0.location.blockZ)
        val homes = SkyOperator.getHomes(p0)
        val homeInfo = """
            ${p0.name} 现在所在的岛屿是 $isLand
            你拥有的岛屿有：
            ${homes.first}
            你加入的岛屿有：
            ${homes.second}
        """.trimIndent()
        p0.sendMessage(homeInfo)
        return true
    }

    private fun onInfo(p0: Player): Boolean {
        val isLand = Sky.getIsLand(p0.location.blockX, p0.location.blockZ)
        val info = """
            ${p0.name} 现在所在的岛屿是 $isLand
            该岛屿的主人有：
            ${SkyOperator.getOwnersList(isLand)}
            该岛屿的成员有：
            ${SkyOperator.getMembersList(isLand)}
            您在此岛屿 ${if (SkyOperator.havePermission(p0)) "有" else "没有"} 权限
        """.trimIndent()
        p0.sendMessage(info)
        return true
    }

    private fun onGet(player: Player, p3: Array<out String>): Boolean {
        if (p3.size == 1) return false
        if (!SkyOperator.canGet(player).first) {
            player.sendMessage("每两个月只能领取一次岛，${SkyOperator.canGet(player).second}后可再次领取")
            return true
        }
        val isLand = Sky.getIsLand(p3[1])
        if (SkyOperator.isUnclaimed(isLand)) {
            buildIsLand(isLand)
            SkyOperator.addOwener(isLand, player)
            SkyOperator.playerGetOver(player)
            SkyOperator.tpIsLand(player, isLand)
        } else {
            player.sendMessage("岛屿 $isLand 已经有人领过了，主人是${SkyOperator.getOwnersList(isLand)}")
        }
        return true
    }


    private fun onHelp(player: Player, p3: Array<out String>): Boolean {
        if (p3.size == 1) {
            val sb = StringBuffer()
            helpMassage.values.forEach { sb.append(it).append("\n") }
            player.sendMessage("帮助：/s <命令>\n$sb")
            return true
        } else {
            helpMassage[p3[1]]?.let { player.sendMessage(it) }
        }
        return true
    }

    private fun onS(p0: Player): Boolean {
        val isLand: IsLand = try {
            SQLiteer.getPlayerIndex(p0.uniqueId.toString())
        } catch (e: RuntimeException) {
            var temp: IsLand
            do {
                val xx = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                val zz = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                temp = Sky.getIsLand(xx, zz)
            } while (!SQLiteer.getIsLandData(temp).Privilege.Owner.isNullOrEmpty())

            buildIsLand(temp)

            val iLD = SQLiteer.getIsLandData(temp)
            iLD.Privilege.Owner.add(PlayerData(p0.uniqueId.toString(), p0.name))
            SQLiteer.saveIslandData(iLD)
            SQLiteer.savePlayerIndex(p0.uniqueId.toString(), temp.toString())
            SkyOperator.playerGetOver(p0)
            temp
        }

        // 如果玩家现在在主城，就回自己岛，不在主城就回主城
        if (Sky.isInIsLand(p0.location.blockX, p0.location.blockZ, Sky.getIsLand("(0,0)"))) {
            p0.sendMessage("欢迎回家")
            SkyOperator.tpIsLand(p0, isLand)
        } else {
            p0.sendMessage("欢迎回到主城")
            p0.teleport(
                Location(
                    Bukkit.getWorld("world"),
                    8.0,
                    4.0,
                    8.0
                )
            )
        }
        SoundPlayer.playCat(p0)
        return true
    }

}