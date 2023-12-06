package common

data class V( val x: Int,  val y: Int) {
    private fun neighbors8(): List<V> {
        return (-1..1).flatMap { dy -> (-1..1).map { dx -> V(dx, dy) } }.map { it + this }.filter { it != this }
    }

    operator fun plus(other: V): V = V(x + other.x, y + other.y)


    fun area8(map: Array<CharArray>): List<V> = neighbors8().filter { it.belongsTo(map) }

    private fun belongsTo(map: Array<CharArray>): Boolean = map.indices.contains(y) && map[0].indices.contains(x)
}