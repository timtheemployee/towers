package com.wxxtfxrmx.towers

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.presentation.LevelScreen

class Towers : Game() {

    private val shapeFactory = ShapeRendererFactory()
    private lateinit var textureAtlas: TextureAtlas

    override fun create() {
        textureAtlas = TextureAtlas(Gdx.files.internal("atlas/towers.atlas"))
        screen = LevelScreen(textureAtlas, shapeFactory)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        getScreen().render(Gdx.graphics.deltaTime)
    }

    override fun dispose() {

    }
}