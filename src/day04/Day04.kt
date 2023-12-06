package day04

import dump
import measureAndPrint
import readInput
import solveAndVerify

data class Card(val id: Int, val winningCards: Set<Int>, val hand: List<Int>){
    val matchCount = hand.intersect(winningCards).size
}

fun main() {
    fun part1(input: List<Card>): Int = input.sumOf {
        when {
            it.matchCount > 0 -> 1 shl (it.matchCount - 1)
            else -> 0
        }
    }

    fun part2(input: List<Card>): Int {
        val counts = input.map { 1 }.toIntArray()
        for (card in input)
            for (copy in 1..card.matchCount)
                counts[card.id - 1 + copy] += counts[card.id - 1]
        return counts.sum()
    }

    val testInput = readInputParsed("sample")
    solveAndVerify(testInput, ::part1, expected = 13)
    solveAndVerify(testInput, ::part2, expected = 30)

    val input = readInputParsed("input")
    measureAndPrint { part1(input).dump("part1: ") }
    measureAndPrint { part2(input).dump("part2: ") }
}

fun toIntArray(s: String) = s.split(' ').filterNot { it.isBlank() }.map { it.toInt() }

fun parseLine(line: String): Card {
    val reg = Regex("Card\\s+(?<id>\\d+): (.*) \\| (.*)")
    val (id, win, hand) = reg.find(line)!!.destructured
    return Card(id.toInt(), toIntArray(win).toSet(), toIntArray(hand))
}

fun readInputParsed(name: String) = readInput(4, name).map(::parseLine)