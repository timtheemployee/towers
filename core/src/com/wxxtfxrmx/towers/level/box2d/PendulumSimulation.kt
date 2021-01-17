package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.halfHeightInMeters
import com.wxxtfxrmx.towers.common.heightInMeters
import com.wxxtfxrmx.towers.common.topBoundY
import com.wxxtfxrmx.towers.level.model.Model

class PendulumSimulation(
        private val viewport: Viewport,
        private val craneModel: Model,
) {

    private val leftPosition = Vector2(1f, viewport.camera.topBoundY())
    private val rightPosition = Vector2(viewport.worldWidth - 1f, viewport.camera.topBoundY())

    private var horizontalAlpha = 0f

    private var simulateToLeftSide = true
    var model: Model? = null
        set(value) {
            field = value
            value?.body?.position?.set(craneModel.body.position.x, craneModel.body.position.y - craneModel.sprite.heightInMeters)
        }

    fun update(delta: Float) {
        leftPosition.set(1f, viewport.camera.topBoundY())
        rightPosition.set(viewport.worldWidth - 1f, viewport.camera.topBoundY())

        horizontalAlpha += delta * 0.5f

        val destination = if (simulateToLeftSide) leftPosition else rightPosition

        val cranePosition = craneModel.body.position
        val movedPosition = cranePosition.cpy().lerp(destination, horizontalAlpha)
        craneModel.body.setTransform(movedPosition, craneModel.body.angle)

        movedPosition.set(movedPosition.x, movedPosition.y - craneModel.sprite.halfHeightInMeters)
        model?.body?.setTransform(movedPosition, model?.body?.angle ?: 0f)

        val isLeftReached = craneModel.body.position.epsilonEquals(leftPosition)
        val isRightReached = craneModel.body.position.epsilonEquals(rightPosition)
        if (horizontalAlpha >= 1 || isLeftReached || isRightReached) {
            simulateToLeftSide = !simulateToLeftSide
            horizontalAlpha = 0f
        }
    }
}