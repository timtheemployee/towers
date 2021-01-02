package com.wxxtfxrmx.towers.level.component

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.wxxtfxrmx.towers.level.model.Uniform

class ShaderComponent : Component {
    var shader: ShaderProgram? = null
    var uniforms: List<Uniform>? = null
}