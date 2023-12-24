package common

import kotlin.math.abs

data class V(val x: Int, val y: Int) {
    companion object {
        val Zero = V(0, 0)
        val Up = V(0, -1)
        val Down = V(0, 1)
        val Left = V(-1, 0)
        val Right = V(1, 0)
        fun dir8(): List<V> = (-1..1).flatMap { dy -> (-1..1).map { V(it, dy) } }.filter { it != Zero }

        fun dir4(): List<V> = listOf(Up, Down, Left, Right)
    }

    private fun neighbors8(): List<V> = dir8().map { it + this }
    fun neighbors4(): List<V> = dir4().map { it + this }

    operator fun plus(other: V): V = V(x + other.x, y + other.y)

    fun <T> area8(map: List<List<T>>): List<V> = neighbors8().filter { it.belongsTo(map) }
    fun <T> area4(map: List<List<T>>): List<V> = neighbors4().filter { it.belongsTo(map) }

    fun <T> belongsTo(map: Collection<Collection<T>>): Boolean = map.indices.contains(y) && map.first().indices.contains(x)
    operator fun minus(start: V): V = V(x - start.x, y - start.y)
    operator fun unaryMinus(): V = V(-x, -y)

    operator fun times(factor: Int): V = V(x * factor, y * factor)

    fun distanceTo(other: V): Int = abs(other.x - x) + abs(other.y - y)
}