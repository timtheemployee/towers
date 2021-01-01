package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Array
import com.wxxtfxrmx.towers.common.Body2DBoundsCalculator
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent

class RenderingSystem(
        private val batch: SpriteBatch,
) : IteratingSystem(
        Family.all(BodyComponent::class.java).one(TextureComponent::class.java).get()
) {
    private val bodyBoundsCalculator = Body2DBoundsCalculator()
    private val renderQueue = Array<Entity>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        renderQueue.forEach { entity ->
            val bodyComponent: BodyComponent = entity.component()
            val body = bodyComponent.body
            requireNotNull(body)

            val textureComponent: TextureComponent = entity.component()
            val texture = textureComponent.texture
            requireNotNull(texture)

            val (width, height) = bodyBoundsCalculator.getBounds(body)

            batch.begin()
            batch.draw(
                    texture,
                    body.position.x, body.position.y,
                    width * 0.5f, height * 0.5f,
                    width, height,
                    1f, 1f,
                    body.angle
            )
            batch.end()
        }

        renderQueue.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderQueue.add(entity)
    }
}