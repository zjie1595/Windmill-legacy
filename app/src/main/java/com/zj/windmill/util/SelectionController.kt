package com.zj.windmill.util

import com.zj.windmill.model.Episode

class SelectionController {

    val models = mutableListOf<Episode>()

    var selectedModel: Episode? = null

    fun addModels(models: List<Episode>) {
        this.models.addAll(models)
    }

    fun select(model: Episode) {
        selectedModel?.selected = false
        selectedModel?.notifyChange()
        model.selected = true
        model.notifyChange()
        selectedModel = model
    }
}