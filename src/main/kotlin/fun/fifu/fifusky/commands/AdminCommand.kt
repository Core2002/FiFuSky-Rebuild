package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator.build
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AdminCommand : CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        println("""
            p0:$p0
            p1:$p1
            p2:$p2
            p3:${p3.contentToString()}
        """.trimIndent())
        if (p0 is Player && !p0.isOp) return true
        if (p3.isNullOrEmpty())
            println("回家")
        return when (p3[0]) {
            "build-island" -> onBuild(p3)
            else -> false
        }

    }

    private fun onBuild(p3: Array<out String>): Boolean {
        Sky.getIsLand(p3[1]).build()
        return true
    }
}