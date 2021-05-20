package com.more.sandboxapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.more.sandboxapp.database.MagicCard
import com.more.sandboxapp.models.State
import com.more.sandboxapp.models.Symbol
import com.more.sandboxapp.repositories.MagicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val magicRepository: MagicRepository,
    val symbolMap: Map<String, Symbol>): ViewModel() {
    val cardDetailFlow = MutableStateFlow<State<MagicCard>>(State.Loading)

    fun fetchCardDetail(id: String) {
        viewModelScope.launch {
            magicRepository.getCardDetails(id).collect { magicCard ->
                delay(1000)
                if (magicCard != null) {
                    cardDetailFlow.value = State.Content(magicCard)
                } else {
                    cardDetailFlow.value = State.Error
                }
            }
        }
    }

}
