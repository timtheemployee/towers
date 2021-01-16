package com.wxxtfxrmx.towers.level.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.wxxtfxrmx.towers.common.UiConstants

class LevelScreen(
        textureAtlas: TextureAtlas,
) : ScreenAdapter() {

    private companion object {
        const val WORLD_DELTA = 1 / 30f
        const val POSITION_ITERATIONS = 3
        const val VELOCITY_ITERATIONS = 8
    }

    private val camera = OrthographicCamera(UiConstants.WIDTH_F, UiConstants.HEIGHT_F).apply {
        position.set(UiConstants.WIDTH_F * 0.5f, UiConstants.HEIGHT_F * 0.5f, 0f)
    }

    private val viewport = StretchViewport(UiConstants.WIDTH_F, UiConstants.HEIGHT_F, camera)

    private val b2dDebugRenderer = Box2DDebugRenderer(true, true, true, true, true, true)
    private val world = World(Vector2(0f, -9.8f), true)

    private val batch = SpriteBatch()
    private val controller = LevelController(world, textureAtlas, viewport)

    override fun render(delta: Float) {
        super.render(delta)
        val newDelta = if (delta > 0.1f) 0.1f else delta
        camera.update()

        batch.projectionMatrix = camera.combined

        world.step(WORLD_DELTA, VELOCITY_ITERATIONS, POSITION_ITERATIONS)
        //b2dDebugRenderer.render(world, camera.combined)

        controller.onPreRender(newDelta)
        batch.begin()
        controller.renderQueue.forEach {
            it.sprite.draw(batch)
        }
        batch.end()
        controller.onPostRender(newDelta)

        if (Gdx.input.isTouched) {
            controller.onTouched()
        }
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