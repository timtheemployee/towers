package com.wxxtfxrmx.towers.level.system.camera

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport

class UpdateCameraPositionSystem(
        private val camera: OrthographicCamera,
        private val viewport: Viewport,
) : EntitySystem() {

    private companion object {
        const val VERTICAL_OFFSET = 8f
    }

    private var alpha = 0f
    private val targetPosition = Vector3(camera.position)

    override fun update(deltaTime: Float) {
        val cameraTopPosition = camera.position.y + camera.viewportHeight

        if (alpha > 1f) {
            alpha = 0f
        } else {
            camera.position.lerp(targetPosition, alpha)
            alpha += deltaTime
        }

        if (alpha == 0f && cameraTopPosition < viewport.worldHeight) {
            targetPosition.set(camera.position)
            targetPosition.y += viewport.worldHeight - cameraTopPosition + VERTICAL_OFFSET
        }

//        if (cameraTopPosition < viewport.worldHeight) {
//            alpha += deltaTime * 1
//            val targetPosition = camera.position.cpy()
//            targetPosition.set(targetPosition.x, targetPosition.y + camera.viewportHeight * 1.5f, 0f)
//            camera.position.lerp(targetPosition, alpha)
//            camera.update()
//
//            if (alpha > 1f) alpha = 0f
//        }
    }
}