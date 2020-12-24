package com.wxxtfxrmx.towers.level.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.mapper
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.OrderComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent

class RenderingSystem(
        private val batch: SpriteBatch,
        private val shapeRendererFactory: ShapeRendererFactory,
) : IteratingSystem(
        Family.all(BoundsComponent::class.java)
                .one(ShaderComponent::class.java, TextureComponent::class.java)
                .get()
) {

    private val shaderMapper = mapper(ShaderComponent::class)
    private val textureMapper = mapper(TextureComponent::class)

    private val renderQueue = mutableListOf<Entity>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        renderQueue.sortByDescending { entity -> entity.component<OrderComponent>().order }

        renderQueue.forEach { entity ->
            val boundsComponent: BoundsComponent = entity.component()

            when {
                shaderMapper.has(entity) -> {
                    entity.component<ShaderComponent>().render(shapeRendererFactory, boundsComponent)
                }

                textureMapper.has(entity) -> {
                    entity.component<TextureComponent>().render(boundsComponent)
                }
            }
        }

        renderQueue.clear()
    }

    private fun ShaderComponent.render(shapeRendererFactory: ShapeRendererFactory, boundsComponent: BoundsComponent) {
        val renderer = shader?.let(shapeRendererFactory::get) ?: return
        val bounds = boundsComponent.bounds
        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
        renderer.end()
    }

    private fun TextureComponent.render(boundsComponent: BoundsComponent) {
        texture ?: return
        val bounds = boundsComponent.bounds

        batch.begin()
        batch.draw(texture,
                bounds.x, bounds.y,
                bounds.x + bounds.width * 0.5f, bounds.y + bounds.height * 0.5f,
                bounds.width, bounds.height,
                1f, 1f,
                0f
        )
        batch.end()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderQueue.add(entity)
    }
}