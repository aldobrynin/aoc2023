package day05

import common.Puzzle
import solveAndVerify

const val DAY = 5

private class Day05 : Puzzle<List<String>>(day = DAY) {
    override fun parse(rawInput: List<String>): List<String> = rawInput

    override fun part1(input: List<String>): Long {
        return 0
    }

    override fun part2(input: List<String>): Long {
        return 0
    }
}

fun main() {
    val puzzle = Day05()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 0)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 0)

    puzzle.run()
}