package `fun`.fifu.fifusky

import kotlin.math.abs


/**
 * SIDE 单个岛屿边长
 * MAX_INLAND 世界最大岛屿数的单个边长的和
 *
 * Pair<Int, Int>一律为真实坐标
 * IsLand.SkyLoc一律为岛坐标
 * @author NekokeCore
 */
object Sky {
    const val SIDE = 1024
    const val MAX_ISLAND = 29296
    const val WORLD = "world"
    val SPAWN = getIsLand("(0,0)")
    private fun getR(SkyR: Int): Int = SIDE * SkyR
    private fun getRR(SkyR: Int): Int = SIDE * (SkyR + 1) - 1

    /**
     * 把岛坐标字符串转化成坐标元组
     * @param skyLoc 岛坐标字符串
     * @return 岛坐标元组
     */
    private fun getSkyLocPair(skyLoc: String): Pair<Int, Int> {
        if (skyLoc == "null" || '(' !in skyLoc || ',' !in skyLoc || ')' !in skyLoc)
            throw java.lang.RuntimeException("SkyLoc 不合法！  ->  $skyLoc")
        val c = skyLoc.indexOf(',')
        val x = skyLoc.substring(1, c)
        val y = skyLoc.substring(c + 1, skyLoc.indexOf(')'))
        return Pair(x.toInt(), y.toInt())
    }

    /**
     * 使用岛坐标元组获取岛屿对象
     * @param skyLoc 岛坐标元组
     * @return 岛屿对象
     */
    fun getIsLand(skyLoc: Pair<Int, Int>) =
        IsLand(skyLoc, getR(skyLoc.first), getRR(skyLoc.first), getR(skyLoc.second), getRR(skyLoc.second))

    /**
     * 使用岛坐标字符串获取岛屿对象
     * @param skyLoc 岛坐标字符串
     * @return 岛屿对象
     */
    fun getIsLand(skyLoc: String) = getIsLand(getSkyLocPair(skyLoc))

    /**
     * 使用坐标串获取岛屿对象
     * @param xx x坐标
     * @param zz z坐标
     * @return 岛屿对象
     */
    fun getIsLand(xx: Int, zz: Int) = getIsLand(Pair(getSkyR(xx), getSkyR(zz)))

    /**
     * 获取岛屿中心坐标元组
     * @param isLand 岛屿对象
     * @return 岛屿中心坐标元组
     */
    fun getIsLandCenter(isLand: IsLand) =
        Pair((isLand.XX - isLand.X) / 2 + isLand.X, (isLand.YY - isLand.Y) / 2 + isLand.Y)

    /**
     * 判断坐标元组是否在指定岛屿内
     * @param loc 坐标元组
     * @param isLand 岛屿对象
     * @return 返回True则真，False则假
     */
    fun isInIsLand(xx: Int, zz: Int, isLand: IsLand) =
        xx in isLand.X..isLand.XX && zz in isLand.Y..isLand.YY

    /**
     * 获取指定坐标所在的岛屿坐标
     * @param rr 指定的坐标
     * @return 其所在的岛屿坐标
     */
    private fun getSkyR(rr: Int): Int {
        var skyR = 0
        if (rr > 0) {
            while (rr !in getR(skyR)..getRR(skyR)) {
                skyR++
                if (abs(skyR) > MAX_ISLAND)
                    throw RuntimeException("R轴SkyLoc正越界！SkyR=$skyR ,rr=$rr")
            }
        } else if (rr < 0) {
            while (rr !in getR(skyR)..getRR(skyR)) {
                skyR--
                if (abs(skyR) > MAX_ISLAND)
                    throw RuntimeException("R轴SkyLoc负越界！SkyR=$skyR ,rr=$rr")
            }
        }
        return skyR
    }

}