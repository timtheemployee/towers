package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.heightInMeters
import com.wxxtfxrmx.towers.common.widthInMeters
import com.wxxtfxrmx.towers.level.model.Model

class PendulumSimulation(
        private val viewport: Viewport,
) {

    private val leftPosition = Vector2(0f, viewport.worldHeight)
    private val rightPosition = Vector2(viewport.worldWidth, viewport.worldHeight)

    private var initialPosition: Vector2? = null
    private var alpha = 0f
    private var simulateToLeftSide = true

    var model: Model? = null
        set(value) {
            val width = value?.sprite?.widthInMeters ?: 0f
            val height = value?.sprite?.heightInMeters ?: 0f
            leftPosition.set(width, viewport.worldHeight - height)
            rightPosition.set(viewport.worldWidth - width, viewport.worldHeight - height)

            field = value
            initialPosition = value?.body?.position
            alpha = 0f
        }

    fun update(delta: Float) {
        alpha += delta * 0.5f

        val position = model?.body?.position?.cpy()
        position?.lerp(leftPosition, alpha)
        model?.body?.setTransform(position, 0f)

        val destination = if (simulateToLeftSide) leftPosition else rightPosition

        val movedPosition = initialPosition?.cpy()?.lerp(destination, alpha)
        model?.body?.setTransform(movedPosition, 0f)

        val isLeftReached = model?.body?.position?.epsilonEquals(leftPosition) ?: return
        val isRightReached = model?.body?.position?.epsilonEquals(rightPosition) ?: return
        if (alpha >= 1 || isLeftReached || isRightReached) {
            simulateToLeftSide = !simulateToLeftSide
            alpha = 0f
        }
    }
}