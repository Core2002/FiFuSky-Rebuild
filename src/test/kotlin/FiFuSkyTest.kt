import `fun`.fifu.fifusky.listeners.function.FiFuItems
import org.junit.jupiter.api.Test
import kotlin.reflect.full.*
import kotlin.reflect.jvm.javaType


class FiFuSkyTest {
    @Test
    fun test() {
        println("OK")
        FiFuItems::class.companionObject?.functions?.filter { it.returnType.javaType.typeName == "org.bukkit.inventory.ItemStack" }
            ?.forEach {
                println(it.name)
            }
    }

}