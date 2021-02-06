package `fun`.fifu.fifusky.data

import `fun`.fifu.fifusky.IsLand
import java.util.*

/**
 * 代表权限信息
 * @author NekokeCore
 */
data class PrivilegeData(
    val Island: IsLand,
    var Owner: ArrayList<PlayerData>,
    var Member: ArrayList<PlayerData>
)
