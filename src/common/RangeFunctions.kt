package common

fun LongRange.hasIntersectionWith(other: LongRange): Boolean =
    other.first in this || other.last in this || this.first in other || this.last in other

fun LongRange.intersectWith(other: LongRange): LongRange =
    maxOf(this.first, other.first)..minOf(this.last, other.last)

fun LongRange.shiftBy(value: Long): LongRange = this.first+value..this.last+value

fun LongRange.excludeRanges(other: List<LongRange>): List<LongRange> {
    val result = mutableListOf<LongRange>()
    var start = this.first
    for (r in other.filter { this.hasIntersectionWith(it) }.sortedBy { it.first }) {
        result.add(start..<r.first)
        start = r.last + 1
    }

    return result.plusElement(start..this.last).filterNot { it.isEmpty() }
}

fun Iterable<LongRange>.merge(): List<LongRange> {
    val sortedRanges = this.sortedBy { it.first }
    if (sortedRanges.size < 2) return sortedRanges

    var current = sortedRanges[0]
    val list = mutableListOf<LongRange>()

    for(i in 1 .. sortedRanges.lastIndex) {
        val range = sortedRanges[i]
        if (current.last > range.last) continue
        else if (range.first > current.last) {
            list.add(current)
            current = range
        } else if (range.last > current.last) {
            current = current.first..range.last
        }
    }
    list.add(current)
    return list
}