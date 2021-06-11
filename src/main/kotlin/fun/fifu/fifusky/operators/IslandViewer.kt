package `fun`.fifu.fifusky.operators

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.Island
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.operators.SkyOperator.getOwnersList
import `fun`.fifu.fifusky.operators.SkyOperator.tpIsland
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class IslandViewer(private val player: Player) {
    private val canViewIsland: MutableList<Island> = SQLiteer.getAllSkyLoc()

    companion object {
        val viewingPlayer: MutableSet<String> = mutableSetOf()
    }

    fun startView() {
        if (viewingPlayer.contains(player.uniqueId.toString()))
            return
        viewingPlayer.add(player.uniqueId.toString())
        val listIterator = canViewIsland.listIterator()

        Bukkit.getOnlinePlayers().forEach {
            if (player != it)
                it.sendMessage("玩家 ${it.name} 正在参观本服岛屿，赶快输入 /s view 试试吧!")
        }
        object : BukkitRunnable() {
            override fun run() {
                if (!listIterator.hasNext()) {
                    this.cancel()
                    viewingPlayer.remove(player.uniqueId.toString())
                    player.tpIsland(Sky.SPAWN)
                    player.gameMode = GameMode.SURVIVAL
                    player.sendMessage("参观结束")
                    return
                }
                player.gameMode = GameMode.SPECTATOR
                val next = listIterator.next()
                player.tpIsland(next)
                player.sendMessage("${canViewIsland.indexOf(next) + 1} / ${canViewIsland.size} : ${next.SkyLoc} ${next.getOwnersList()}")
            }
        }.runTaskTimer(FiFuSky.fs, 20, 20 * 10)
    }

}