package `fun`.fifu.fifusky.listeners.permission

import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator
import org.bukkit.Chunk
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.MerchantInventory
import org.bukkit.inventory.PlayerInventory
import org.bukkit.entity.Player


class EntityListener : Listener {
    /**
     * 当一个生物体在世界中出生时触发该事件.
     *
     * @param event
     */
    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (event.location.world.name != Sky.WORLD) return
        val xx = event.location.x.toInt()
        val zz = event.location.z.toInt()
        if (Sky.isInIsLand(xx, zz, Sky.SPAWN) && event.entity is Monster) {
            event.isCancelled = true
        }
    }

    /**
     * 实体爆炸事件
     *
     * @param explodeEvent
     */
    @EventHandler(ignoreCancelled = true)
    fun onEntityExplode(explodeEvent: EntityExplodeEvent) {
        if (explodeEvent.location.world.name != Sky.WORLD) return
        val chunk: Chunk = explodeEvent.location.chunk
        val CLoc: String = "[${chunk.x},${chunk.z}]"
        if (SkyOperator.canExplosion(CLoc)) {
            return
        }
        explodeEvent.blockList().clear()
        explodeEvent.isCancelled = true
    }

    /**
     * 容器被打开事件
     *
     * @param event
     */
    @EventHandler
    fun onOpen(event: InventoryOpenEvent) {
        if (event.player.location.world.name != Sky.WORLD) return
        val inventory = event.inventory
        if (inventory is PlayerInventory || inventory is MerchantInventory || inventory is CraftingInventory || inventory is EnchantingInventory) {
            return
        }
        val player: Player = if (event.player is Player) {
            event.player as Player
        } else {
            return
        }
        val title = event.view.title
        if ("Slimefun 指南" == title) {
            return
        }
        if (title.contains("Slimefun") || title.contains("设置") || title.contains("設置") || title.contains("Settings") || title.contains(
                "Searching"
            ) || title.contains("搜索")
        ) {
            return
        }
        if (!SkyOperator.havePermission(player)) {
            player.sendMessage("你没权限")
            event.isCancelled = true
        }
    }

    /**
     * 实体受伤时触发
     * 保护非怪物不被没有权限的玩家伤害
     *
     * @param event
     */
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        //todo 保护区域
//        if (Helper.hasSpawnProtection(event.entity.location)) {
//            event.isCancelled = true
//            return
//        }
        val entity: Entity = event.entity
        val loc = entity.location
        if (entity is LivingEntity) {
            if (event.damager is Player) {
                val player = event.damager as Player
                //伤害显示
//                Helper.showDamage(player, entity as LivingEntity)

                if (entity !is Monster && !SkyOperator.havePermission(player) && !Sky.isInIsLand(
                        loc.blockX,
                        loc.blockZ,
                        Sky.SPAWN
                    )
                ) {
                    player.sendMessage("你没权限伤害她！")
                    event.isCancelled = true
                }
            } else if (event.damager is Projectile) {
                if ((event.damager as Projectile).shooter is Player) {
                    val player = (event.damager as Projectile).shooter as Player?
                    if (player != null) SkyOperator.showDamage(player, entity)
                    if (entity !is Monster && player != null && !SkyOperator.havePermission(player) && !Sky.isInIsLand(
                            loc.blockX,
                            loc.blockZ,
                            Sky.SPAWN
                        )
                    ) {
                        player.sendMessage("你没权限伤害她！")
                        event.isCancelled = true
                    }
                }
            }
        }
    }

}