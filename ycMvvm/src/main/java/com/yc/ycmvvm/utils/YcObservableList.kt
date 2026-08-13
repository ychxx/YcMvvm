package com.yc.ycmvvm.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.function.IntFunction

class YcObservableList<T>(
    private val delegate: MutableList<T> = mutableListOf()
) : MutableList<T> by delegate {

    var onListChanged: (() -> Unit)? = null

    override fun add(element: T): Boolean {
        val result = delegate.add(element)
        onListChanged?.invoke()
        return result
    }

    override fun add(index: Int, element: T) {
        delegate.add(index, element)
        onListChanged?.invoke()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = delegate.addAll(elements)
        onListChanged?.invoke()
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = delegate.addAll(index, elements)
        onListChanged?.invoke()
        return result
    }

    override fun remove(element: T): Boolean {
        val result = delegate.remove(element)
        onListChanged?.invoke()
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val result = delegate.removeAll(elements)
        onListChanged?.invoke()
        return result
    }

    override fun removeAt(index: Int): T {
        val result = delegate.removeAt(index)
        onListChanged?.invoke()
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val result = delegate.retainAll(elements)
        onListChanged?.invoke()
        return result
    }

    override fun clear() {
        delegate.clear()
        onListChanged?.invoke()
    }

    override fun set(index: Int, element: T): T {
        val result = delegate.set(index, element)
        onListChanged?.invoke()
        return result
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun <T> toArray(generator: IntFunction<Array<T>>): Array<T> {
        // delegate 在 JVM 上总是实现了 java.util.Collection，所以转换为 Collection<*> 是安全的
        return (delegate as java.util.Collection<*>).toArray(generator)
    }
}