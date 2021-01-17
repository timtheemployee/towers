package com.wxxtfxrmx.towers.level.ui.camera

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.level.model.Model

class CameraUpdater(
        private val viewport: Viewport,
        private val craneModel: Model,
) {

    private var alpha = 0f
    private val targetPosition = Vector3(viewport.camera.position)


    fun update(delta: Float) {
        val camera = viewport.camera
        val cameraTopBound = camera.position.y + camera.viewportHeight * 0.5f

        if (alpha > 1f) {
            alpha = 0f
        } else {
            camera.position.lerp(targetPosition, alpha)
            craneModel.body.setTransform(craneModel.body.position.x, cameraTopBound, craneModel.body.angle)
            alpha += delta
        }

        if (alpha == 0f && cameraTopBound != viewport.worldHeight) {
            targetPosition.set(camera.position)
            targetPosition.y += viewport.worldHeight - cameraTopBound
        }
    }
}