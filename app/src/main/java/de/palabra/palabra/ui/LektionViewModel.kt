package de.palabra.palabra.ui

import androidx.lifecycle.*
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.LektionWithVocabs
import de.palabra.palabra.db.Repository
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LektionViewModel(private val repository: Repository) : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    private val _expandedLektionIds = MutableLiveData<Set<Int>>(emptySet())

    // Bridge Flow to LiveData for proper observation
    val allLektionenWithVocabs: LiveData<List<LektionWithVocabs>> =
        repository.getAllLektionenWithVocabsFlow().asLiveData()

    val filteredLektionWithVocabs: LiveData<List<LektionWithVocabs>> =
        MediatorLiveData<List<LektionWithVocabs>>().apply {
            fun update() {
                val query = _searchQuery.value.orEmpty().lowercase()
                val data = allLektionenWithVocabs.value.orEmpty()
                value = if (query.isBlank()) data
                else data.filter { it.lektion.title.lowercase().contains(query) }
            }
            addSource(allLektionenWithVocabs) { update() }
            addSource(_searchQuery) { update() }
        }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleExpansion(lektionId: Int) {
        _expandedLektionIds.value = _expandedLektionIds.value?.let { set ->
            if (set.contains(lektionId)) set - lektionId else set + lektionId
        }
    }

    fun isExpanded(lektionId: Int): Boolean {
        return _expandedLektionIds.value?.contains(lektionId) == true
    }

    fun deleteLektion(lektion: Lektion) {
        viewModelScope.launch {
            repository.deleteLektion(lektion)
        }
    }

    fun deleteVocab(vocab: Vocab) {
        viewModelScope.launch {
            repository.deleteVocab(vocab)
        }
    }

    class Factory(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LektionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LektionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}