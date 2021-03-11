package `fun`.fifu.fifusky.listeners

import `fun`.fifu.fifusky.data.SQLiteer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListener :Listener{

    @EventHandler
    fun onPlayerJoin(event:PlayerJoinEvent){
        SQLiteer.savePlayerIp(event.player.uniqueId.toString(),event.player.address.hostName)
        SoundPlayer.playCat(event.player)
    }
}