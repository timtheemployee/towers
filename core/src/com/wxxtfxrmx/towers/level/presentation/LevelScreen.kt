package com.wxxtfxrmx.towers.level.presentation

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.common.shader.shader
import com.wxxtfxrmx.towers.level.component.*
import com.wxxtfxrmx.towers.level.model.TowersTexture
import com.wxxtfxrmx.towers.level.model.Uniform2f
import com.wxxtfxrmx.towers.level.model.Uniform3f
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
        renderingSystems.add(
                RenderingSystem(batch, shapeRendererFactory)
        )

        inputSystems.forEach(engine::addSystem)
        logicSystems.forEach(engine::addSystem)
        renderingSystems.forEach(engine::addSystem)

        engine.addEntity(backgroundEntity())
        engine.addEntities(fenceEntities())
        engine.addEntity(keepOutSignEntity())
        engine.addEntity(alertSignsEntity())
        engine.addEntities(groundEntities())
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


    private fun fenceEntities(): List<Entity> {
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val entities = mutableListOf<Entity>()

        val textureComponent: TextureComponent = engine.component {
            this.texture = fenceTexture
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE - 1
        }

        for (x in 0 until UiConstants.WIDTH.toInt() step UiConstants.HALF_UNIT) {
            val entity = engine.createEntity()

            val boundsComponent: BoundsComponent = engine.component {
                //16f - ground height
                bounds.set(x.toFloat(), 16f, fenceTexture.regionWidth.toFloat(), fenceTexture.regionHeight.toFloat())
            }

            entity.addComponents(textureComponent, orderComponent, boundsComponent)
            entities.add(entity)
        }

        return entities
    }

    private fun keepOutSignEntity(): Entity {
        val keepOutSignEntity = engine.createEntity()
        val signTexture = textureAtlas.region(TowersTexture.KEEP_OUT_SIGN)

        val textureComponent: TextureComponent = engine.component {
            texture = signTexture
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE - 2
        }

        val boundsComponent: BoundsComponent = engine.component {
            bounds.set(UiConstants.HALF_WIDTH + UiConstants.UNIT, UiConstants.HALF_UNIT.toFloat(),
                    signTexture.regionWidth.toFloat(), signTexture.regionHeight.toFloat())
        }

        keepOutSignEntity.addComponents(textureComponent, orderComponent, boundsComponent)

        return keepOutSignEntity
    }

    private fun alertSignsEntity(): Entity {
        val alertTexture = textureAtlas.region(TowersTexture.ALERT_SIGN)

        val entity = engine.createEntity()

        val textureComponent: TextureComponent = engine.component {
            texture = alertTexture
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE - 2
        }

        val scaleComponent: ScaleComponent = engine.component {
            scale.set(0.7f, 0.7f)
        }

        val boundsComponent: BoundsComponent = engine.component {
            bounds.set(UiConstants.HALF_UNIT.toFloat(), UiConstants.HALF_UNIT.toFloat(),
                    alertTexture.regionWidth.toFloat(), alertTexture.regionHeight.toFloat())
        }

        entity.addComponents(textureComponent, orderComponent, scaleComponent, boundsComponent)

        return entity
    }

    private fun groundEntities(): List<Entity> {
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)
        val entities = mutableListOf<Entity>()
        val textureComponent: TextureComponent = engine.component {
            texture = groundTexture
        }

        val orderComponent: OrderComponent = engine.component {
            order = Int.MAX_VALUE - 2
        }

        for (x in 0..UiConstants.WIDTH.toInt() step 16) {
            val entity = engine.createEntity()

            val boundsComponent: BoundsComponent = engine.component {
                bounds.set(x.toFloat(), 0f, groundTexture.regionWidth.toFloat(), groundTexture.regionHeight.toFloat())
            }

            entity.addComponents(textureComponent, orderComponent, boundsComponent)
            entities.add(entity)
        }

        return entities
    }

    private fun backgroundEntity(): Entity {
        val entity = engine.createEntity()

        val shaderComponent: ShaderComponent = engine.component {
            shader = shader(
                    vertexSource = "shaders/background/vertex.glsl",
                    fragmentSource = "shaders/background/towers_background.glsl",
            )

            val background = UiColors.BLACK.toOpenGl()

            uniforms = listOf(
                    Uniform2f("u_resolution", UiConstants.WIDTH, UiConstants.HEIGHT),
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