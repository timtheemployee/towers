package com.wxxtfxrmx.towers.level.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.model.UniformDt

class AccumulateElapsedTimeSystem : IteratingSystem(
        Family.one(ShaderComponent::class.java).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val shaderComponent: ShaderComponent = entity.component()
        shaderComponent.uniforms?.forEach { uniform ->
            if (uniform is UniformDt) {
                uniform.elapsed += deltaTime
            }
        }
    }
}