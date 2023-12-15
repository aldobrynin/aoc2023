package day15

import common.Puzzle
import solveAndVerify

private typealias ParsedInput = List<String>
private typealias Lens = Pair<String, Int>

private class Day15 : Puzzle<ParsedInput>(day = 15) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.single().split(',')

    override fun part1(input: ParsedInput): Long = input.sumOf { it.hash() }.toLong()

    override fun part2(input: ParsedInput): Long {
        val boxes = Array<MutableList<Lens>>(256) { mutableListOf() }
        for (instruction in input.map { it.split('=', '-') }) {
            val (label, lens) = instruction
            val box = boxes[label.hash()]
            val index = box.indexOfFirst { it.first == label }
            if (lens.isEmpty()) {
                if (index != -1) box.removeAt(index)
            } else {
                if (index != -1) box[index] = label to lens.toInt()
                else box.add(label to lens.toInt())
            }
        }

        return boxes
            .flatMapIndexed { boxId, box -> box.mapIndexed { slot, lens -> (boxId + 1L) * (slot + 1) * lens.second } }
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