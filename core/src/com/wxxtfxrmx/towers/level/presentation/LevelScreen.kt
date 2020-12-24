package com.wxxtfxrmx.towers.level.presentation

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.wxxtfxrmx.towers.common.BaseScreen
import com.wxxtfxrmx.towers.common.UiConstants
import com.wxxtfxrmx.towers.common.addComponents
import com.wxxtfxrmx.towers.common.component
import com.wxxtfxrmx.towers.common.shader.ShapeRendererFactory
import com.wxxtfxrmx.towers.level.component.BoundsComponent
import com.wxxtfxrmx.towers.level.component.OrderComponent
import com.wxxtfxrmx.towers.level.component.ShaderComponent
import com.wxxtfxrmx.towers.level.system.RenderingSystem

class LevelScreen(
        shapeRendererFactory: ShapeRendererFactory,
) : BaseScreen() {

    private val engine = PooledEngine()
    private val inputSystems = mutableListOf<EntitySystem>()
    private val logicSystems = mutableListOf<EntitySystem>()
    private val renderingSystems = mutableListOf<EntitySystem>()

    init {
        renderingSystems.add(
                RenderingSystem(batch, shapeRendererFactory)
        )

        inputSystems.forEach(engine::addSystem)
        logicSystems.forEach(engine::addSystem)
        renderingSystems.forEach(engine::addSystem)

        //FIXME: Shaders stuff
        val entity = engine.createEntity()

        val shaderComponent: ShaderComponent = engine.component()

        val boundsComponent: BoundsComponent = engine.component()
        boundsComponent.bounds.set(0f, 0f, UiConstants.WIDTH, UiConstants.HEIGHT)

        val orderComponent: OrderComponent = engine.component()
        orderComponent.order = Int.MAX_VALUE

        val vertexShader = Gdx.files.internal("shaders/background/vertex.glsl").readString()
        val fragmentShader = Gdx.files.internal("shaders/background/fragment.glsl").readString()
        val shader = ShaderProgram(vertexShader, fragmentShader)
        shaderComponent.shader = shader

        entity.addComponents(shaderComponent, boundsComponent)

        engine.addEntity(entity)
    }

    override fun render(delta: Float) {
        super.render(delta)
        engine.update(delta)
    }

    override fun pause() {
        super.pause()
        engine.systems.forEach { it.setProcessing(false) }
    }

    override fun resume() {
        super.resume()
        engine.systems.forEach { it.setProcessing(true) }
    }
}