package day19

import common.Puzzle
import product
import solveAndVerify

private typealias ParsedInput = List<String>

private class Day19 : Puzzle<ParsedInput>(day = 19) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput

    override fun part1(input: ParsedInput): Long {
        val workflows = input.takeWhile { it.isNotBlank() }.map { Workflow.parse(it) }.associateBy { it.name }
        val parts = input.drop(workflows.size + 1).map { line ->
            line.split('{', '}', ',', '=').filter { it.isNotBlank() }.chunked(2)
                .associateBy({ it[0] }, { it[1].toLong() })
        }

        fun isAccepted(part: Map<String, Long>, workflowName: String = "in"): Boolean = when (workflowName) {
            "R" -> false
            "A" -> true
            else -> isAccepted(part, workflows[workflowName]!!.getNext(part))
        }

        return parts.filter { isAccepted(it) }.sumOf { it.values.sum() }
    }

    override fun part2(input: ParsedInput): Long {
        val workflows = input.takeWhile { it.isNotBlank() }.map { Workflow.parse(it) }.associateBy { it.name }

        fun countAccepted(workflowName: String, currentRatings: Map<String, IntRange>): Long {
            if (workflowName == "R") return 0
            if (workflowName == "A") return currentRatings.values.map { 1L + it.last.toLong() - it.first }.product()

            val workflow = workflows[workflowName]!!
            var count = 0L

            var ratings = currentRatings
            for ((ratingName, operator, value, nextWorkflow) in workflow.rules) {
                val (matchRange, elseRange) = ratings.getValue(ratingName).match(value, operator)
                if (!matchRange.isEmpty())
                    count += countAccepted(nextWorkflow, ratings.mutate(ratingName, matchRange))

                if (elseRange.isEmpty()) break
                ratings = ratings.mutate(ratingName, elseRange)
            }
            count += countAccepted(workflow.fallback, ratings)
            return count
        }

        return countAccepted("in", "xmas".associate { it.toString() to 1..4000 })
    }

    fun IntRange.match(value: Int, operator: String): Pair<IntRange, IntRange> = when (operator) {
        ">" -> value + 1..this.last to this.first..value
        "<" -> this.first..<value to value..this.last
        else -> throw Exception("Invalid operator $operator")
    }

    fun <T, K> Map<T, K>.mutate(key: T, value: K): Map<T, K> = this.toMutableMap().also { it[key] = value }

    private data class Condition(
        val ratingName: String,
        val operator: String,
        val value: Int,
        val workflowName: String
    ) {
        fun matches(part: Map<String, Long>): Boolean =
            operator == ">" && part[ratingName]!! > value || operator == "<" && part[ratingName]!! < value
    }

    private data class Workflow(val name: String, val rules: List<Condition>, val fallback: String) {
        companion object {
            fun parse(line: String): Workflow {
                val (name, rest) = line.split('{')
                val conditionsRaw = rest.trim('}').split(',')
                val conditions = conditionsRaw.dropLast(1).map {
                    val (condition, nextWorkflow) = it.split(':')
                    val ratingName = condition.substring(0, 1)
                    val operator = condition.substring(1, 2)
                    val value = condition.substring(2).toInt()
                    Condition(ratingName, operator, value, nextWorkflow)
                }
                return Workflow(name, conditions, conditionsRaw.last())
            }
        }

        fun getNext(part: Map<String, Long>): String = rules.firstOrNull { it.matches(part) }?.workflowName ?: fallback
    }
}

fun main() {
    val puzzle = Day19()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 19114)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 167409079868000)

    puzzle.run()
}