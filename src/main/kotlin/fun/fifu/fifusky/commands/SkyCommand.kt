package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.IsLand
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.Dataer
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

        var isLand: IsLand = try {
            Dataer.getPlayerIndex(p0.uniqueId.toString())
        } catch (e: RuntimeException) {
            var temp: IsLand
            do {
                val xx = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                val zz = Random.nextInt(-Sky.MAX_ISLAND * Sky.SIDE, Sky.MAX_ISLAND * Sky.SIDE + 1)
                temp = Dataer.getPlayerIndex("($xx,$zz)")
            } while (Dataer.getIsLandData(temp).Privilege.Owner.isNullOrEmpty())

            temp
        }



        return true
    }

}