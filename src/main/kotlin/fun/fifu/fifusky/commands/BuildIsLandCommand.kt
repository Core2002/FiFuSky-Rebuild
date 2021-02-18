package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.SkyOperator.BuildIsLand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class BuildIsLandCommand : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        BuildIsLand(Sky.getIsLand(p3[0]))
        return true
    }
}