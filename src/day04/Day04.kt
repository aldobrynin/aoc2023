package day04

import common.Puzzle
import solveAndVerify
import toIntArray

private data class Card(val id: Int, val winningCards: Set<Int>, val hand: List<Int>) {
    val matchCount = hand.intersect(winningCards).size
}

private class Day04 : Puzzle<List<Card>>(day = 4) {
    override fun parse(rawInput: List<String>): List<Card> {
        fun parseLine(line: String): Card {
            val reg = Regex("Card\\s+(?<id>\\d+): (.*) \\| (.*)")
            val (id, win, hand) = reg.find(line)!!.destructured
            return Card(id.toInt(), win.toIntArray().toSet(), hand.toIntArray())
        }
        return rawInput.map(::parseLine)
    }

    override fun part1(input: List<Card>): Long = input.sumOf {
        when {
            it.matchCount > 0 -> 1 shl (it.matchCount - 1)
            else -> 0
        }.toLong()
    }

    override fun part2(input: List<Card>): Long {
        val counts = input.map { 1L }.toTypedArray()
        for (card in input)
            for (copy in 1..card.matchCount)
                counts[card.id - 1 + copy] += counts[card.id - 1]
        return counts.sum()
    }
}

fun main() {
    val puzzle = Day04()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 13)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 30)

    puzzle.run()
}