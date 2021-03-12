package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator
import `fun`.fifu.fifusky.operators.SkyOperator.buildIsLand
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.data.PlayerData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

/**
 * 玩家命令
 */
class SkyCommand : CommandExecutor {
    private val HelpMassage = mapOf(
        "get" to "/get <SkyLoc> 领取一个岛屿，两个月只能领一次"
    )

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("你必须是一名玩家")
            return true
        }
        if (p3.isNullOrEmpty()) return onS(p0, p3)
        return when (p3[0]) {
            "get" -> onGet(p0, p3)
            "help" -> onHelp(p0)
            else -> false
        }
    }

    private fun onGet(player: Player, p3: Array<out String>): Boolean {
        if (p3[1].isEmpty()) player.sendMessage(HelpMassage["get"]!!)
        if (!SkyOperator.canGet(player).first) {
            player.sendMessage("每两个月只能领取一次岛，${SkyOperator.canGet(player).second}后可再次领取")
            return true
        }
        val s = p3[1]
        val isLand = Sky.getIsLand(s)
        if (SkyOperator.isUnclaimed(isLand)) {
            buildIsLand(isLand)
            SkyOperator.addOwener(isLand, player)
            SkyOperator.tpIsLand(player, isLand)
        } else {
            player.sendMessage("岛屿 $isLand 已经有人领过了，主人是${SkyOperator.getOwnersList(isLand)}")
        }
        return true
    }


    private fun onHelp(player: Player): Boolean {
        val sb = StringBuffer()
        HelpMassage.values.forEach { sb.append(it).append("\n") }
        player.sendMessage("帮助：/s <命令>\n$sb")
        return true
    }

    fun onS(p0: Player, p3: Array<out String>): Boolean {
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