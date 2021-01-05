package com.wxxtfxrmx.towers.level.system.camera

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.CameraDestinationComponent

class CameraTranslateSystem(
        private val camera: OrthographicCamera,
) : IteratingSystem(
    Family.all(CameraDestinationComponent::class.java).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val destinationComponent: CameraDestinationComponent = entity.component()

        val destination = destinationComponent.destination
        camera.position.set(destination)

        entity.remove(CameraDestinationComponent::class.java)
        engine.removeEntity(entity)
    }
}