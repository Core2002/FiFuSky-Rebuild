import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.ChunkData
import `fun`.fifu.fifusky.data.Jsoner
import `fun`.fifu.fifusky.data.PlayerData
import `fun`.fifu.fifusky.data.SQLiteer
import `fun`.fifu.fifusky.operators.SkyOperator.isUnclaimed
import cn.hutool.core.date.BetweenFormatter
import cn.hutool.core.date.DateUtil
import cn.hutool.db.Db
import cn.hutool.db.Entity
import cn.hutool.json.JSONUtil
import java.lang.StringBuilder

class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")
        println(Sky.getIsLand("(0,0)").isUnclaimed())



//        GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//            println("World!") // 在延迟后打印输出
//        }
//        println("Hello,") // 协程已在等待时主线程还在继续
//
//        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活


//        Jsoner.setPlayerLastGet("53bac3d9-3d06-328d-a96e-009ea28befe0",1615607087680L)
//        println(Jsoner.getPlayerLastGet("53bac3d9-3d06-328d-a96e-009ea28befe0"))



    }


}