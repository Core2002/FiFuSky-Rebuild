import `fun`.fifu.fifusky.Sky
import `fun`.fifu.fifusky.data.Dataer

class FiFuSkyTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            FiFuSkyTest().test()

        }
    }

    fun test() {
        println("OK")
//        val a = Dataer.getIsLandData(Sky.getIsLand("(0,0)"))
//        a.Privilege.Owner.add(PlayerData("awa","zxb"))
//        a.Privilege.Member.add(PlayerData("qwq","xyl"))
//        a.Privilege.Member.add(PlayerData("xwx","yxl"))
//        Dataer.saveIslandData(a)
//        println(a)
        println(Dataer.getIsLandData(Sky.getIsLand("(0,0)")))
    }


}