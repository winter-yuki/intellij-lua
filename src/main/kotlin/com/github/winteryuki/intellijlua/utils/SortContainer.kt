package com.github.winteryuki.intellijlua.utils

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface Sort

interface SortContainer<T> {
    fun tokenSetOf(sort: Sort): TokenSet
}

abstract class AbstractSortContainer<T : IElementType>(
    private val tokenFactory: (String) -> T,
    private val tokenArrayFactory: (List<T>) -> Array<T>
) : SortContainer<T> {
    private val tokenSorts = mutableMapOf<T, Set<Sort>>()

    // Delegate providers are lazy so manual initialization is needed anyway
    protected abstract val tokens: List<T>

    override fun tokenSetOf(sort: Sort): TokenSet {
        val filtered = tokens.filter { sort in tokenSorts[it].orEmpty() }
        return TokenSet.create(*tokenArrayFactory(filtered))
    }

    protected fun token(vararg sorts: Sort) = PropertyDelegateProvider { _: Any?, property ->
        val token by lazy { tokenFactory(property.name).also { tokenSorts[it] = sorts.toSet() } }
        ReadOnlyProperty<Any?, T> { _, _ -> token }
    }
}
