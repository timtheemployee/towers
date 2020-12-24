package com.wxxtfxrmx.towers

import com.badlogic.gdx.backends.android.AndroidApplication
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.wxxtfxrmx.towers.Towers

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        //NOTE: not stable
        config.useGL30 = true
        initialize(Towers(), config)
    }
}