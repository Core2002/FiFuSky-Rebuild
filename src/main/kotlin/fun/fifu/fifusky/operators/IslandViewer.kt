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
    companion object {
        val canViewIsland: MutableList<Island> = SQLiteer.getAllSkyLoc()
        val viewingIndex: MutableMap<String, Int> = mutableMapOf()
    }

    fun startView(index: Int = 1) {
        if (viewingIndex.contains(player.uniqueId.toString())) {
            player.sendMessage("冷却中... ${((viewingIndex[player.uniqueId.toString()]!! + 1.0) / canViewIsland.size) * 100} %")
            return
        }
        viewingIndex[player.uniqueId.toString()] = index
        val listIterator = if (index < canViewIsland.size) {
            canViewIsland.listIterator(index - 1)
        } else {
            canViewIsland.listIterator(0)
        }

        Bukkit.getOnlinePlayers().forEach {
            if (player != it)
                it.sendMessage("玩家 ${it.name} 正在参观本服岛屿，赶快输入 /s view 试试吧!")
        }
        object : BukkitRunnable() {
            override fun run() {
                if (!listIterator.hasNext()) {
                    this.cancel()
                    viewingIndex.remove(player.uniqueId.toString())
                    player.tpIsland(Sky.SPAWN)
                    player.gameMode = GameMode.SURVIVAL
                    player.sendMessage("参观结束")
                    return
                }
                player.gameMode = GameMode.SPECTATOR
                viewingIndex[player.uniqueId.toString()] = listIterator.nextIndex()
                val next = listIterator.next()
                player.tpIsland(next)
                player.sendMessage("${canViewIsland.indexOf(next) + 1} / ${canViewIsland.size} : ${next.SkyLoc} ${next.getOwnersList()}")
            }
        }.runTaskTimer(FiFuSky.fs, 0, 20 * 10)
    }

}