package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Array
import com.wxxtfxrmx.towers.common.Body2DBoundsCalculator
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.SortingLayerComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent

class RenderingSystem(
        private val batch: SpriteBatch,
) : IteratingSystem(
        Family.all(BodyComponent::class.java).one(SpriteComponent::class.java).get()
) {
    private val renderQueue = Array<Entity>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

//        renderQueue.sortedBy { it.component<SortingLayerComponent>().layer }

        renderQueue.forEach { entity ->
            val bodyComponent: BodyComponent = entity.component()
            val body = bodyComponent.body
            requireNotNull(body)

            val spriteComponent: SpriteComponent = entity.component()
            val sprite = spriteComponent.sprite
            requireNotNull(sprite)

            batch.begin()
            sprite.draw(batch)
            batch.end()
        }

        renderQueue.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderQueue.add(entity)
    }
}