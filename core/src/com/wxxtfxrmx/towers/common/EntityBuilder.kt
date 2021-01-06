package com.wxxtfxrmx.towers.common

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import kotlin.reflect.KClass

class EntityBuilder constructor(private val engine: PooledEngine) {

    private lateinit var entity: Entity

    fun begin(): EntityBuilder {
        entity = engine.createEntity()
        return this
    }

    fun <T: Component> component(componentClass: KClass<T>, block: T.() -> Unit): EntityBuilder {
        val component = engine.createComponent(componentClass.java)
        block(component)
        entity.add(component)

        return this
    }

    fun build(): Entity =
            entity

    fun build(block: Entity.() -> Unit): Entity {
        block(entity)
        return entity
    }
}