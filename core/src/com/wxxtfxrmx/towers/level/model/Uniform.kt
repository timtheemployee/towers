package com.wxxtfxrmx.towers.level.model

sealed class Uniform(val name: String)

class Uniform1f(name: String, var v0: Float): Uniform(name)
class Uniform2f(name: String, var v0: Float, var v1: Float): Uniform(name)
class Uniform3f(name: String, var v0: Float, var v1: Float, var v2: Float): Uniform(name)
class UniformDt(name: String, var elapsed: Float = 0f): Uniform(name)

