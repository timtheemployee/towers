package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

fun TextureAtlas.region(name: String): TextureRegion =
        TextureRegion(findRegion(name))