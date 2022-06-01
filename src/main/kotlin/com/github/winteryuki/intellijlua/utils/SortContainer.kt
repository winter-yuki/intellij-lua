package com.github.winteryuki.intellijlua.utils

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.containers.map2Array
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface Sort

interface SortContainer<T> {
    fun itemSetOf(sort: Sort): TokenSet
}

abstract class AbstractSortContainer<I : IElementType>(
    private val itemFactory: (String) -> I
) : SortContainer<I> {
    private val itemSorts = mutableMapOf<I, Set<Sort>>()

    // Delegate providers are lazy so manual initialization is needed anyway
    protected abstract val items: List<I>

    override fun itemSetOf(sort: Sort): TokenSet {
        val filtered = items.filter { sort in itemSorts[it].orEmpty() }
        return TokenSet.create(*filtered.map2Array<I, IElementType> { it })
    }

    protected fun item(vararg sorts: Sort) = PropertyDelegateProvider { _: Any?, property ->
        val item by lazy { itemFactory(property.name).also { itemSorts[it] = sorts.toSet() } }
        ReadOnlyProperty<Any?, I> { _, _ -> item }
    }
}
