package common

data class V3(val x: Double, val y: Double, val z: Double) {
    operator fun minus(pos: V3): V3 = V3(x - pos.x, y - pos.y, z - pos.z)
    operator fun plus(pos: V3): V3 = V3(x + pos.x, y + pos.y, z + pos.z)
    operator fun div(d: Long): V3 = V3(x / d, y / d, z / d)
    operator fun times(t: Long): V3 = V3(x * t, y * t, z * t)
    operator fun unaryMinus(): V3 = V3(-x, -y, -z)

    companion object {
        fun parse(string: String): V3 {
            val (x, y, z) = string.split(",").map { it.trim() }.map { it.toDouble() }
            return V3(x, y, z)
        }
    }
}