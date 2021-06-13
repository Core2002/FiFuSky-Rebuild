package `fun`.fifu.fifusky.listeners.permission

import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.operators.SkyOperator.havePermission
import `fun`.fifu.fifusky.operators.SkyOperator.isSkyWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.player.PlayerBucketEmptyEvent
import org.bukkit.event.player.PlayerBucketFillEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * 权限组：方块类权限处理
 * @author NekokeCore
 */
class BlockListener : Listener {

    var str = "你没权限"

    /**
     * 当一个方块被玩家破坏的时候，调用本事件.
     *
     * @param event
     */
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (!event.player.havePermission()) {
            event.isCancelled = true
            event.player.sendMessage(str)
        }
    }

    /**
     * 当一个方块被火烧掉的时候触发此事件.
     *
     * @param event
     */
    @EventHandler
    fun onBlockBurn(event: BlockBurnEvent) {
        if (event.block.location.world.isSkyWorld()) {
            event.isCancelled = true
        }
    }

    /**
     * 玩家试图交互
     *
     * @param event
     */
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.RIGHT_CLICK_AIR || event.action == Action.PHYSICAL) {
            return
        }
        if (!event.player.havePermission()) {
            event.player.sendMessage(str)
            event.isCancelled = true
        }
    }

    /**
     * 玩家用完一只桶后触发此事件.
     *
     * @param event
     */
    @EventHandler
    fun onBucketEmpty(event: PlayerBucketEmptyEvent) {
        if (!event.player.havePermission()) {
            event.player.sendMessage(str)
            event.isCancelled = true
        }
    }

    /**
     * 水桶装满水事件.
     *
     * @param event
     */
    @EventHandler
    fun onBucketFill(event: PlayerBucketFillEvent) {
        if (!event.player.havePermission()) {
            event.player.sendMessage(str)
            event.isCancelled = true
        }
    }

    /**
     * 当树叶消失时触发此事件.
     *
     * @param event
     */
    @EventHandler
    fun onLeavesDecay(event: LeavesDecayEvent) {
        if (event.block.world.name != Sky.WORLD) return
        val xx = event.block.location.x.toInt()
        val zz = event.block.location.z.toInt()
        if (Sky.isInIsland(xx, zz, Sky.SPAWN)) {
            event.isCancelled = true
        }
    }

    /**
     * 当一个方块被点燃时触发.
     *
     * @param event
     */
    @EventHandler
    fun onBlockIgnite(event: BlockIgniteEvent) {
        if (!event.block.world.isSkyWorld()) return
        if (event.cause != BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
            event.isCancelled = true
            return
        }
        if (event.player != null && event.player!!.isOp) {
            return
        }
        val xx = event.block.location.x.toInt()
        val zz = event.block.location.z.toInt()
        if (Sky.isInIsland(xx, zz, Sky.SPAWN)) {
            event.isCancelled = true
        }
    }
}