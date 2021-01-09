package com.wxxtfxrmx.towers.level.box2d

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.physics.box2d.*

class FloorContactListener : ContactListener {

    private var listener: ((Body, Body) -> Unit)? = null

    fun setOnContactBeginListener(listener: (Body, Body) -> Unit) {
        this.listener = listener
    }

    override fun beginContact(contact: Contact) {
        Gdx.app.log(this::class.simpleName, "Contact begin")
        listener?.invoke(contact.fixtureA.body, contact.fixtureB.body)
    }

    override fun endContact(contact: Contact) = Unit

    override fun preSolve(contact: Contact, oldManifold: Manifold) = Unit

    override fun postSolve(contact: Contact, impulse: ContactImpulse) = Unit
}