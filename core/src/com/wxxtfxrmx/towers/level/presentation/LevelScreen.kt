package com.wxxtfxrmx.towers.level.presentation

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.common.shader.shader
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.OrderComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent
import com.wxxtfxrmx.towers.level.model.GroundTexture
import com.wxxtfxrmx.towers.level.model.Uniform3f
import com.wxxtfxrmx.towers.level.system.AccumulateElapsedTimeSystem
import com.wxxtfxrmx.towers.level.system.rendering.RenderingSystem
import kotlin.random.Random

class LevelScreen(
        private val textureAtlas: TextureAtlas,
        shapeRendererFactory: ShapeRendererFactory,
) : BaseScreen() {

    private val engine = PooledEngine()
    private val inputSystems = mutableListOf<EntitySystem>()
    private val logicSystems = mutableListOf<EntitySystem>()
    private val renderingSystems = mutableListOf<EntitySystem>()
    private val random = Random(888L)


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
        engine.addEntities(foundationEntities())
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


    private fun foundationEntities(): List<Entity> {
        val complexGrounds = GroundTexture.values().toMutableList()
                .apply { remove(GroundTexture.SLICE_5) }

        val entities = mutableListOf<Entity>()

        for (x in 0 until UiConstants.WIDTH.toInt() step UiConstants.UNIT) {
            for (y in 0 until UiConstants.HALF_HEIGHT.toInt() step UiConstants.UNIT) {
                val entity = engine.createEntity()

                val groundTexture = if (random.nextBoolean()) {
                    GroundTexture.SLICE_5
                } else {
                    complexGrounds.random()
                }

                val texture = textureAtlas.region(groundTexture.textureName)

                val textureComponent: TextureComponent = engine.component {
                    this.texture = texture
                }

                val orderComponent: OrderComponent = engine.component {
                    order = Int.MAX_VALUE - 1
                }

                val boundsComponent: BoundsComponent = engine.component {
                    bounds.set(x.toFloat(), y.toFloat(), texture.regionWidth.toFloat(), texture.regionHeight.toFloat())
                }

                entity.addComponents(textureComponent, orderComponent, boundsComponent)
                entities.add(entity)
            }
        }

        return entities
    }

    private fun backgroundEntity(): Entity {
        val entity = engine.createEntity()

        val shaderComponent: ShaderComponent = engine.component {
            shader = shader(
                    vertexSource = "shaders/background/vertex.glsl",
                    fragmentSource = "shaders/background/fragment.glsl",
            )

            uniforms = listOf(
                    Uniform3f("u_color", 0.80f, 0.72f, 0.58f),
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