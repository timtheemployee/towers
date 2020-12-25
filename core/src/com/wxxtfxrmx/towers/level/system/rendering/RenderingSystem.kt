package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.mapper
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.OrderComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent

class RenderingSystem(batch: SpriteBatch, shapeRendererFactory: ShapeRendererFactory) : IteratingSystem(
        Family.all(BoundsComponent::class.java)
                .one(ShaderComponent::class.java, TextureComponent::class.java)
                .get()
) {

    private val shaderMapper = mapper(ShaderComponent::class)
    private val textureMapper = mapper(TextureComponent::class)

    private val shaderRendering = ShaderRendering(shapeRendererFactory)
    private val textureRendering = TextureRendering(batch)

    private val renderQueue = mutableListOf<Entity>()

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        renderQueue.sortByDescending { entity -> entity.component<OrderComponent>().order }

        renderQueue.forEach { entity ->
            when {
                shaderMapper.has(entity) -> shaderRendering.render(entity)
                textureMapper.has(entity) -> textureRendering.render(entity)
            }
        }

        renderQueue.clear()
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        renderQueue.add(entity)
    }
}