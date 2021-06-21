package `fun`.fifu.fifusky.listeners.function

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.Island
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.operators.SkyOperator.getIsland
import `fun`.fifu.fifusky.operators.SkyOperator.getOwnersList
import `fun`.fifu.fifusky.operators.SkyOperator.tpIsland
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.scheduler.BukkitRunnable

/**
 * 参观岛屿模块
 * @author NekokeCore
 */
class ViewIsland : Listener {
    /**
     * 岛屿参观操作者单例，负责带领玩家参观全部的岛屿
     */
    companion object {
        val canViewIsland: MutableList<Island> = SQLiteer.getAllSkyLoc()
        val viewingAllIndex: MutableMap<String, Int> = mutableMapOf()

        /**
         * 带领参观所有的岛屿
         * @param player 要参观岛屿的玩家
         * @param index 从哪里开始参观
         */
        fun startViewAll(player: Player, index: Int = 0) {
            if (viewingAllIndex.contains(player.uniqueId.toString())) {
                player.sendMessage("冷却中... ${((viewingAllIndex[player.uniqueId.toString()]!! + 1.0) / canViewIsland.size) * 100} %")
                return
            }
            viewingAllIndex[player.uniqueId.toString()] = index
            val listIterator = if (index < canViewIsland.size) {
                canViewIsland.listIterator(index)
            } else {
                canViewIsland.listIterator(0)
            }

            Bukkit.getOnlinePlayers().forEach {
                if (player != it)
                    it.sendMessage("玩家 ${player.name} 正在参观本服岛屿，赶快输入 /s view-all 试试吧!")
            }
            object : BukkitRunnable() {
                override fun run() {
                    if (!listIterator.hasNext()) {
                        this.cancel()
                        viewingAllIndex.remove(player.uniqueId.toString())
                        player.tpIsland(Sky.SPAWN)
                        player.gameMode = GameMode.SURVIVAL
                        player.sendMessage("参观结束")
                        return
                    }
                    player.gameMode = GameMode.SPECTATOR
                    viewingAllIndex[player.uniqueId.toString()] = listIterator.nextIndex()
                    val next = listIterator.next()
                    player.tpIsland(next)
                    player.sendMessage("${canViewIsland.indexOf(next)} / ${canViewIsland.indices.last} : ${next.SkyLoc} ${next.getOwnersList()}")
                }
            }.runTaskTimer(FiFuSky.fs, 0, 20 * 10)
        }
    }

    private val starViewingIndex: MutableMap<String, Int> = mutableMapOf()

    @EventHandler
    fun starView(event: PlayerItemHeldEvent) {
        if (event.player.inventory.itemInOffHand != FiFuItems.theStar())
            return

        if (viewingAllIndex.contains(event.player.uniqueId.toString())) {
            event.player.sendMessage("冷却中... ${((viewingAllIndex[event.player.uniqueId.toString()]!! + 1.0) / canViewIsland.size) * 100} %")
            return
        }

        if (!starViewingIndex.containsKey(event.player.name)) {
            starViewingIndex[event.player.name] = 0
        }

        var index = starViewingIndex[event.player.name]!!
        if (event.newSlot == 0 && event.previousSlot == 8 || !(event.newSlot == 8 && event.previousSlot == 0) && event.newSlot > event.previousSlot) {
            index++
            if (index !in canViewIsland.indices)
                index = canViewIsland.indices.first
        } else if ((event.newSlot == 8 && event.previousSlot == 0) || event.newSlot < event.previousSlot) {
            index--
            if (index !in canViewIsland.indices)
                index = canViewIsland.indices.last
        }

        starViewingIndex[event.player.name] = index

        event.player.gameMode = GameMode.ADVENTURE
        event.player.tpIsland(canViewIsland[starViewingIndex[event.player.name]!!])
        event.player.sendMessage(
            "index $index / ${canViewIsland.indices.last} island:${event.player.location.getIsland()} 主人:${
                event.player.location.getIsland().getOwnersList()
            }"
        )
//        println("index: $index newSlot: ${event.newSlot}  previousSlot:${event.previousSlot}")

    }

}