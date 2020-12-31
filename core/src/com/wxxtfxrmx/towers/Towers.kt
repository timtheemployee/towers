package com.wxxtfxrmx.towers

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.wxxtfxrmx.towers.common.UiColors
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.ui.LevelScreen

class Towers : Game() {

    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var shapeFactory: ShapeRendererFactory
    private lateinit var textureAtlas: TextureAtlas

    override fun create() {
        shapeRenderer = ShapeRenderer()
        shapeFactory = ShapeRendererFactory(shapeRenderer)
        textureAtlas = TextureAtlas(Gdx.files.internal("atlas/towers.atlas"))
        screen = LevelScreen(textureAtlas, shapeFactory)
    }

    override fun render() {
        Gdx.gl.glClearColor(UiColors.BLACK.r, UiColors.BLACK.g, UiColors.BLACK.b, UiColors.BLACK.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        getScreen().render(Gdx.graphics.deltaTime)
    }

    override fun dispose() {

    }
}