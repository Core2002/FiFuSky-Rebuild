package `fun`.fifu.fifusky.listeners.function

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

/**
 * 这里存储了FiFu的物品
 */
class FiFuItems {
    companion object {
        /**
         * 伊卡洛斯的翅膀
         */
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

        /**
         * 弈颗星欸
         * 用来参观岛屿
         */
        fun theStar(): ItemStack {
            val itemStack = ItemStack(Material.NETHER_STAR)
            val im: ItemMeta = itemStack.itemMeta
            im.setDisplayName("弈颗星欸")
            im.lore = Collections.singletonList("放在副手+滚轮可以参观岛屿")
            im.addEnchant(Enchantment.OXYGEN, 10, true)
            itemStack.itemMeta = im
            return itemStack
        }

    }
}