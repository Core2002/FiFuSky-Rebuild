package `fun`.fifu.fifusky.listeners

import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.operators.SoundPlayer
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerListener :Listener{

    @EventHandler
    fun onPlayerJoin(event:PlayerJoinEvent){
        SQLiteer.savePlayerIp(event.player.uniqueId.toString(),event.player.address.hostName)
        SQLiteer.savePlayerName(event.player.uniqueId.toString(),event.player.name)
        event.player.gameMode=GameMode.SURVIVAL
        SoundPlayer.playCat(event.player)
    }
}