package day06

import common.Puzzle
import product
import solveAndVerify
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt

private class Day06 : Puzzle<List<String>>(day = 6) {
    override fun parse(rawInput: List<String>): List<String> = rawInput

    override fun part1(input: List<String>): Long {
        val (times, distances) = input.map { line ->
            line.split(':')[1].split(' ').filterNot { it.isBlank() }.map { it.toLong() }
        }

        return times.zip(distances) { a, b -> solve(a, b) }.product()
    }

    override fun part2(input: List<String>): Long {
        val (totalTime, totalDistance) = input.map { line -> line.split(':')[1].replace(" ", "").toLong() }
        return solve(totalTime, totalDistance)
    }

    private fun solve(time: Long, distance: Long): Long {
        val discriminant = sqrt(1.0 * time * time - 4 * distance)
        val x1 = (time + discriminant) / 2
        val x2 = (time - discriminant) / 2

        return (ceil(x1) - 1 - floor(x2)).toLong()
    }
}

fun main() {
    val puzzle = Day06()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 288)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 71503)

    puzzle.run()
}