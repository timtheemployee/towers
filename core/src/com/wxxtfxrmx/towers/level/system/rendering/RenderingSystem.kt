package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.Array
import com.wxxtfxrmx.towers.common.Body2DBoundsCalculator
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.mapper
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.*
import com.wxxtfxrmx.towers.level.model.*

class RenderingSystem(
        private val batch: SpriteBatch,
) : IteratingSystem(
        Family.all(BodyComponent::class.java, SortingLayerComponent::class.java)
                .one(TextureComponent::class.java, ShaderComponent::class.java, ColorComponent::class.java).get()
) {

    private val textureMapper = mapper(TextureComponent::class)

    private val bodyBoundsCalculator = Body2DBoundsCalculator()

    private val renderQueue = Array<Entity>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        renderQueue.sortedBy { it.component<SortingLayerComponent>().layer }

        renderQueue.forEach { entity ->
            val bodyComponent: BodyComponent = entity.component()
            val body = bodyComponent.body
            requireNotNull(body)

            when {
                textureMapper.has(entity) -> {
                    val textureComponent: TextureComponent = entity.component()
                    val texture = textureComponent.texture
                    requireNotNull(texture)

                    render(batch, body, texture)
                }
            }
        }

        renderQueue.clear()
    }

    private fun render(batch: SpriteBatch, body: Body, texture: TextureRegion) {
        val (width, height) = bodyBoundsCalculator.getBounds(body)

        Gdx.app.log("TAG", "body position is ${body.position.x}, ${body.position.y}")

        batch.apply {
            begin()
            draw(texture,
                    body.position.x, body.position.y,
                    width * 0.5f, height * 0.5f,
                    width, height,
                    1f, 1f,
                    body.angle)
            end()
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderQueue.add(entity)
    }
}