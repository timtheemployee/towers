package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.mapper
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.RotationComponent
import com.wxxtfxrmx.towers.level.component.ScaleComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent

class TextureRendering(private val batch: SpriteBatch) {

    private val rotationMapper = mapper(RotationComponent::class)
    private val scaleMapper = mapper(ScaleComponent::class)

    fun render(entity: Entity) {
        val textureComponent: TextureComponent = entity.component()
        val texture = textureComponent.texture ?: return
        val boundsComponent: BoundsComponent = entity.component()
        val bounds = boundsComponent.bounds

        val rotation = if (rotationMapper.has(entity)) {
            entity.component<RotationComponent>().angle
        } else {
            0f
        }

        val scale: Vector2? = if (scaleMapper.has(entity)) {
            entity.component<ScaleComponent>().scale
        } else {
            null
        }

        batch.begin()
        batch.draw(
                texture,
                bounds.x, bounds.y,
                bounds.width * 0.5f, bounds.height * 0.5f,
                bounds.width, bounds.height,
                scale?.x ?: 1f, scale?.y ?: 1f,
                rotation
        )
        batch.end()
    }
}