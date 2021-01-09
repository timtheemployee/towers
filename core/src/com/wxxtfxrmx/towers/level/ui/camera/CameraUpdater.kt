package com.wxxtfxrmx.towers.level.ui.camera

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport

class CameraUpdater(
        private val viewport: Viewport
) {

    private var alpha = 0f
    private val targetPosition = Vector3(viewport.camera.position)

    fun update(delta: Float) {
        val camera = viewport.camera

        if (alpha > 1f) {
            alpha = 0f
        } else {
            camera.position.lerp(targetPosition, alpha)
            alpha += delta
        }

        val cameraTopBound = camera.position.y + camera.viewportHeight * 0.5f
        if (alpha == 0f && cameraTopBound != viewport.worldHeight) {
            targetPosition.set(camera.position)
            targetPosition.y += viewport.worldHeight - cameraTopBound
        }
    }
}