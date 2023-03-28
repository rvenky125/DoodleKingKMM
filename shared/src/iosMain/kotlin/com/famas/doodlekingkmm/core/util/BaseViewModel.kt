package com.famas.doodlekingkmm.core.util

import kotlinx.coroutines.CoroutineScope

actual open class BaseViewModel {
    override val scope: CoroutineScope
        get() {
            // these methods need to be defined
            return this.getScopeInstance() ?: this.createScopeInstance()
        }

    // marks view model as cleared & closes the coroutine scope.
    fun clear() {}
}