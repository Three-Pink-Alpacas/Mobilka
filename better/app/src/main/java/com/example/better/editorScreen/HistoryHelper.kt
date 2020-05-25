package com.example.better.editorScreen

import android.graphics.Bitmap

class HistoryHelper(initState: Bitmap) {
    private val states: MutableList<Bitmap> = mutableListOf()
    private var index = 0

    init {
        states.add(initState)
    }

    fun add(bitmap: Bitmap) {
        if (index == states.size-1) {
            states.add(bitmap)
        } else {
            states.dropLast(states.size-1-index)
            states.add(bitmap)
        }
        index++
    }

    fun undo(): Bitmap? {
        if (index > 0) {
            index--
        }
        return states[index]
    }

    fun redo(): Bitmap? {
        if (index < states.size-1) {
            index++
        }
        return states[index]
    }

    fun current(): Bitmap {
        return states[index]
    }
}