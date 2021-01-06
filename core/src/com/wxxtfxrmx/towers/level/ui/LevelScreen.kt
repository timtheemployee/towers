package com.wxxtfxrmx.towers.level.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.box2d.FloorContactListener
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.SortingLayerComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent
import com.wxxtfxrmx.towers.level.model.SortingLayer
import com.wxxtfxrmx.towers.level.model.TowersTexture
import com.wxxtfxrmx.towers.level.system.EmitFloorSystem
import com.wxxtfxrmx.towers.level.system.UpdateSpritePositionSystem
import com.wxxtfxrmx.towers.level.system.camera.UpdateCameraPositionSystem
import com.wxxtfxrmx.towers.level.system.camera.UpdateViewportSizeSystem
import com.wxxtfxrmx.towers.level.system.rendering.RenderingSystem

class LevelScreen(
        private val textureAtlas: TextureAtlas,
) : ScreenAdapter() {

    private val camera = OrthographicCamera(UiConstants.WIDTH_F, UiConstants.HEIGHT_F).apply {
        position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F * 0.5f, 0f)
    }

    private val viewport = StretchViewport(UiConstants.WIDTH_F, UiConstants.HEIGHT_F, camera)

    private val b2dDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
    private val world = World(Vector2(0f, -9.8f), true)
    private val engine = PooledEngine()

    private val batch = SpriteBatch()
    private val renderingSystem = RenderingSystem(camera, batch)
    private val updateSpritePositionSystem = UpdateSpritePositionSystem()
    private val emitFloorSystem = EmitFloorSystem(engine, world, textureAtlas, viewport)
    private val updateViewportSizeSystem = UpdateViewportSizeSystem(engine, viewport)
    private val updateCameraPositionSystem = UpdateCameraPositionSystem(camera, viewport)

    init {
        engine.addEntity(foundationEntity())
        engine.addEntity(groundEntities())
        engine.addEntity(fenceEntities())
        engine.addEntity(keepOutSignEntity())
        engine.addEntity(alertEntity())

        engine.addSystem(updateViewportSizeSystem)
        engine.addSystem(updateCameraPositionSystem)
        engine.addSystem(updateSpritePositionSystem)
        engine.addSystem(renderingSystem)
        engine.addSystem(emitFloorSystem)

        world.setContactListener(FloorContactListener(engine))
    }

    private fun foundationEntity(): Entity {
        val foundationTexture = textureAtlas.region(TowersTexture.FOUNDATION)

        val body = BodyDef().apply {
            position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F - foundationTexture.heightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.DynamicBody
        }.let(world::createBody)

        val mass = MassData().apply {
            mass = 100f
            center.set(0f, body.position.y)
        }

        body.massData = mass

        val polygonShape = PolygonShape().apply {
            setAsBox(foundationTexture.halfWidthInMeters, foundationTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 1f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.FRONT
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(foundationTexture).apply {
                setSize(foundationTexture.widthInMeters, foundationTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)

        return entity
    }

    private fun fenceEntities(): Entity {
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(fenceTexture.halfWidthInMeters, fenceTexture.halfHeightInMeters + groundTexture.heightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.StaticBody
            active = false
        }.let(world::createBody)

        val groundShape = PolygonShape().apply {
            setAsBox(fenceTexture.halfWidthInMeters, fenceTexture.halfHeightInMeters)
        }

        body.createFixture(groundShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(fenceTexture).apply {
                setSize(fenceTexture.widthInMeters, fenceTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
                setOrigin(body.position.x, body.position.y)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)

        return entity
    }

    private fun groundEntities(): Entity {
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(groundTexture.halfWidthInMeters, groundTexture.halfHeightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.StaticBody
            active = true
        }.let(world::createBody)

        val groundShape = PolygonShape().apply {
            setAsBox(groundTexture.halfWidthInMeters, groundTexture.halfHeightInMeters)
        }

        body.createFixture(groundShape, 1f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(groundTexture).apply {
                setSize(groundTexture.widthInMeters, groundTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)

        return entity
    }

    private fun keepOutSignEntity(): Entity {
        val keepOutSignTexture = textureAtlas.region(TowersTexture.KEEP_OUT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(UiConstants.WIDTH_F - keepOutSignTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.StaticBody
            active = false
        }.let(world::createBody)


        val polygonShape = PolygonShape().apply {
            setAsBox(keepOutSignTexture.halfWidthInMeters, keepOutSignTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(keepOutSignTexture).apply {
                setSize(keepOutSignTexture.widthInMeters, keepOutSignTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)

        return entity
    }

    private fun alertEntity(): Entity {
        val alertTexture = textureAtlas.region(TowersTexture.ALERT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(alertTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.StaticBody
            active = false
        }.let(world::createBody)


        val polygonShape = PolygonShape().apply {
            setAsBox(alertTexture.halfWidthInMeters, alertTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.MIDDLE
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(alertTexture).apply {
                setSize(alertTexture.widthInMeters, alertTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)

        return entity
    }

    override fun render(delta: Float) {
        super.render(delta)
        val newDelta = if (delta > 0.1f) 0.1f else delta
        camera.update()

        batch.projectionMatrix = camera.combined
        engine.update(newDelta)

        world.step(newDelta, 8, 8)
        b2dDebugRenderer.render(world, camera.combined)
    }

    override fun pause() {
        super.pause()
        engine.systems.forEach { it.setProcessing(false) }
    }

    override fun resume() {
        super.resume()
        engine.systems.forEach { it.setProcessing(true) }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun dispose() {
        super.dispose()
        world.dispose()
        b2dDebugRenderer.dispose()
    }
}