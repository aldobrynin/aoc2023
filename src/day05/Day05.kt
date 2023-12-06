package day05

import common.*
import solveAndVerify
import toLongArray

private data class MapRange(val source: LongRange, val destStart: Long) {
    fun contains(value: Long) = source.contains(value)
    val offset = destStart - source.first

    fun applyOrNull(value: Long): Long? = if (contains(value)) value + offset else null

    fun applyRangeOrEmpty(value: LongRange): LongRange = value.intersectWith(source).shiftBy(offset)
}

private data class Input(val seeds: List<Long>, val mapRanges: List<List<MapRange>>)

private typealias ParsedInput = Input

private class Day05 : Puzzle<ParsedInput>(day = 5) {
    override fun parse(rawInput: List<String>): ParsedInput {
        val seeds = rawInput.first().split(':').last().toLongArray()

        val blocks = rawInput.drop(n = 2)
            .joinToString("\n")
            .split("\n\n")
            .map { x ->
                x.split("\n")
                    .drop(1)
                    .map { it.toLongArray() }
                    .map { MapRange(it[1] until it[1] + it[2], it[0]) }
            }
        return Input(seeds, blocks)
    }

    override fun part1(input: ParsedInput): Long {
        val (seeds, mapRanges) = input
        return seeds.minOf { applyMappings(it, mapRanges) }
    }

    override fun part2(input: ParsedInput): Long {
        val (seeds, mapRanges) = input
        val seedsRanges = seeds.chunked(2).map { it[0] until it[0] + it[1] }

        return applyMappings(seedsRanges, mapRanges).minOf { it.first }
    }

    private fun applyMappings(value: Long, mapRanges: List<List<MapRange>>) =
        mapRanges.fold(value) { seed, ranges -> ranges.firstNotNullOfOrNull { r -> r.applyOrNull(seed) } ?: seed }

    private fun applyMappings(seedRanges: List<LongRange>, mapRanges: List<List<MapRange>>) =
        mapRanges.fold(seedRanges) { ranges, mapping ->
            ranges.flatMap { r ->
                mapping.map { it.applyRangeOrEmpty(r) }
                    .filterNot { it.isEmpty() }
                    .plus(r.excludeRanges(mapping.map { it.source }))
            }.merge()
        }
}

fun main() {
    val puzzle = Day05()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 35)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 46)

    puzzle.run()
}