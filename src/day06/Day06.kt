package day06

import dump
import product
import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt
import kotlin.time.measureTime

fun main() {
    fun solve(time: Long, distance: Long): Long {
        val discriminant = sqrt(1.0 * time * time - 4 * distance)
        val x1 = (time + discriminant) / 2
        val x2 = (time - discriminant) / 2

        return (ceil(x1) - 1 - floor(x2)).toLong()
    }

    fun part1(input: List<String>): Long {
        val (times, distances) = input.map { line ->
            line.split(':')[1].split(' ').filterNot { it.isBlank() }.map { it.toLong() }
        }

        return times.zip(distances) { a, b -> solve(a, b) }.product()
    }

    fun part2(input: List<String>): Long {
        val (totalTime, totalDistance) = input.map { line -> line.split(':')[1].replace(" ", "").toLong() }
        return solve(totalTime, totalDistance)
    }

    val testInput = readInputParsed("sample")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val executionTime = measureTime {
        val input = readInputParsed("input")
        part1(input).dump("part1: ")
        part2(input).dump("part2: ")
    }
    executionTime.dump("Executed in ")
}

fun readInputParsed(name: String) = readInput(6, name)