package com.github.nort3x.backendchallenge2.utils

fun Char.alpha2Int(): Int {
    return this.lowercaseChar().code - 97
}