package common

import dump
import measureAndPrint
import kotlin.io.path.Path
import kotlin.io.path.readLines

abstract class Puzzle<Input>(private val day: Int) {
    fun parse(inputFileName: String): Input = parse(readInput(day, inputFileName))
    abstract fun parse(rawInput: List<String>): Input
    abstract fun part1(input: Input): Long
    abstract fun part2(input: Input): Long

    fun run() {
        println("Solving day $day")
        val input = parse("input.txt")
        measureAndPrint {
            part1(input).dump("Part1: ")
            part2(input).dump("Part2: ")
        }
    }
}

private fun readInput(day: Int, name: String) = Path("src/day%02d/%s".format(day, name)).readLines()