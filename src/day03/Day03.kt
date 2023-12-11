package day03

import common.Puzzle
import common.V
import product
import solveAndVerify

private data class PartNumber(val number: Long, val coordinates: List<V>)

private typealias ParsedInput = List<List<Char>>

private class Day03 : Puzzle<ParsedInput>(day = 3) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }

    override fun part1(input: ParsedInput): Long {
        return findNumbers(input)
            .filter { num -> area8(num.coordinates, input).any { isSymbol(it, input) } }
            .sumOf { it.number }
    }

    override fun part2(input: ParsedInput): Long {
        return findNumbers(input)
            .flatMap { x ->
                area8(x.coordinates, input).filter { n -> input[n.y][n.x] == '*' }.map { Pair(x.number, it) }
            }
            .groupBy { it.second }
            .filter { it.value.size == 2 }
            .values
            .map { pair -> pair.map { it.first } }
            .sumOf { x -> x.product() }
    }

    private fun area8(v: List<V>, map: List<List<Char>>): List<V> = v.flatMap { it.area8(map) }.distinct()

    private fun isSymbol(v: V, map: List<List<Char>>): Boolean = map[v.y][v.x] != '.' && !map[v.y][v.x].isDigit()

    private fun findNumbers(map: List<List<Char>>): List<PartNumber> {
        val numbers = mutableListOf<PartNumber>()
        for ((rowIndex, row) in map.withIndex()) {
            var number = 0L
            val currentNumberCells = mutableListOf<V>()
            for ((colIndex, char) in row.withIndex()) {
                if (char.isDigit()) {
                    number = number * 10 + char.digitToInt()
                    currentNumberCells.add(V(colIndex, rowIndex))
                }
                val isEndOfNumber = colIndex == row.lastIndex || !char.isDigit()
                if (currentNumberCells.isNotEmpty() && isEndOfNumber) {
                    numbers.add(PartNumber(number, currentNumberCells.toList()))
                    number = 0
                    currentNumberCells.clear()
                }
            }
        }
        return numbers
    }
}

fun main() {
    val puzzle = Day03()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 4361)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 467835)

    puzzle.run()
}