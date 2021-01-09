package com.wxxtfxrmx.towers.level.ui

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.model.Block
import com.wxxtfxrmx.towers.level.model.Environment
import com.wxxtfxrmx.towers.level.model.Model
import com.wxxtfxrmx.towers.level.model.TowersTexture

class LevelController(
        world: World,
        private val textureAtlas: TextureAtlas,
        private val viewport: Viewport,
) {

    private val models = mutableListOf<Model>()
    val renderQueue = mutableListOf<Model>()
    private val bodyBuilder = BodyBuilder(world)

    init {
        models.add(foundationEntity())
        models.add(groundEntities())
        models.add(fenceEntities())
        models.add(keepOutSignEntity())
        models.add(alertEntity())
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

    fun onPreRender() {
        models.forEach(::updateModelPosition)

        models
                .filter(::isInCameraBounds)
                .sortedWith { left, right -> right.order.layer - left.order.layer }
                .let(renderQueue::addAll)
    }

    fun onPostRender() {
        renderQueue.clear()
    }

    private fun updateModelPosition(model: Model): Model {
        val position = model.body.position
        val sprite = model.sprite

        sprite.setPosition(position.x - sprite.halfWidthInMeters, position.y - sprite.halfHeightInMeters)
        return model
    }

    private fun isInCameraBounds(model: Model): Boolean {
        val camera = viewport.camera
        val bottomBound = camera.position.y - camera.viewportHeight * 0.5f
        val topBound = camera.position.y + camera.viewportHeight * 0.5f

        val sprite = model.sprite
        val spriteBottom = sprite.y
        val spriteTop = sprite.y + sprite.height

        return spriteBottom < topBound || spriteTop > bottomBound
    }
}