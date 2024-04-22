package com.nestor.spentify

import kotlin.math.abs

const val magicNumber = 15

/**
 * rowsum will be [0:15, 1: 15, 2:15]
 * colum same
 * and diagSum will be [0:15, 1:15]
 */
fun isSquareMagic(square: List<List<Int>>) {
    val rowSum = hashMapOf<Int, Int>()
    val colSum = hashMapOf<Int, Int>()
    val diagSum = hashMapOf<Int, Int>()
    val rest = Array(9) { 0 }
    square.forEachIndexed { j, row ->
        row.forEachIndexed { k, item ->
            rowSum[j] = rowSum.getOrDefault(j, 0) + item
            colSum[k] = colSum.getOrDefault(k, 0) + item
            if (abs(j - k) == 2 || (j == 1 && k == 1)) {
                diagSum[1] = diagSum.getOrDefault(1, 0) + item
            }
            if (j == k) {
                diagSum[0] = diagSum.getOrDefault(0, 0) + item
            }
        }
    }
    rowSum.values.forEach {  }
    println(rowSum)
    println(colSum)
    println(diagSum)
}

fun magicSquare(vararg items: Int): List<List<Int>> {
    return items.asList().chunked(3)
}

fun main() {
    val magicSquare = magicSquare(4, 9, 2, 3, 5, 7, 8, 1, 5)
    println(magicSquare)
    isSquareMagic(magicSquare)
}
