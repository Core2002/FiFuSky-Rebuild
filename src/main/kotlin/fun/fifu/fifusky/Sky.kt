package `fun`.fifu.fifusky

import kotlin.math.abs


/**
 * SIDE 单个岛屿边长
 * MAXINLAND 世界最大岛屿数的单个边长的和
 *
 * Pair<Int, Int>一律为真实坐标
 * IsLand.SkyLoc一律为岛坐标
 * @author NekokeCore
 */
object Sky {
    private const val SIDE = 1024
    private const val MAXISLAND = 29296
    private fun getR(SkyR: Int): Int = SIDE * SkyR
    private fun getRR(SkyR: Int): Int = SIDE * (SkyR + 1) - 1

    fun getIsLand(skyLoc: Pair<Int, Int>) =
        IsLand(skyLoc, getR(skyLoc.first), getRR(skyLoc.first), getR(skyLoc.second), getRR(skyLoc.second))

    fun getIsLandCenter(isLand: IsLand) =
        Pair((isLand.XX - isLand.X) / 2 + isLand.X, (isLand.YY - isLand.Y) / 2 + isLand.Y)

    fun isInIsLand(loc: Pair<Int, Int>, isLand: IsLand) =
        loc.first in isLand.X..isLand.XX && loc.second in isLand.Y..isLand.YY

    fun getSkyR(rr: Int): Int {
        var SkyR = 0
        if (rr > 0) {
            while (rr !in getR(SkyR)..getRR(SkyR)) {
                SkyR++
                if (abs(SkyR) > MAXISLAND)
                    throw RuntimeException("R轴SkyLoc正越界！SkyR=$SkyR ,rr=$rr")
            }
        } else if (rr < 0) {
            while (rr !in getR(SkyR)..getRR(SkyR)) {
                SkyR--
                if (abs(SkyR) > MAXISLAND)
                    throw RuntimeException("R轴SkyLoc负越界！SkyR=$SkyR ,rr=$rr")
            }
        }
        return SkyR
    }

}