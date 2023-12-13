package day12

import common.Puzzle
import solveAndVerify
import toIntArray

private typealias ConditionRecord = Pair<String, List<Int>>
private typealias ParsedInput = List<ConditionRecord>

private class Day12 : Puzzle<ParsedInput>(day = 12) {
    override fun parse(rawInput: List<String>): ParsedInput =
        rawInput.map { it.split(' ') }.map { it[0] to it[1].toIntArray(",") }

    override fun part1(input: ParsedInput): Long = input.sumOf { count(it) }

    override fun part2(input: ParsedInput): Long = input.sumOf { count(it.unfold(5)) }

    fun ConditionRecord.unfold(factor: Int): ConditionRecord =
        (0..<factor).joinToString("?") { this.first } to (0..<factor).flatMap { this.second }

    fun count(record: ConditionRecord): Long {
        val (springs, groups) = record
        var dp = arrayOf(1L) + springs.map { 0L }
        for (i in springs.indices.takeWhile { springs[it] != '#' })
            dp[i + 1] = dp[i]

        for (group in groups) {
            val newDp = Array(springs.length + 1) { 0L }
            var currentGroup = 0
            for ((index, spring) in springs.withIndex()) {
                if (spring != '#') newDp[index + 1] = newDp[index]
                if (spring != '.') currentGroup++ else currentGroup = 0

                if (currentGroup < group) continue
                val groupStart = index - group + 1
                if (groupStart == 0 || springs[groupStart - 1] != '#') {
                    newDp[index + 1] += dp[if (groupStart == 0) 0 else groupStart - 1]
                }
            }
            dp = newDp
        }
        return dp.last()
    }
}

fun main() {
    val puzzle = Day12()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 21)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 525152)

    puzzle.run()
}