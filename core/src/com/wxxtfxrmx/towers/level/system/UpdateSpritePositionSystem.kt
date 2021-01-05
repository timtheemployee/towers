package com.wxxtfxrmx.towers.level.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.halfHeightInMeters
import com.wxxtfxrmx.towers.common.halfWidthInMeters
import com.wxxtfxrmx.towers.level.component.BodyComponent
import com.wxxtfxrmx.towers.level.component.SpriteComponent

class UpdateSpritePositionSystem : IteratingSystem(
        Family.all(BodyComponent::class.java, SpriteComponent::class.java).get()
) {

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val bodyComponent: BodyComponent = entity.component()
        val spriteComponent: SpriteComponent = entity.component()

        val body = requireNotNull(bodyComponent.body)
        val sprite = requireNotNull(spriteComponent.sprite)

        sprite.setPosition(
                body.position.x - sprite.halfWidthInMeters,
                body.position.y - sprite.halfHeightInMeters)
    }
}