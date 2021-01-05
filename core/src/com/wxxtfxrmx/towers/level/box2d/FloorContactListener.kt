package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.Body2DBoundsCalculator
import com.wxxtfxrmx.towers.level.component.EmitFloorComponent
import com.wxxtfxrmx.towers.common.UiConstants
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.CameraDestinationComponent

class FloorContactListener(
        private val engine: PooledEngine,
        private val viewport: Viewport,
) : ContactListener {

    private val sizeCalculator = Body2DBoundsCalculator()

    override fun beginContact(contact: Contact) {
        val emitEntity = engine.createEntity()
        val emitFloorComponent: EmitFloorComponent = engine.component()

        emitEntity.add(emitFloorComponent)

        engine.addEntity(emitEntity)

        val bottomBody = contact.fixtureB.body
        bottomBody.isAwake = false

        val topBody = contact.fixtureA.body

        val (_, height) = sizeCalculator.getBounds(topBody)

        viewport.worldHeight += height * 1.5f + 1

        if (topBody.position.y >= UiConstants.HEIGHT_F * 0.2f) {
            val entity = engine.createEntity()
            val destinationComponent: CameraDestinationComponent = engine.component()
            destinationComponent.destination.set(viewport.worldWidth * 0.5f, viewport.worldHeight - UiConstants.HEIGHT_F * 0.8f, 0f)
            entity.add(destinationComponent)
            engine.addEntity(entity)
        }
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse) = Unit
}