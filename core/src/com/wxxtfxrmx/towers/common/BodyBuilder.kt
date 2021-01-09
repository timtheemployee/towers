package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.World

class BodyBuilder(private val world: World) {

    private lateinit var bodyDef: BodyDef
    private lateinit var fixtureDef: FixtureDef

    fun begin(): BodyBuilder {
        bodyDef = BodyDef()
        fixtureDef = FixtureDef()
        return this
    }

    fun body(block: BodyDef.() -> Unit): BodyBuilder {
        block(bodyDef)
        return this
    }

    fun fixture(block: FixtureDef.() -> Unit): BodyBuilder {
        block(fixtureDef)
        return this
    }

    fun build(): Body {
        val body = world.createBody(bodyDef)
        body.createFixture(fixtureDef)

        return body
    }
}