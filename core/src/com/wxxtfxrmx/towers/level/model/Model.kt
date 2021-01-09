package com.wxxtfxrmx.towers.level.model

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.Body

sealed class Model(val body: Body, val sprite: Sprite, val order: SortingLayer)

class Environment(body: Body, sprite: Sprite): Model(body, sprite, SortingLayer.MIDDLE)

class Block(body: Body, sprite: Sprite): Model(body, sprite, SortingLayer.FRONT)