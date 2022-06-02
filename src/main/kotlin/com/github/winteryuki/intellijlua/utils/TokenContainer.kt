package com.github.winteryuki.intellijlua.utils

import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.containers.map2Array
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty

interface TokenSort

interface TokenContainer<T> {
    fun tokenSetOf(sort: TokenSort): TokenSet
}

abstract class AbstractTokenContainer<I : IElementType>(
    private val tokenFactory: (String) -> I
) : TokenContainer<I> {
    private val tokenSorts = mutableMapOf<I, Set<TokenSort>>()

    // Delegate providers are lazy so manual initialization is needed anyway (or use reflection)
    protected abstract val tokens: List<I>

    override fun tokenSetOf(sort: TokenSort): TokenSet {
        val filtered = tokens.filter { sort in tokenSorts[it].orEmpty() }
        return TokenSet.create(*filtered.map2Array<I, IElementType> { it })
    }

    protected fun token(vararg sorts: TokenSort) = PropertyDelegateProvider { _: Any?, property ->
        val item by lazy { tokenFactory(property.name).also { tokenSorts[it] = sorts.toSet() } }
        ReadOnlyProperty<Any?, I> { _, _ -> item }
    }
}
