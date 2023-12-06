package day05

import dump
import product
import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.time.measureTime

fun main() {
    fun part1(input: List<String>): Int {
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInputParsed("sample")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val executionTime = measureTime {
        val input = readInputParsed("input")
        part1(input).dump("part1: ")
        part2(input).dump("part2: ")
    }
    executionTime.dump("Executed in ")
}

fun readInputParsed(name: String) = readInput(5, name)