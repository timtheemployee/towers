package com.wxxtfxrmx.towers.common

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import java.lang.IllegalStateException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private var FloatArray.top
    get() = this[0]
    set(value) {
        this[0] = value
    }

private var FloatArray.right
    get() = this[1]
    set(value) {
        this[1] = value
    }

private var FloatArray.bottom
    get() = this[2]
    set(value) {
        this[2] = value
    }

private var FloatArray.left
    get() = this[3]
    set(value) {
        this[3] = value
    }

class Body2DBoundsCalculator {

    fun getBounds(body: Body): Pair<Float, Float> {
        val maxBounds = FloatArray(4)

        body.fixtureList.forEach { fixture ->
            val bounds = getBounds(fixture)

            if (bounds.left > maxBounds.left)
                maxBounds.left = bounds.left

            if (bounds.right > maxBounds.right)
                maxBounds.right = bounds.right

            if (bounds.top > maxBounds.top)
                maxBounds.top = bounds.top

            if (bounds.bottom > maxBounds.bottom)
                maxBounds.bottom = bounds.bottom
        }

        return abs(maxBounds.right - maxBounds.left) to abs(maxBounds.top - maxBounds.bottom)
    }

    private fun getBounds(fixture: Fixture): FloatArray {
        return when (val shape = fixture.shape) {
            is PolygonShape -> shape.edgeBounds()
            is ChainShape -> shape.chainBounds()
            is EdgeShape -> shape.edgeBounds()
            is CircleShape -> shape.circleBounds()
            else -> throw IllegalStateException("Unsupported shape type $shape (${shape.type})")
        }
    }

    private fun PolygonShape.edgeBounds(): FloatArray {
        val bounds = FloatArray(4)
        val tmp = Vector2()

        for (i in 0..vertexCount) {
            getVertex(i, tmp)
            updateBounds(bounds, tmp)
        }

        return bounds
    }

    //same as PolygonShape.getBounds LOL
    private fun ChainShape.chainBounds(): FloatArray {
        val bounds = FloatArray(4)
        val tmp = Vector2()

        for (i in 0..vertexCount) {
            getVertex(i, tmp)
            updateBounds(bounds, tmp)
        }

        return bounds
    }

    private fun EdgeShape.edgeBounds(): FloatArray {
        val bounds = FloatArray(4)
        val tmp = Vector2()
        getVertex1(tmp)
        updateBounds(bounds, tmp)

        getVertex2(tmp)
        updateBounds(bounds, tmp)

        return bounds
    }

    private fun CircleShape.circleBounds(): FloatArray {
        val bounds = FloatArray(4)
        bounds.top = position.y + radius
        bounds.right = position.x + radius
        bounds.bottom = position.y - radius
        bounds.left = position.x - radius

        return bounds
    }

    private fun updateBounds(bounds: FloatArray, vertex: Vector2) {
        bounds.right = max(vertex.x, bounds.right)
        bounds.left = min(vertex.x, bounds.left)
        bounds.top = max(vertex.y, bounds.top)
        bounds.bottom = min(vertex.y, bounds.bottom)
    }
}
