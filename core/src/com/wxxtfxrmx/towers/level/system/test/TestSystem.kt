package com.wxxtfxrmx.towers.level.system.test

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent

class TestSystem : IteratingSystem(
        Family.all(BodyComponent::class.java, ShaderComponent::class.java).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyComponent: BodyComponent = entity.component()
        val body = bodyComponent.body
        requireNotNull(body)
    }
}