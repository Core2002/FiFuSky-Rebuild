package `fun`.fifu.fifusky.listeners

import `fun`.fifu.fifusky.data.Dataer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListener :Listener{

    @EventHandler
    fun onPlayerJoin(e:PlayerJoinEvent){
        Dataer.savePlayerIp(e.player.uniqueId.toString(),e.player.address.hostName)
    }
}