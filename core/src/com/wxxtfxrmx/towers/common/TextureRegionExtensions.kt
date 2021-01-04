package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.graphics.g2d.TextureRegion

val TextureRegion.widthInMeters: Float
    get() = regionWidth / UiConstants.PPM_F

val TextureRegion.heightInMeters: Float
    get() = regionHeight / UiConstants.PPM_F

val TextureRegion.halfWidthInMeters: Float
    get() = widthInMeters * 0.5f

val TextureRegion.halfHeightInMeters: Float
    get() = heightInMeters * 0.5f