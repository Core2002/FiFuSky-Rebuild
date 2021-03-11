import `fun`.fifu.fifusky.data.SQLiteer

class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")


//        GlobalScope.launch { // 在后台启动一个新的协程并继续
//            delay(1000L) // 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
//            println("World!") // 在延迟后打印输出
//        }
//        println("Hello,") // 协程已在等待时主线程还在继续
//
//        Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活

        val HelpMassage = mapOf(
            "get" to "领取一个岛屿，两个月只能领一次",
            "asa" to "bbb",
            "ccc" to "owo"
        )
        val sb = StringBuffer()
        HelpMassage.values.forEach { sb.append(it).append("\n") }
        println(sb)


    }


}