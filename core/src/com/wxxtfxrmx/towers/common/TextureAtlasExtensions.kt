package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.wxxtfxrmx.towers.level.model.TowersTexture

fun TextureAtlas.region(name: String): TextureRegion =
        TextureRegion(findRegion(name))

fun TextureAtlas.region(towersTexture: TowersTexture): TextureRegion =
        TextureRegion(findRegion(towersTexture.regionName))