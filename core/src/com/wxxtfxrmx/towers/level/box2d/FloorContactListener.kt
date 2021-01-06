package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Manifold
import com.wxxtfxrmx.towers.common.Body2DBoundsCalculator
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.level.component.EmitFloorComponent
import com.wxxtfxrmx.towers.level.component.UpdateViewportSizeComponent

class FloorContactListener(
        private val engine: PooledEngine,
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

        val updateViewportSizeComponent: UpdateViewportSizeComponent = engine.component()
        updateViewportSizeComponent.height = height

        val updateViewportEntity = engine.createEntity()
        updateViewportEntity.add(updateViewportSizeComponent)
        engine.addEntity(updateViewportEntity)
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse) = Unit
}