package `fun`.fifu.fifusky.listeners.permission

import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator
import `fun`.fifu.fifusky.operators.SkyOperator.havePermission
import `fun`.fifu.fifusky.operators.SkyOperator.inProtectionRadius
import `fun`.fifu.fifusky.operators.SkyOperator.inSpawn
import `fun`.fifu.fifusky.operators.SkyOperator.isSkyWorld
import `fun`.fifu.fifusky.operators.SkyOperator.showDamage
import org.bukkit.ChatColor
import org.bukkit.Chunk
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityExplodeEvent
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.*
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * 权限组：实体类权限处理
 * @author NekokeCore
 */
class EntityListener : Listener {
    /**
     * 当一个生物体在世界中出生时触发该事件.
     *
     * @param event
     */
    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (!event.location.world.isSkyWorld()) return
        if (event.entity.location.inSpawn() && event.entity is Monster) {
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
        if (!explodeEvent.location.world.isSkyWorld()) return
        val chunk: Chunk = explodeEvent.location.chunk
        val chunkLoc = "[${chunk.x},${chunk.z}]"
        if (SkyOperator.canExplosion(chunkLoc)) {
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
        if (!event.player.location.world.isSkyWorld()) return
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
        if (!player.havePermission()) {
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
        val entity: Entity = event.entity
        if (entity.inProtectionRadius()) {
            event.isCancelled = true
        }
        val loc = entity.location
        if (entity is LivingEntity) {
            if (event.damager is Player) {
                val player = event.damager as Player
                if (entity !is Monster && !player.havePermission() && !Sky.isInIsland(
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
                    if (player != null) entity.showDamage(player)
                    if (entity !is Monster && player != null && !player.havePermission() && !Sky.isInIsland(
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

    /**
     * 物品拾取保护机制
     */
    @EventHandler
    fun onPickup(entityPickupItemEvent: EntityPickupItemEvent) {
        val itemStack = entityPickupItemEvent.item.itemStack
        try {
            val byteArrayOutputStream1 = ByteArrayOutputStream()
            val bukkitObjectOutputStream = BukkitObjectOutputStream(byteArrayOutputStream1)
            bukkitObjectOutputStream.writeObject(itemStack)
            bukkitObjectOutputStream.close()
            var player: Player? = null
            var isPlayer = false
            if (entityPickupItemEvent.entity is Player) {
                player = entityPickupItemEvent.entity as Player
                isPlayer = true
            }
            val invSize = if (player == null) {
                10000
            } else {
                try {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    val data = BukkitObjectOutputStream(byteArrayOutputStream)
                    data.writeObject(player.inventory.contents)
                    data.close()
                    byteArrayOutputStream.close()
                    byteArrayOutputStream.toByteArray().size
                } catch (ioException: IOException) {
                    ioException.printStackTrace()
                    5000
                }
            }
            val itemSize = byteArrayOutputStream1.toByteArray().size
            byteArrayOutputStream1.close()
            val maxSize = 1000000
            if (itemSize + invSize > maxSize) {
                if (isPlayer) {
                    player!!.sendMessage(
                        ChatColor.translateAlternateColorCodes(
                            '&',
                            "&8&l(&c&l!&8&l) &c你试图捡起的这个物品要素过多，为了安全起见，不能被捡起。"
                        )
                    )
                }
                entityPickupItemEvent.isCancelled = true
            }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

}