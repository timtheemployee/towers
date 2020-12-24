package com.wxxtfxrmx.towers.common.shader

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

private data class RendererKey(val shader: ShaderProgram)

class ShapeRendererFactory {

    private val rendererCache = mutableMapOf<RendererKey, ShapeRenderer>()

    fun get(shader: ShaderProgram): ShapeRenderer {
        val rendererKey = RendererKey(shader)

        return rendererCache[rendererKey] ?: ShapeRenderer(5000, shader)
                .apply { rendererCache[rendererKey] = this }
    }
}