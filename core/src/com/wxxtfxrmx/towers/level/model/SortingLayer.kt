package com.wxxtfxrmx.towers.level.model

enum class SortingLayer(val layer: Int) {
    BACKGROUND(Int.MAX_VALUE),
    MIDDLE(Int.MAX_VALUE - 1),
    FRONT(Int.MAX_VALUE - 2),
}