package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.halfHeightInMeters
import com.wxxtfxrmx.towers.common.heightInMeters
import com.wxxtfxrmx.towers.common.topBoundY
import com.wxxtfxrmx.towers.level.model.Model
import kotlin.math.sin

class PendulumSimulation(
        private val viewport: Viewport,
        private val craneModel: Model,
) {

    private val leftPosition = Vector2(1f, viewport.camera.topBoundY())
    private val rightPosition = Vector2(viewport.worldWidth - 1f, viewport.camera.topBoundY())

    private val position = Vector2()
    private val velocity = Vector2()
    private val movement = Vector2()
    private val direction = Vector2()
    private val blockPosition = Vector2()
    
    private var simulateToLeftSide = true
    var model: Model? = null
        set(value) {
            field = value
            value?.body?.position?.set(craneModel.body.position.x, craneModel.body.position.y - craneModel.sprite.heightInMeters)
        }

    fun update(delta: Float) {

        leftPosition.set(1f, viewport.camera.topBoundY())
        rightPosition.set(viewport.worldWidth - 1f, viewport.camera.topBoundY())

        val destination = if (simulateToLeftSide) leftPosition else rightPosition

        position.set(craneModel.body.position)
        direction.set(destination).sub(position).nor()
        velocity.set(direction).scl(10f)
        movement.set(velocity).scl(delta)
        position.add(movement)

        craneModel.body.setTransform(position, craneModel.body.angle)

        blockPosition.set(position.x, position.y - craneModel.sprite.halfHeightInMeters)
        model?.body?.setTransform(blockPosition, model?.body?.angle ?: 0f)

        val isLeftReached = craneModel.body.position.x <= leftPosition.x
        val isRightReached = craneModel.body.position.x >= rightPosition.x
        if (isLeftReached || isRightReached) {
            simulateToLeftSide = !simulateToLeftSide
        }
    }
}