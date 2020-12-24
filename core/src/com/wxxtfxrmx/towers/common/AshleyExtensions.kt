package com.wxxtfxrmx.towers.common

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import kotlin.reflect.KClass

fun <T : Component> mapper(componentClass: KClass<T>) : ComponentMapper<T> =
        ComponentMapper.getFor(componentClass.java)

inline fun <reified T : Component> Entity.component() : T =
        getComponent(T::class.java)

fun Entity.addComponents(vararg components: Component) {
    components.forEach(::add)
}

inline fun <reified T: Component> PooledEngine.component() : T =
        createComponent(T::class.java)