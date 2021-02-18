package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.SkyOperator
import `fun`.fifu.fifusky.SkyOperator.BuildIsLand
import `fun`.fifu.fifusky.data.Dataer
import `fun`.fifu.fifusky.data.PlayerData
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.random.Random

class SkyCommand : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if (p0 !is Player) {
            p0.sendMessage("你必须是一名玩家")
            return true
        }

        val isLand: IsLand = try {
            Dataer.getPlayerIndex(p0.uniqueId.toString())
        } catch (e: RuntimeException) {
            var temp: IsLand
            do {
                val xx = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                val zz = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                temp = Sky.getIsLand(xx, zz)
            } while (!Dataer.getIsLandData(temp).Privilege.Owner.isNullOrEmpty())

            BuildIsLand(temp)

            val iLD = Dataer.getIsLandData(temp)
            iLD.Privilege.Owner.add(PlayerData(p0.uniqueId.toString(), p0.name))
            Dataer.saveIslandData(iLD)
            Dataer.savePlayerIndex(p0.uniqueId.toString(), temp.toString())
            temp
        }

        // 如果玩家现在在主城，就回自己岛，不在主城就回主城
        if (Sky.isInIsLand(Pair(p0.location.blockX, p0.location.blockZ), Sky.getIsLand("(0,0)"))) {
            p0.sendMessage("欢迎回家")
            SkyOperator.TpIsLand(p0, isLand)
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
        return true
    }

}