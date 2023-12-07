package day07

import common.Puzzle
import solveAndVerify

private data class Hand(val cards: String, val bid: Long) {
    val cardsScore = getCardsScore(cards)
    val cardsScoreWithWildcard = getCardsScoreWithWildcard(cards)
    val handType = getHandTypeScore(cards)
    val handTypeWithWildcards = getBestHandTypeWithWildcards(cards)

    companion object {
        fun parse(input: String): Hand {
            val (cards, bid) = input.split(' ', limit = 2)
            return Hand(cards, bid.toLong())
        }

        private fun getHandTypeScore(cards: String): Int {
            val counts = cards.groupingBy { it }.eachCount().map { it.value }.sortedDescending()
            return when (counts) {
                listOf(5) -> 6
                listOf(4, 1) -> 5
                listOf(3, 2) -> 4
                listOf(3, 1, 1) -> 3
                listOf(2, 2, 1) -> 2
                listOf(2, 1, 1, 1) -> 1
                else -> 0
            }
        }

        private fun getBestHandTypeWithWildcards(cards: String): Int {
            if (!cards.contains('J')) return getHandTypeScore(cards)
            val parts = cards.split('J')
            return "23456789TQKA".maxOf { getHandTypeScore(parts.joinToString(it.toString())) }
        }

        private fun getCardsScore(cards: String): Long {
            return cards.map { "23456789TJQKA".indexOf(it) }.fold(1L) { acc, cur -> acc * 100 + cur }
        }

        private fun getCardsScoreWithWildcard(cards: String): Long {
            return cards.map { "J23456789TQKA".indexOf(it) }.fold(1L) { acc, cur -> acc * 100 + cur }
        }
    }
}

private typealias ParsedInput = List<Hand>

private class Day07 : Puzzle<ParsedInput>(day = 7) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map(Hand.Companion::parse)
    private val part1Comparator = Comparator.comparingInt(Hand::handType).thenComparingLong(Hand::cardsScore)
    private val part2Comparator =
        Comparator.comparingInt(Hand::handTypeWithWildcards).thenComparingLong(Hand::cardsScoreWithWildcard)

    override fun part1(input: ParsedInput): Long {
        return input.sortedWith(part1Comparator).mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }

    override fun part2(input: ParsedInput): Long {
        return input.sortedWith(part2Comparator).mapIndexed { index, hand -> (index + 1) * hand.bid }.sum()
    }
}

fun main() {
    val puzzle = Day07()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 6440)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 5905)

    puzzle.run()
}