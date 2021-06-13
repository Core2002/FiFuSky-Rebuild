package `fun`.fifu.fifusky.listeners.function

import `fun`.fifu.fifusky.operators.SkyOperator.isSkyWorld
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFormEvent

/**
 * 模块：挖石出矿
 * @author NekokeCore
 */
class Minerals : Listener {
    private fun getBlock(m: Material, b: Int): Material {
        val minerals = (Math.random() * 100).toInt()
        var chufalv = 45
        chufalv += b
        if (minerals <= chufalv) {
            var luck = (Math.random() * 100).toInt()
            if (chufalv > 80 && luck > 80) {
                luck = (Math.random() * 80).toInt()
            }
            // System.out.println("shuakuang=true,luck=" + luck);
            if (luck in 21..34) { // 煤炭
                return Material.COAL_ORE
            } else {
                if (luck in 35..44) { // 红石
                    return Material.REDSTONE_ORE
                } else {
                    if (luck in 45..47) { // 铁
                        return Material.IRON_ORE
                    } else {
                        if (luck in 48..49) { // 金
                            return Material.GOLD_ORE
                        } else {
                            if (luck == 50) { // 钻石
                                return Material.DIAMOND_ORE
                            } else {
                                if (luck in 51..52) { // 青金石
                                    return Material.LAPIS_ORE
                                } else {
                                    if (luck in 53..56) { // 绿宝石
                                        return Material.EMERALD_ORE
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // System.out.println("shuakuang=false");
            return m
        }
        return m
    }

    @EventHandler
    fun onBlockCanBuild(event: BlockFormEvent) {
        if (!event.block.world.isSkyWorld()) return
        // System.out.println("试图放置的方块的X" + block2.getX() + "y" + block2.getY() + "Z" +
        // block2.getZ());

        // 更改目标方块为矿石
        //System.out.println(event.getNewState().getLocation());
        // b.setType(Material.ICE);
        if (event.newState.type == Material.COBBLESTONE) { //当破坏原石
            event.newState.type = getBlock(event.newState.type, 40)
        } else {
            if (event.newState.type == Material.STONE) { //当破坏石头
                if (event.block.world.hasStorm()) { //当下雨天
                    event.newState.type = getBlock(event.newState.type, 30)
                } else { //当非下雨天
                    event.newState.type = getBlock(event.newState.type, 20)
                }
            }
        }

        // b.setType(Material.STONE);
    }
}