package day25

import common.Puzzle
import product
import solveAndVerify
import kotlin.math.sqrt

private typealias ParsedInput = List<String>
private typealias Edge = Pair<String, String>

private data class Graph(val vertices: List<String>, val edges: List<Edge>, val vertPower: Map<String, Int> = vertices.associateWith { 1 })

private class Day25 : Puzzle<ParsedInput>(day = 25) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput

    override fun part1(input: ParsedInput): Long {
        val edges = input.map { it.split(':', ' ').filter(String::isNotBlank) }
            .flatMap { x -> x.drop(1).map { x[0] to it } }

        val vertices = edges.flatMap { it.toList() }.distinct()
        val graph = Graph(vertices, edges)
        repeat(100) {
            val contractedGraph = fastMinCut(graph)
            if (contractedGraph.edges.size == 3) {
                return contractedGraph.vertPower.values.product().toLong()
            }
        }

        return -1L
    }

    // https://en.wikipedia.org/wiki/Karger%27s_algorithm
    fun fastMinCut(graph: Graph): Graph {
        if (graph.vertices.size <= 6) return contract(graph, 2)
        else {
            val t = (1 + graph.vertices.size / sqrt(2.0)).toInt()
            return List(size = 2) { fastMinCut(contract(graph, t)) }.minBy { it.edges.size }
        }
    }

    fun contract(graph: Graph, t: Int): Graph {
        val (vertices, edges) = graph
        val components = vertices.associateWith { mutableSetOf(it) }.toMutableMap()
        val vertexToComponent = vertices.associateWith { it }.toMutableMap()
        while (components.size > t) {
            val (from, to) = edges.random()
            val fromComp = vertexToComponent.getValue(from)
            val toComp = vertexToComponent.getValue(to)
            if (fromComp == toComp) continue
            components.getValue(fromComp).addAll(components.getValue(toComp))
            components.remove(toComp)!!.forEach { vertexToComponent[it] = fromComp }
        }

        val newEdges = edges.map { (from, to) -> vertexToComponent[from]!! to vertexToComponent[to]!! }
            .filter { (from, to) -> from != to }

        val vertPower = components.map { (v, set) -> v to set.sumOf { graph.vertPower[it]!! } }.toMap()
        return Graph(components.keys.toList(), newEdges, vertPower)
    }

    override fun part2(input: ParsedInput): Long = 0
}

fun main() {
    val puzzle = Day25()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 54)

    puzzle.run()
}