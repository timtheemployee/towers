package com.wxxtfxrmx.towers.level.presentation

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.OrderComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent
import com.wxxtfxrmx.towers.level.model.*
import com.wxxtfxrmx.towers.level.system.AccumulateElapsedTimeSystem
import com.wxxtfxrmx.towers.level.system.rendering.RenderingSystem

class LevelScreen(
        private val textureAtlas: TextureAtlas,
        shapeRendererFactory: ShapeRendererFactory,
) : BaseScreen() {

    private val engine = PooledEngine()
    private val inputSystems = mutableListOf<EntitySystem>()
    private val logicSystems = mutableListOf<EntitySystem>()
    private val renderingSystems = mutableListOf<EntitySystem>()

    init {
        logicSystems.add(
                AccumulateElapsedTimeSystem()
        )

        renderingSystems.add(
                RenderingSystem(batch, shapeRendererFactory)
        )

        inputSystems.forEach(engine::addSystem)
        logicSystems.forEach(engine::addSystem)
        renderingSystems.forEach(engine::addSystem)

        engine.addEntity(backgroundEntity())
        engine.addEntity(foundationEntity())
    }

    override fun render(delta: Float) {
        super.render(delta)
        engine.update(delta)
    }

    override fun pause() {
        super.pause()
        engine.systems.forEach { it.setProcessing(false) }
    }

    override fun resume() {
        super.resume()
        engine.systems.forEach { it.setProcessing(true) }
    }

    private fun foundationEntity() : Entity {
        val entity = engine.createEntity()
        val texture = textureAtlas.findRegion("foundation")

        val textureComponent: TextureComponent = engine.component {
            this.texture = texture
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE - 1
        }

        val boundsComponent: BoundsComponent = engine.component {
            bounds.set(0f, 0f, texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
        }

        entity.addComponents(textureComponent, orderComponent, boundsComponent)

        return entity
    }

    private fun backgroundEntity() : Entity {
        val entity = engine.createEntity()

        val shaderComponent: ShaderComponent = engine.component {
            shader = shader(
                    vertexSource = "shaders/background/vertex.glsl",
                    fragmentSource = "shaders/background/fragment.glsl",
            )

            uniforms = listOf(
                    Uniform2f("u_resolution", UiConstants.WIDTH, UiConstants.HEIGHT),
                    Uniform3f("u_bottom_color", 0.45f, 0.44f, 0.36f),
                    Uniform3f("u_top_color", 0.34f, 0.59f, 1.0f),
            )
        }

        val boundsComponent: BoundsComponent = engine.component {
            bounds.set(0f, 0f, UiConstants.WIDTH, UiConstants.HEIGHT)
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE
        }

        entity.addComponents(shaderComponent, boundsComponent, orderComponent)

        return entity
    }
}