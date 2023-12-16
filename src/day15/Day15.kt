package day15

import common.Puzzle
import solveAndVerify

private typealias ParsedInput = List<String>

private class Day15 : Puzzle<ParsedInput>(day = 15) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.single().split(',')

    override fun part1(input: ParsedInput): Long = input.sumOf { it.hash() }.toLong()

    override fun part2(input: ParsedInput): Long {
        val boxes = mutableMapOf<Int, MutableMap<String, Int>>()
        for (instruction in input.map { it.split('=', '-') }) {
            val (label, lens) = instruction
            val box = boxes.getOrPut(label.hash()) { mutableMapOf() }
            if (lens.isEmpty()) box.remove(label)
            else box[label] = lens.toInt()
        }
        return boxes.entries
            .flatMap { box -> box.value.entries.mapIndexed { slot, lens -> (box.key + 1L) * (slot + 1) * lens.value } }
            .sum()
    }

    fun String.hash() = this.toCharArray().fold(0) { acc, char -> (acc + char.code) * 17 % 256 }
}

fun main() {
    val puzzle = Day15()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 1320)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 145)

    puzzle.run()
}