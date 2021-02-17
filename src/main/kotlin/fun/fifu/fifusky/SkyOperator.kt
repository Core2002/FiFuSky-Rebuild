package `fun`.fifu.fifusky

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

object SkyOperator {

    /**
     * 把玩家传送至某岛屿
     * @param player 玩家
     * @param isLand 岛屿
     */
    fun TpIsLand(player: Player, isLand: IsLand) {
        val isLandCenter = Sky.getIsLandCenter(isLand)
        player.teleport(
            Location(
                Bukkit.getWorld("world"),
                isLandCenter.first.toDouble(),
                65.0,
                isLandCenter.second.toDouble()
            )
        )
    }

}