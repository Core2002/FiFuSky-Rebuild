import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList

/**
 * 声音播放器单例
 */
object SoundPlayer {
    var catList = ArrayList<Sound>()

    init {
        catList.add(Sound.ENTITY_CAT_AMBIENT)
        catList.add(Sound.ENTITY_CAT_BEG_FOR_FOOD)
        catList.add(Sound.ENTITY_CAT_DEATH)
        catList.add(Sound.ENTITY_CAT_EAT)
        catList.add(Sound.ENTITY_CAT_HISS)
        catList.add(Sound.ENTITY_CAT_HURT)
        catList.add(Sound.ENTITY_CAT_PURR)
        catList.add(Sound.ENTITY_CAT_AMBIENT)
        catList.add(Sound.ENTITY_CAT_PURREOW)
        catList.add(Sound.ENTITY_CAT_STRAY_AMBIENT)
    }

    fun playCat(player: Player) {
        player.playSound(player.location, extact(catList), 10f, 10f)
    }

    private fun extact(arrayList: ArrayList<Sound>): Sound {
        val r = Random(1)
        val temp: Int = r.nextInt(arrayList.size)
        return arrayList[temp]
    }
}