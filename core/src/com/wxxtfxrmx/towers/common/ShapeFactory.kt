package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

private data class ShapeKey(val width: Int, val height: Int, val color: Color)

class ShapeFactory {

    private val shapeCache = mutableMapOf<ShapeKey, Texture>()

    fun rectangle(width: Int, height: Int, color: Color): TextureRegion {
        val shapeKey = ShapeKey(width, height, color)

        return if (shapeCache.containsKey(shapeKey)) {
            val texture = shapeCache[shapeKey]
            requireNotNull(texture)
            TextureRegion(texture)
        } else {
            val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
            pixmap.setColor(color)
            pixmap.fillRectangle(0, 0, width, height)
            val texture = Texture(pixmap)

            shapeCache[shapeKey] = texture
            return TextureRegion(texture)
        }
    }
}