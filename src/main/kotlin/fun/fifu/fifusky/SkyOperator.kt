package `fun`.fifu.fifusky

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

object SkyOperator {

    /**
     * 把玩家传送至某岛屿
     * @param player 玩家
     * @param isLand 岛屿
     */
    fun TpIsLand(player: Player, isLand: IsLand) {
        val isLandCenter = Sky.getIsLandCenter(isLand)
        player.teleport(
            Location(
                Bukkit.getWorld("world"),
                isLandCenter.first.toDouble(),
                65.0,
                isLandCenter.second.toDouble()
            )
        )
    }

    /**
     * 构建一个岛屿，将模板岛屿复制到目标岛屿
     * @param isLand 要构建的目标岛屿
     */
    fun BuildIsLand(isLand: IsLand) {
        val t = System.currentTimeMillis()
        //准备工作
        val ic = Sky.getIsLandCenter(isLand)
        val world = Bukkit.getWorld("world")
        world?.getBlockAt(ic.first, 64, ic.second)?.setType(Material.BEDROCK, true)

        //上界
        val sj = Triple(3, 82, -114)
        //下界
        val xj = Triple(12, 70, -120)
        //基址
        val jz = Triple(8, 75, -117)

        //存储模板岛屿偏移
        val ib = HashMap<Triple<Int, Int, Int>, BlockData>()
        for (t in blp(sj, xj)) {
            ib[Triple(
                t.first - jz.first,
                t.second - jz.second,
                t.third - jz.third
            )] = world?.getBlockAt(t.first, t.second, t.third)!!.blockData
        }

        //目标基址
        val mb = Sky.getIsLandCenter(isLand)

        //开始复制
        for ((t, b) in ib) {
            world?.getBlockAt(
                mb.first + t.first,
                64 + t.second,
                mb.second + t.third
            )!!.blockData = b
        }

        FiFuSky.fiFuSky.logger.info("复制完毕！$isLand 耗时 ${System.currentTimeMillis() - t} ms。")
    }


    /**
     * 遍历一个三维空间
     * @param sj 空间上界顶点
     * @param xj 空间下界顶点
     * @return 该空间内的点集列表
     */
    fun blp(sj: Triple<Int, Int, Int>, xj: Triple<Int, Int, Int>): ArrayList<Triple<Int, Int, Int>> {
        fun bl(f: Int, c: Int) = if (f <= c) f..c else f downTo c
        val arr = ArrayList<Triple<Int, Int, Int>>()
        for (xx in bl(sj.first, xj.first))
            for (yy in bl(sj.second, xj.second))
                for (zz in bl(sj.third, xj.third))
                    arr.add(Triple(xx, yy, zz))
        return arr
    }

}