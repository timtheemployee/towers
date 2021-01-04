package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScalingViewport

abstract class BaseScreen() : Screen {

    protected val camera = OrthographicCamera(
        UiConstants.WIDTH_PPM_F, UiConstants.HEIGHT_PPM_F
    )

    private val viewport = ScalingViewport(
            Scaling.stretch,
            UiConstants.WIDTH_PPM_F, UiConstants.HEIGHT_PPM_F,
            camera
    )

    protected val stage = Stage(viewport)
    protected val renderer = ShapeRenderer()
    protected val batch = SpriteBatch()

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        stage.act(delta)
        stage.draw()

        camera.update()

        renderer.projectionMatrix = camera.combined
        batch.projectionMatrix = camera.combined
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun pause() = Unit

    override fun resume() = Unit

    override fun hide() = Unit

    override fun dispose() = Unit
}