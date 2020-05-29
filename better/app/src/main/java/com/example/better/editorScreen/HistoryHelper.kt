package com.example.better.editorScreen

import android.graphics.Bitmap

class HistoryHelper<T>(initState: T) {
    private val states: MutableList<T> = mutableListOf()
    private var index = 0

    init {
        states.add(initState)
    }

    fun add(bitmap: T) {
        if (index == states.size-1) {
            states.add(bitmap)
        } else {
            states.dropLast(states.size-1-index)
            states.add(bitmap)
        }
        index++
    }

    fun undo(): T? {
        if (index > 0) {
            index--
        }
        return states[index]
    }

    fun redo(): T? {
        if (index < states.size-1) {
            index++
        }
        return states[index]
    }

    fun current(): T {
        return states[index]
    }

    fun getList(): MutableList<T> {
        return states
    }
}