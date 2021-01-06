package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.physics.box2d.*
import kotlin.reflect.KClass

class BodyBuilder(private val world: World) {

    private lateinit var bodyDef: BodyDef
    private lateinit var body: Body

    fun begin(): BodyBuilder {
        bodyDef = BodyDef()
        return this
    }

    fun define(block: BodyDef.() -> Unit): BodyBuilder {
        block(bodyDef)
        return this
    }

    fun after(): BodyBuilder {
        body = world.createBody(bodyDef)
        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Shape> fixture(shapeClass: KClass<T>, density: Float, block: T.() -> Unit): BodyBuilder {
        val shape = when (shapeClass) {
            PolygonShape::class -> PolygonShape()
            CircleShape::class -> CircleShape()
            EdgeShape::class -> EdgeShape()
            ChainShape::class -> ChainShape()
            else -> throw IllegalArgumentException("Not supported fixture with shape $shapeClass")
        }

        block(shape as T)

        body.createFixture(shape, density)
        return this
    }

    fun mass(block: MassData.() -> Unit): BodyBuilder {
        val massData = MassData()
        block(massData)
        body.massData = massData
        return this
    }

    fun build(): Body =
            body
}