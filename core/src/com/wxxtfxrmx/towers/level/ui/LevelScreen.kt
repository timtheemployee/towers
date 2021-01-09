package com.wxxtfxrmx.towers.level.ui

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
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.model.Block
import com.wxxtfxrmx.towers.level.model.Environment
import com.wxxtfxrmx.towers.level.model.Model
import com.wxxtfxrmx.towers.level.model.TowersTexture

class LevelScreen(
        private val textureAtlas: TextureAtlas,
) : ScreenAdapter() {

    private val camera = OrthographicCamera(UiConstants.WIDTH_F, UiConstants.HEIGHT_F).apply {
        position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F * 0.5f, 0f)
    }

    private val viewport = StretchViewport(UiConstants.WIDTH_F, UiConstants.HEIGHT_F, camera)

    private val b2dDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
    private val world = World(Vector2(0f, -9.8f), true)
    private val bodyBuilder = BodyBuilder(world)

    private val batch = SpriteBatch()
    private val renderQueue = Array<Model>()

    init {
        renderQueue.add(foundationEntity())
        renderQueue.add(groundEntities())
        renderQueue.add(fenceEntities())
        renderQueue.add(keepOutSignEntity())
        renderQueue.add(alertEntity())

        //world.setContactListener(FloorContactListener(engine))
    }

    private fun foundationEntity(): Model {
        val foundationTexture = textureAtlas.region(TowersTexture.FOUNDATION)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F - foundationTexture.heightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.DynamicBody
                }
                .fixture {
                    density = 1.0f
                    shape = PolygonShape().apply {
                        setAsBox(foundationTexture.halfWidthInMeters, foundationTexture.halfHeightInMeters)
                    }
                }
                .build()

        val sprite = Sprite(foundationTexture).apply {
            setSize(widthInMeters, heightInMeters)
            setPosition(body.position.x - halfWidthInMeters, body.position.y - halfWidthInMeters)
        }

        return Block(body, sprite)
    }

    private fun fenceEntities(): Model {
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(fenceTexture.halfWidthInMeters, fenceTexture.halfHeightInMeters + groundTexture.heightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.StaticBody
                    active = false
                }
                .fixture {
                    density = 0f
                    shape = PolygonShape().apply {
                        setAsBox(fenceTexture.halfWidthInMeters, fenceTexture.halfHeightInMeters)
                    }
                }
                .build()

        val sprite = Sprite(fenceTexture).apply {
            setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            setSize(widthInMeters, heightInMeters)
        }

        return Environment(body, sprite)
    }

    private fun groundEntities(): Model {
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(groundTexture.halfWidthInMeters, groundTexture.halfHeightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.StaticBody
                    active = true
                }
                .fixture {
                    shape = PolygonShape().apply {
                        setAsBox(groundTexture.halfWidthInMeters, groundTexture.halfHeightInMeters)
                    }
                }
                .build()

        val sprite = Sprite(groundTexture).apply {
            setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            setSize(widthInMeters, heightInMeters)
        }

        return Environment(body, sprite)
    }

    private fun keepOutSignEntity(): Model {
        val keepOutSignTexture = textureAtlas.region(TowersTexture.KEEP_OUT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(UiConstants.WIDTH_F - keepOutSignTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.StaticBody
                    active = false
                }
                .fixture {
                    density = 0f
                    shape = PolygonShape().apply {
                        setAsBox(keepOutSignTexture.halfWidthInMeters, keepOutSignTexture.halfHeightInMeters)
                    }
                }
                .build()

        val sprite = Sprite(keepOutSignTexture).apply {
            setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            setSize(widthInMeters, heightInMeters)
        }

        return Environment(body, sprite)
    }

    private fun alertEntity(): Model {
        val alertTexture = textureAtlas.region(TowersTexture.ALERT_SIGN)
        val fenceTexture = textureAtlas.region(TowersTexture.FENCE)
        val groundTexture = textureAtlas.region(TowersTexture.GROUND)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(alertTexture.widthInMeters, groundTexture.heightInMeters + fenceTexture.halfHeightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.StaticBody
                    active = false
                }
                .fixture {
                    density = 0f
                    shape = PolygonShape().apply {
                        setAsBox(alertTexture.widthInMeters, alertTexture.heightInMeters)
                    }
                }
                .build()

        val sprite = Sprite(alertTexture).apply {
            setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            setSize(widthInMeters, heightInMeters)
        }

        return Environment(body, sprite)
    }

    override fun render(delta: Float) {
        super.render(delta)
        val newDelta = if (delta > 0.1f) 0.1f else delta
        camera.update()

        batch.projectionMatrix = camera.combined

        world.step(newDelta, 8, 8)
        b2dDebugRenderer.render(world, camera.combined)

        renderQueue.forEach {
            val position = it.body.position
            val sprite = it.sprite

            sprite.setPosition(position.x - sprite.halfWidthInMeters, position.y - sprite.halfHeightInMeters)
        }

        renderQueue.sort { left, right -> right.order.layer - left.order.layer}

        batch.begin()
        renderQueue.forEach {
            it.sprite.draw(batch)
        }
        batch.end()
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