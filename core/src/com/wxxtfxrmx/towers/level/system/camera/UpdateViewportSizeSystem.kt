package com.wxxtfxrmx.towers.level.system.camera

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.UpdateViewportSizeComponent

class UpdateViewportSizeSystem(
        private val engine: PooledEngine,
        private val viewport: Viewport
) : IteratingSystem(
        Family.all(UpdateViewportSizeComponent::class.java).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val updateViewportSizeComponent: UpdateViewportSizeComponent = entity.component()
        viewport.worldHeight += updateViewportSizeComponent.height * 2

        entity.remove(UpdateViewportSizeComponent::class.java)
        engine.removeEntity(entity)
    }
}