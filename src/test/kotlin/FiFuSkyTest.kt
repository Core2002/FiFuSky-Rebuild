import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.Dataer
import cn.hutool.cache.Cache
import cn.hutool.cache.file.LFUFileCache
import cn.hutool.json.JSONUtil
import cn.hutool.core.date.DateUnit

import cn.hutool.cache.CacheUtil


class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")
//        参数1：容量，能容纳的byte数
//        参数2：最大文件大小，byte数，决定能缓存至少多少文件，大于这个值不被缓存直接读取
//        参数3：超时。毫秒
//        val cache = LFUFileCache(1024*1024, 1024*1024, 2000)
//        val bytes = cache.getFileBytes("d:/a.json")
//        var parseObj = JSONUtil.parseObj(String(bytes))
//        println(cache.getFileBytes("d:/a.json")===cache.getFileBytes("d:/a.json"))

        val lfuCache = CacheUtil.newLFUCache<Any, Any>(2)
        lfuCache.put("a", "a", DateUnit.SECOND.millis * 3)
        lfuCache.put("aa", "b", DateUnit.SECOND.millis * 3)
        lfuCache.put("aaa", "c", DateUnit.SECOND.millis * 3)
        println(lfuCache["a"])


        val isLand = Sky.getIsLand(Pair(5393, -5551))
        println(isLand)
        println(Dataer.getPlayerName("b71fe8c7-0ef5-4bb3-9fe2-623a04083a46"))
        val isLandData = Dataer.getIsLandData(isLand)
        println(isLandData)
        println(isLandData.IsLand === isLandData.Privilege.Island)

        println(Dataer.getPlayerUuid("NekokeCore"))
    }
}