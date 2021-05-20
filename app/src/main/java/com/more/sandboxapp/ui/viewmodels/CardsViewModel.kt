package com.more.sandboxapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.more.sandboxapp.database.MagicCard
import com.more.sandboxapp.models.State
import com.more.sandboxapp.repositories.MagicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(private val magicRepository: MagicRepository): ViewModel() {
    val magicCardsFlow = MutableStateFlow<State<List<MagicCard>>>(State.Loading)

    fun fetchCardsInSet(setId: String) {
        viewModelScope.launch {
            magicRepository.getCardsInSet(setId).collect{
                magicCardsFlow.emit(State.Content(it))
            }
        }
    }
}