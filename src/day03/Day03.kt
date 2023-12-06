package day03

import common.V
import dump
import product
import readInput

fun main() {
    fun area8(v: List<V>, map: Array<CharArray>): List<V> = v.flatMap { it.area8(map) }.distinct()

    fun isSymbol(v: V, map: Array<CharArray>): Boolean = map[v.y][v.x] != '.' && !map[v.y][v.x].isDigit()

    fun findNumbers(map: Array<CharArray>): List<Pair<Int, List<V>>> {
        val numbers = mutableListOf<Pair<Int, List<V>>>()
        for ((rowIndex, row) in map.withIndex()) {
            var number = 0
            val currentNumberCells = mutableListOf<V>()
            for ((colIndex, char) in row.withIndex()) {
                if (char.isDigit()) {
                    number = number * 10 + char.digitToInt()
                    currentNumberCells.add(V(colIndex, rowIndex))
                }
                val isEndOfNumber = colIndex == row.lastIndex || !char.isDigit()
                if (currentNumberCells.isNotEmpty() && isEndOfNumber) {
                    numbers.add(Pair(number, currentNumberCells.toList()))
                    number = 0
                    currentNumberCells.clear()
                }
            }
        }
        return numbers
    }


    fun part1(input: Array<CharArray>): Int {
        return findNumbers(input)
            .filter { num -> area8(num.second, input).any { isSymbol(it, input) } }
            .sumOf { it.first }
    }

    fun part2(input: Array<CharArray>): Int {
        return findNumbers(input)
            .flatMap { x -> area8(x.second, input).filter { n -> input[n.y][n.x] == '*' }.map { Pair(x.first, it) } }
            .groupBy { it.second }
            .filter { it.value.size == 2 }
            .values
            .map { pair -> pair.map { it.first } }
            .sumOf { x -> x.product() }
    }

    val testInput = readInputParsed("sample")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInputParsed("input")
    part1(input).dump("part1: ")
    part2(input).dump("part2: ")
}

fun readInputParsed(name: String) = readInput(3, name).map{line -> line.toCharArray()}.toTypedArray()