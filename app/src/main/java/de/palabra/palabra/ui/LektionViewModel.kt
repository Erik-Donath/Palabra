package de.palabra.palabra.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LektionViewModel : ViewModel() {
    private val _expandedStates = MutableLiveData<Map<Long, Boolean>>(emptyMap())
    val expandedStates: LiveData<Map<Long, Boolean>> = _expandedStates

    fun toggleLektionExpansion(lektionId: Long) {
        val current = _expandedStates.value?.toMutableMap() ?: mutableMapOf()
        current[lektionId] = !(current[lektionId] ?: false)
        _expandedStates.value = current
    }
}
