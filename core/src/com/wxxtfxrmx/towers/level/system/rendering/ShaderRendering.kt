package com.wxxtfxrmx.towers.level.system.rendering

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.model.*

class ShaderRendering(private val rendererFactory: ShapeRendererFactory) {

    fun render(entity: Entity) {
        val shaderComponent: ShaderComponent = entity.component()
        val shader = shaderComponent.shader ?: return
        val renderer = rendererFactory.get(shader)
        val boundsComponent: BoundsComponent = entity.component()
        val bounds = boundsComponent.bounds

        shaderComponent.uniforms?.let { applyUniforms(shader, it) }

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height)
        renderer.end()
    }

    private fun applyUniforms(shader: ShaderProgram, uniforms: List<Uniform>) {
        shader.bind()
        uniforms.forEach { uniform ->
            when (uniform) {
                is Uniform1f -> shader.setUniformf(uniform.name, uniform.v0)
                is Uniform2f -> shader.setUniformf(uniform.name, uniform.v0, uniform.v1)
                is Uniform3f -> shader.setUniformf(uniform.name, uniform.v0, uniform.v1, uniform.v2)
                is UniformDt -> shader.setUniformf(uniform.name, uniform.elapsed)
            }
        }
    }
}