package com.wxxtfxrmx.towers.level.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.component.SortingLayerComponent
import com.wxxtfxrmx.towers.level.component.TextureComponent
import com.wxxtfxrmx.towers.level.model.SortingLayer
import com.wxxtfxrmx.towers.level.model.TowersTexture
import com.wxxtfxrmx.towers.level.model.Uniform
import com.wxxtfxrmx.towers.level.model.Uniform3f
import com.wxxtfxrmx.towers.level.system.rendering.RenderingSystem

class LevelScreen(
        private val textureAtlas: TextureAtlas,
        private val shapeFactory: ShapeFactory,
        shapeRendererFactory: ShapeRendererFactory,
) : BaseScreen() {

    private val world = World(Vector2(0f, -100.0f), true)
    private val engine = PooledEngine()
    private val renderingSystem = RenderingSystem(batch)
    private val box2DDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)

    init {
        engine.addSystem(renderingSystem)

        engine.addEntities(fenceEntities())
        engine.addEntities(groundEntities())
        engine.addEntities(signsEntities())
        engine.addEntity(shapeEntity())
    }

    override fun render(delta: Float) {
        super.render(delta)

        engine.update(delta)
        world.step(delta, 4, 4)
        box2DDebugRenderer.render(world, camera.combined)
    }

    override fun pause() {
        super.pause()
        engine.systems.forEach { it.setProcessing(false) }
    }

    override fun resume() {
        super.resume()

        engine.systems.forEach { it.setProcessing(true) }
    }

    private fun fenceEntities() : List<Entity> {
        val fences = mutableListOf<Entity>()
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)

        val textureComponent: TextureComponent = engine.component {
            texture = fenceTexture
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val polygon = PolygonShape().apply {
            setAsBox(fenceTexture.regionWidth.toFloat(), fenceTexture.regionHeight.toFloat())
        }

        for (i in 0..UiConstants.WIDTH.toInt() step 4 * fenceTexture.regionWidth) {
            val entity = engine.createEntity()

            val body = BodyDef().apply {
                type = BodyDef.BodyType.StaticBody
                position.set(i.toFloat(), 16f)
                fixedRotation = true
            }.let(world::createBody)

            body.createFixture(polygon, 0f)

            val bodyComponent: BodyComponent = engine.component {
                this.body = body
            }

            entity.addComponents(bodyComponent, textureComponent, sortingLayerComponent)
            fences.add(entity)
        }

        polygon.dispose()
        return fences
    }

    private fun groundEntities(): List<Entity> {
        val ground = mutableListOf<Entity>()
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val textureComponent: TextureComponent = engine.component {
            texture = groundTexture
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val polygon = PolygonShape().apply {
            setAsBox(groundTexture.regionWidth.toFloat(), groundTexture.regionHeight.toFloat())
        }

        for (i in 0..UiConstants.WIDTH.toInt() step groundTexture.regionWidth) {
            val entity = engine.createEntity()

            val body = BodyDef().apply {
                type = BodyDef.BodyType.StaticBody
                position.set(i.toFloat(), 16f)
                fixedRotation = true
            }.let(world::createBody)

            body.createFixture(polygon, 0f)

            val bodyComponent: BodyComponent = engine.component {
                this.body = body
            }

            entity.addComponents(bodyComponent, textureComponent, sortingLayerComponent)
            ground.add(entity)
        }

        polygon.dispose()
        return ground
    }

    private fun signsEntities(): List<Entity> {
        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val alertTexture = textureAtlas.region(TowersTexture.ALERT_SIGN)

        val alertTextureComponent: TextureComponent = engine.component {
            texture = alertTexture
        }

        val alertSignShape = PolygonShape().apply {
            setAsBox(alertTexture.regionWidth.toFloat(), alertTexture.regionHeight.toFloat())
        }

        val alertSignBody = BodyDef().apply {
            type = BodyDef.BodyType.StaticBody
            position.set(UiConstants.HALF_UNIT.toFloat(), UiConstants.UNIT.toFloat())
            angle = 15f
            fixedRotation = true
        }.let(world::createBody)

        alertSignBody.createFixture(alertSignShape, 0f)

        val alertBodyComponent: BodyComponent = engine.component {
            this.body = alertSignBody
        }

        val alertEntity = engine.createEntity()

        alertEntity.addComponents(alertTextureComponent, alertBodyComponent, sortingLayerComponent)


        val keepOutSignTexture = textureAtlas.region(TowersTexture.KEEP_OUT_SIGN)

        val keepOutSignTextureComponent: TextureComponent = engine.component {
            texture = keepOutSignTexture
        }

        val keepOutSignShape = PolygonShape().apply {
            setAsBox(keepOutSignTexture.regionWidth.toFloat(), keepOutSignTexture.regionHeight.toFloat())
        }

        val keepOutSignBody = BodyDef().apply {
            type = BodyDef.BodyType.StaticBody
            position.set(UiConstants.WIDTH - 2 * UiConstants.UNIT, UiConstants.HALF_UNIT.toFloat() + 16)
            fixedRotation = true
        }.let(world::createBody)

        keepOutSignBody.createFixture(keepOutSignShape, 0f)

        val keepOutSignBodyComponent: BodyComponent = engine.component {
            this.body = keepOutSignBody
        }

        val keepOutEntity = engine.createEntity()

        keepOutEntity.addComponents(keepOutSignTextureComponent, keepOutSignBodyComponent, sortingLayerComponent)

        return listOf(alertEntity, keepOutEntity)
    }

    private fun shapeEntity(): Entity {
        val entity = engine.createEntity()

        val shape = textureAtlas.region(TowersTexture.FLOOR_V1)

        val textureComponent: TextureComponent = engine.component {
            texture = shape
        }

        val body = BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set(UiConstants.HALF_WIDTH, UiConstants.HEIGHT)
            active = true
            gravityScale = 2f
        }.let(world::createBody)

        val polygon = PolygonShape().apply {
            setAsBox(shape.regionWidth.toFloat(), shape.regionHeight.toFloat())
        }

        body.createFixture(polygon, 0f)

        val mass = MassData().apply {
            mass = 100f
        }

        body.massData = mass

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.FRONT
        }

        entity.addComponents(bodyComponent, textureComponent, sortingLayerComponent)

        return entity
    }
}