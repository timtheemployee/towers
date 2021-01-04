package com.wxxtfxrmx.towers.level.ui

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent
import com.wxxtfxrmx.towers.level.model.TowersTexture
import com.wxxtfxrmx.towers.level.system.rendering.RenderingSystem

class LevelScreen(
        private val textureAtlas: TextureAtlas,
) : ScreenAdapter() {

    private val camera = OrthographicCamera(UiConstants.WIDTH_F, UiConstants.HEIGHT_F).apply {
        position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F * 0.5f, 0f)
    }

    private val viewport = StretchViewport(UiConstants.WIDTH_F, UiConstants.HEIGHT_F, camera)

    private val b2dDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
    private val world = World(Vector2(0f, -26.0f), true)
    private val engine = PooledEngine()

    private val batch = SpriteBatch()
    private val renderingSystem = RenderingSystem(batch)

    init {
        engine.addEntity(foundationEntity())
        engine.addEntities(groundEntities())
        engine.addEntities(fenceEntities())
        engine.addEntity(keepOutSignEntity())
        engine.addEntity(alertEntity())

        engine.addSystem(renderingSystem)
    }

    private fun foundationEntity(): Entity {
        val foundationTexture = textureAtlas.region(TowersTexture.FOUNDATION)

        val body = BodyDef().apply {
            position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F - foundationTexture.heightInMeters)
            fixedRotation = true
        }.let(world::createBody)


        val polygonShape = PolygonShape().apply {
            setAsBox(foundationTexture.halfWidthInMeters, foundationTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(foundationTexture).apply {
                setSize(foundationTexture.widthInMeters, foundationTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent)

        return entity
    }

    private fun fenceEntities(): List<Entity> {
        val entities = mutableListOf<Entity>()
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        var x = -fenceTexture.widthInMeters
        while (x < UiConstants.WIDTH_F) {
            x += fenceTexture.widthInMeters

            val body = BodyDef().apply {
                position.set(x, fenceTexture.halfHeightInMeters + groundTexture.heightInMeters)
                fixedRotation = true
            }.let(world::createBody)

            val groundShape = PolygonShape().apply {
                setAsBox(fenceTexture.halfWidthInMeters, fenceTexture.halfHeightInMeters)
            }

            body.createFixture(groundShape, 0f)

            val bodyComponent: BodyComponent = engine.component {
                this.body = body
            }

            val spriteComponent: SpriteComponent = engine.component {
                this.sprite = Sprite(fenceTexture).apply {
                    setSize(fenceTexture.widthInMeters, fenceTexture.heightInMeters)
                    setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
                }
            }

            val entity = engine.createEntity()
            entity.addComponents(bodyComponent, spriteComponent)
            entities.add(entity)
        }

        return entities
    }

    private fun groundEntities(): List<Entity> {
        val entities = mutableListOf<Entity>()
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        var x = -groundTexture.widthInMeters
        while (x < UiConstants.WIDTH_F) {
            x += groundTexture.widthInMeters

            val body = BodyDef().apply {
                position.set(x, groundTexture.halfHeightInMeters)
                fixedRotation = true
            }.let(world::createBody)

            val groundShape = PolygonShape().apply {
                setAsBox(groundTexture.halfWidthInMeters, groundTexture.halfHeightInMeters)
            }

            body.createFixture(groundShape, 0f)

            val bodyComponent: BodyComponent = engine.component {
                this.body = body
            }

            val spriteComponent: SpriteComponent = engine.component {
                this.sprite = Sprite(groundTexture).apply {
                    setSize(groundTexture.widthInMeters, groundTexture.heightInMeters)
                    setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
                }
            }

            val entity = engine.createEntity()
            entity.addComponents(bodyComponent, spriteComponent)
            entities.add(entity)
        }

        return entities
    }

    private fun keepOutSignEntity(): Entity {
        val keepOutSignTexture = textureAtlas.region(TowersTexture.KEEP_OUT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(UiConstants.WIDTH_F - keepOutSignTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
            fixedRotation = true
        }.let(world::createBody)


        val polygonShape = PolygonShape().apply {
            setAsBox(keepOutSignTexture.halfWidthInMeters, keepOutSignTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(keepOutSignTexture).apply {
                setSize(keepOutSignTexture.widthInMeters, keepOutSignTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent)

        return entity
    }

    private fun alertEntity(): Entity {
        val alertTexture = textureAtlas.region(TowersTexture.ALERT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = BodyDef().apply {
            position.set(alertTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
            fixedRotation = true
        }.let(world::createBody)


        val polygonShape = PolygonShape().apply {
            setAsBox(alertTexture.halfWidthInMeters, alertTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 0f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(alertTexture).apply {
                setSize(alertTexture.widthInMeters, alertTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val entity = engine.createEntity()
        entity.addComponents(bodyComponent, spriteComponent)

        return entity
    }

    override fun render(delta: Float) {
        super.render(delta)
        camera.update()

        batch.projectionMatrix = camera.combined
        engine.update(delta)

        world.step(delta, 4, 4)
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
}