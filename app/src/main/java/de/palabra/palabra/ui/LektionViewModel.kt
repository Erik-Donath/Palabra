package de.palabra.palabra.ui

import androidx.lifecycle.*
import de.palabra.palabra.db.Lektion
import de.palabra.palabra.db.Repository
import de.palabra.palabra.db.Vocab
import kotlinx.coroutines.launch

class LektionViewModel(private val repository: Repository) : ViewModel() {
    private val _searchQuery = MutableLiveData("")
    private val _expandedLektionIds = MutableLiveData<Set<Int>>(emptySet())
    private val _lektionVocabs = MutableLiveData<Map<Int, List<Vocab>>>(emptyMap())

    val allLektionen: LiveData<List<Lektion>> =
        repository.getAllLektionenFlow().asLiveData()

    val filteredLektionen: LiveData<List<Lektion>> =
        MediatorLiveData<List<Lektion>>().apply {
            fun update() {
                val query = _searchQuery.value.orEmpty().lowercase()
                val data = allLektionen.value.orEmpty()
                value = if (query.isBlank()) data
                else data.filter { it.title.lowercase().contains(query) }
            }
            addSource(allLektionen) { update() }
            addSource(_searchQuery) { update() }
        }

    val expandedLektionIds: LiveData<Set<Int>> = _expandedLektionIds
    val lektionVocabs: LiveData<Map<Int, List<Vocab>>> = _lektionVocabs

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleExpansion(lektionId: Int) {
        val currentExpanded = _expandedLektionIds.value.orEmpty()
        val isCurrentlyExpanded = currentExpanded.contains(lektionId)
        
        _expandedLektionIds.value = if (isCurrentlyExpanded) {
            currentExpanded - lektionId
        } else {
            loadVocabsForLektion(lektionId)
            currentExpanded + lektionId
        }
    }

    private fun loadVocabsForLektion(lektionId: Int) {
        viewModelScope.launch {
            val vocabs = repository.getVocabsForLektion(lektionId)
            val currentMap = _lektionVocabs.value.orEmpty().toMutableMap()
            currentMap[lektionId] = vocabs
            _lektionVocabs.value = currentMap
        }
    }

    fun getVocabsForLektion(lektionId: Int): List<Vocab> {
        return _lektionVocabs.value?.get(lektionId).orEmpty()
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
            if (isExpanded(vocab.lektionId)) {
                loadVocabsForLektion(vocab.lektionId)
            }
        }
    }

    fun addVocab(vocab: Vocab) {
        viewModelScope.launch {
            repository.insertVocab(vocab)
            if (isExpanded(vocab.lektionId)) {
                loadVocabsForLektion(vocab.lektionId)
            }
        }
    }

    fun refreshData() {
        val expandedIds = _expandedLektionIds.value.orEmpty()
        expandedIds.forEach { lektionId ->
            loadVocabsForLektion(lektionId)
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
