import `fun`.fifu.utils.PackageUtil
import `fun`.fifu.utils.PackageUtil.getClassNameByFile
import `fun`.fifu.utils.PackageUtil.getClassNameByJar

class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")
        PackageUtil.getClassName("fun.fifu.fifusky.listeners")?.forEach {
            println(it)
        }
        "zhong xiao bai zui shuai".split(' ').forEach(System.out::println)

        getClassNameByJar("C:\\Users\\core2\\IdeaProjects\\ZxbSkyWorldPlugin\\build\\libs\\ZxbSkyWorldPlugin-1.0-SNAPSHOT.jar!.",true).forEach {
            println(it)
        }



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