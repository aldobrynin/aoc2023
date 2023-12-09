package day09

import common.Puzzle
import solveAndVerify
import toLongArray

private typealias ParsedInput = List<List<Long>>

private class Day09 : Puzzle<ParsedInput>(day = 9) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { line -> line.toLongArray() }

    override fun part1(input: ParsedInput): Long = input.sumOf {
        extrapolate(it).fold(0L) { acc, b -> b.second + acc }
    }

    override fun part2(input: ParsedInput): Long = input.sumOf {
        extrapolate(it).reversed().fold(0L) { acc, b -> b.first - acc }
    }

    tailrec fun extrapolate(sequence: List<Long>, acc: List<Pair<Long, Long>> = emptyList()): List<Pair<Long, Long>> {
        if (sequence.all { it == 0L }) return acc
        return extrapolate(
            sequence.zipWithNext { a, b -> b - a },
            acc.plusElement(sequence.first() to sequence.last())
        )
    }
}

fun main() {
    val puzzle = Day09()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 114)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 2)

    puzzle.run()
}