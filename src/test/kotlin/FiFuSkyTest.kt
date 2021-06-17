import `fun`.fifu.fifusky.Sky
import `fun`.fifu.utils.PackageUtil
import `fun`.fifu.utils.PackageUtil.getClassNameByFile
import `fun`.fifu.utils.PackageUtil.getClassNameByJar
import cn.hutool.core.io.FileUtil.getOutputStream
import java.io.DataOutputStream
import java.net.Socket
import java.io.IOException
import java.net.UnknownHostException


class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")

//        val s = Socket("1.117.16.245", 1024)
//        val out = DataOutputStream(s.getOutputStream())
//        out.write("NekokeCore——啊吧啊吧".toByteArray())
//        out.flush()
//        out.close()

        for (i in 0..16) {
            println(i)
        }
        println("OKK")
    }


}