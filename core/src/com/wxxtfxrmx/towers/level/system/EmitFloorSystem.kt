package com.wxxtfxrmx.towers.level.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.viewport.Viewport
import com.wxxtfxrmx.towers.common.*
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.EmitFloorComponent
import com.wxxtfxrmx.towers.level.component.SortingLayerComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent
import com.wxxtfxrmx.towers.level.model.Block
import com.wxxtfxrmx.towers.level.model.Model
import com.wxxtfxrmx.towers.level.model.SortingLayer
import com.wxxtfxrmx.towers.level.model.TowersTexture

class EmitFloorSystem(
        private val bodyBuilder: BodyBuilder,
        private val textureAtlas: TextureAtlas,
        private val viewport: Viewport
) : IteratingSystem(
        Family.all(EmitFloorComponent::class.java).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        entity.remove(EmitFloorComponent::class.java)
        engine.removeEntity(entity)

        val floorTexture = textureAtlas.region(TowersTexture.FLOOR_V1)

        val body = bodyBuilder
                .begin()
                .body {
                    position.set(viewport.worldWidth * 0.5f, viewport.worldHeight - floorTexture.heightInMeters)
                    fixedRotation = true
                    type = BodyDef.BodyType.DynamicBody
                }
                .fixture {
                    density = 1f
                    shape = PolygonShape().apply {
                        setAsBox(floorTexture.halfWidthInMeters, floorTexture.halfHeightInMeters)
                    }
                }
                .build()

       val sprite = Sprite(floorTexture).apply {
           setPosition(body.position.x - halfWidthInMeters, body.position.y - halfHeightInMeters)
           setSize(widthInMeters, heightInMeters)
       }

        val model = Block(body, sprite)
    }
}