package com.wxxtfxrmx.towers.common.shader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram

fun shader(vertexSource: String, fragmentSource: String) : ShaderProgram {
    val vertexShader = Gdx.files.internal(vertexSource).readString()
    val fragmentShader = Gdx.files.internal(fragmentSource).readString()

    return ShaderProgram(vertexShader, fragmentShader)
}