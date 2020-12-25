package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent

class TextureRendering(private val batch: SpriteBatch) {

    fun render(entity: Entity) {
        val textureComponent: TextureComponent = entity.component()
        val texture = textureComponent.texture ?: return
        val boundsComponent: BoundsComponent = entity.component()
        val bounds = boundsComponent.bounds

        batch.begin()
        batch.draw(
                texture,
                bounds.x, bounds.y,
                bounds.x + bounds.width * 0.5f, bounds.y + bounds.height * 0.5f,
                bounds.width, bounds.height,
                1f, 1f,
                0f
        )
        batch.end()
    }
}