package com.wxxtfxrmx.towers.level.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.MassData
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.EmitFloorComponent
import com.wxxtfxrmx.towers.level.component.SortingLayerComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent
import com.wxxtfxrmx.towers.level.model.SortingLayer
import com.wxxtfxrmx.towers.level.model.TowersTexture

class EmitFloorSystem(
        private val engine: PooledEngine,
        private val world: World,
        private val textureAtlas: TextureAtlas,
        private val viewport: Viewport
) : IteratingSystem(
        Family.all(EmitFloorComponent::class.java).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.remove(EmitFloorComponent::class.java)
        engine.removeEntity(entity)

        val floorTexture = textureAtlas.region(TowersTexture.FLOOR_V1)

        val body = BodyDef().apply {
            position.set(viewport.worldWidth * 0.5f, viewport.worldHeight - floorTexture.heightInMeters)
            fixedRotation = true
            type = BodyDef.BodyType.DynamicBody
        }.let(world::createBody)

        val mass = MassData().apply {
            mass = 100f
            center.set(0f, body.position.y)
        }

        body.massData = mass

        val polygonShape = PolygonShape().apply {
            setAsBox(floorTexture.halfWidthInMeters, floorTexture.halfHeightInMeters)
        }

        body.createFixture(polygonShape, 1f)

        val bodyComponent: BodyComponent = engine.component {
            this.body = body
        }

        val sortingLayerComponent: SortingLayerComponent = engine.component {
            layer = SortingLayer.FRONT
        }

        val spriteComponent: SpriteComponent = engine.component {
            this.sprite = Sprite(floorTexture).apply {
                setSize(floorTexture.widthInMeters, floorTexture.heightInMeters)
                setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
            }
        }

        val floor = engine.createEntity()
        floor.addComponents(bodyComponent, spriteComponent, sortingLayerComponent)
        engine.addEntity(floor)
    }
}