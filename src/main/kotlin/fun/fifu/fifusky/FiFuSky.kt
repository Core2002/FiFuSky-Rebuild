package `fun`.fifu.fifusky

import `fun`.fifu.fifusky.commands.AdminCommand
import `fun`.fifu.fifusky.commands.SkyCommand
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.listeners.PlayerListener
import `fun`.fifu.fifusky.listeners.permission.BlockListener
import `fun`.fifu.fifusky.listeners.permission.EntityListener
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * FiFuSky插件主类
 * @author NekokeCore
 */
class FiFuSky : JavaPlugin() {

    companion object {
        lateinit var fs: FiFuSky
    }

    override fun onLoad() {
        fs = this
        if (!File("plugins/FiFuSky").isDirectory){
            saveResource("FiFuSky.db", false)
            saveResource("db.setting", false)
        }
    }

    override fun onEnable() {
        //注册命令
        Bukkit.getPluginCommand("s")?.setExecutor(SkyCommand())
        Bukkit.getPluginCommand("fs-admin")?.setExecutor(AdminCommand())

        //注册监听器
        server.pluginManager.registerEvents(PlayerListener(), this)
        server.pluginManager.registerEvents(BlockListener(), this)
        server.pluginManager.registerEvents(EntityListener(), this)

        logger.info(SQLiteer.getIsLandData(Sky.getIsLand("(0,0)")).toString())
        logger.info("FiFu空岛插件已启动！")
    }
}