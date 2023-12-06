package common

data class V(val x: Int, val y: Int) {
    companion object {
        val Zero = V(0, 0)
        fun dir8(): List<V> = (-1..1).flatMap { dy -> (-1..1).map { V(it, dy) } }.filter { it != Zero }
    }

    private fun neighbors8(): List<V> = dir8().map { it + this }

    operator fun plus(other: V): V = V(x + other.x, y + other.y)

    fun area8(map: Array<Array<Char>>): List<V> = neighbors8().filter { it.belongsTo(map) }

    private fun belongsTo(map: Array<Array<Char>>): Boolean = map.indices.contains(y) && map[0].indices.contains(x)
}