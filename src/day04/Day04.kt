package day04

import dump
import product
import readInput



fun main() {
    fun part1(input: Array<CharArray>): Int {
        return 0
    }

    fun part2(input: Array<CharArray>): Int {
        return 0
    }

    val testInput = readInputParsed("sample")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInputParsed("input")
    part1(input).dump("part1: ")
    part2(input).dump("part2: ")
}

fun readInputParsed(name: String) = readInput(4, name).map{line -> line.toCharArray()}.toTypedArray()