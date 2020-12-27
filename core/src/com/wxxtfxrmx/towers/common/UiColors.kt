package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.graphics.Color

object UiColors {

    val WHITE = Color(240f, 246f, 240f, 1f)
    val BLACK = Color(34f, 35f, 35f, 1f)
}

fun Color.toOpenGl(): Triple<Float, Float, Float> =
        Triple(first = r / 255f, second = g / 255f, third = b / 255f)