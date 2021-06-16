package `fun`.fifu.fifusky.commands

import `fun`.fifu.fifusky.FiFuSky
import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator.addMember
import `fun`.fifu.fifusky.operators.SkyOperator.build
import `fun`.fifu.fifusky.operators.SkyOperator.getIsland
import `fun`.fifu.fifusky.operators.SkyOperator.isSkyWorld
import `fun`.fifu.fifusky.operators.SkyOperator.removeMember
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*


class AdminCommand : TabExecutor {

    companion object{
        fun theIcarus(): ItemStack {
            val itemStack = ItemStack(Material.ELYTRA)
            val im: ItemMeta = itemStack.itemMeta
            im.setDisplayName("钉三多的翅膀")
            im.lore = Collections.singletonList("组成翅膀的羽毛来自伊卡洛斯")
            im.addEnchant(Enchantment.DEPTH_STRIDER, 10, true)
            im.addEnchant(Enchantment.OXYGEN, 10, true)
            im.addEnchant(Enchantment.PROTECTION_FALL, 10, true)
            im.addEnchant(Enchantment.PROTECTION_PROJECTILE, 10, true)
            im.addEnchant(Enchantment.BINDING_CURSE, 1, true)
            im.addEnchant(Enchantment.PROTECTION_FIRE, 10, true)
            im.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 10, true)
            im.isUnbreakable = true
            itemStack.itemMeta = im
            return itemStack
        }
    }


    private val helpMassage = mapOf(
        "build-island" to "/fs-admin build-island <Skyloc> 来build一个岛",
        "make-member" to "/fs-admin make-member 将自己纳入该岛屿的成员",
        "exit-member" to "/fs-admin exit-member 将自己退出该岛屿成员",
        "get-theIcarus" to "/fs-admin get-theIcarus 获得钉三多的翅膀"
    )

    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): MutableList<String> {
        if (p0 !is Player) return mutableListOf()
        if (p3.size == 1) return helpMassage.keys.toMutableList()
        val ml = mutableListOf<String>()
        val playersName = mutableListOf<String>()
        Bukkit.getOnlinePlayers().forEach {
            playersName.add(it.name)
        }
        return when (p3[0]) {
            else -> ml
        }
    }


    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        println(
            """
            p0:$p0
            p1:$p1
            p2:$p2
            p3:${p3.contentToString()}
        """.trimIndent()
        )
        if (p0 !is Player || !p0.isOp) {
            p0.sendMessage("你必须是一名管理员级别的玩家")
            return true
        }
        if (p3.isNullOrEmpty()) return onHelp(p0, p3)
        try {
            if (!p0.world.isSkyWorld()) {
                p0.sendMessage("你必须在空岛世界才能使用这条命令")
                return true
            }
            val re = when (p3[0]) {
                "help" -> onHelp(p0,p3)
                "build-island" -> onBuild(p3)
                "make-member" -> onMakeMember(p0)
                "exit-member" -> onExitMember(p0)
                "get-theIcarus" -> onGetTheIcarus(p0)
                else -> false
            }
            if (!re) onHelp(p0, arrayOf("help", p3[0]))
        } catch (e: Exception) {
            onHelp(p0, arrayOf("help", p3[0]))
            FiFuSky.fs.logger.warning("$p0 的命令 /s ${p3.contentToString()} 导致了一个异常：")
            e.printStackTrace()
            return true
        }
        return true

    }

    private fun onGetTheIcarus(p0: Player): Boolean {
        p0.inventory.addItem(theIcarus())
        return true
    }

    private fun onExitMember(p0: Player): Boolean {
        p0.location.getIsland().removeMember(p0)
        p0.sendMessage("已将你从岛屿 ${p0.location.getIsland()} 中作为成员移除")
        return true
    }

    private fun onMakeMember(p0: Player): Boolean {
        p0.location.getIsland().addMember(p0)
        p0.sendMessage("已将你作为成员加入岛屿 ${p0.location.getIsland()}")
        return true
    }

    private fun onBuild(p3: Array<out String>): Boolean {
        Sky.getIsland(p3[1]).build()
        return true
    }

    private fun onHelp(player: Player, p3: Array<out String>): Boolean {
        if (p3.size == 1) {
            val sb = StringBuffer()
            helpMassage.values.forEach { sb.append(it).append("\n") }
            player.sendMessage("帮助：/fs-admin <命令>\n$sb")
            return true
        } else {
            helpMassage[p3[1]]?.let { player.sendMessage(it) }
        }
        return true
    }
}