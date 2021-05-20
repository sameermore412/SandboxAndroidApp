package com.more.sandboxapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.more.sandboxapp.database.MagicSet
import com.more.sandboxapp.models.State
import com.more.sandboxapp.repositories.MagicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetsViewModel @Inject constructor(private val magicRepository: MagicRepository) : ViewModel() {
    val officialSetsFlow = MutableStateFlow<State<List<MagicSet>>>(State.Loading)
    val coreSetsFlow = MutableStateFlow<State<List<MagicSet>>>(State.Loading)

    fun fetchOfficialSets() {
        viewModelScope.launch {
            magicRepository.getScryFallSets().collect { sets ->
                val filteredSets = sets.filter { it.code.length == 3 }.sortedByDescending { it.releasedDate }
                officialSetsFlow.emit(State.Content(filteredSets))
            }
        }
    }

    fun fetchCoreSets() {
        viewModelScope.launch {
            magicRepository.getScryFallSets().collect { sets ->
                val filteredSets = sets.filter { it.type == "core" }.sortedByDescending { it.releasedDate }
                coreSetsFlow.emit(State.Content(filteredSets))
            }
        }
    }
}