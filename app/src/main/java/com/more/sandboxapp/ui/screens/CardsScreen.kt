package com.more.sandboxapp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.more.sandboxapp.R
import com.more.sandboxapp.database.MagicCard
import com.more.sandboxapp.models.State
import com.more.sandboxapp.ui.magicCard
import com.more.sandboxapp.ui.viewmodels.CardsViewModel
import com.more.sandboxapp.ui.widgets.MagicLoadingIndicator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CardScreenContainer(cardsViewModel: CardsViewModel = hiltNavGraphViewModel(),
                        setId: String,
                        onCardClick: (MagicCard) -> Unit) {
    cardsViewModel.fetchCardsInSet(setId)
    val state = cardsViewModel.magicCardsFlow.collectAsState().value
    when (state) {
        is State.Content -> CardScreen(state.data, onCardClick)
        is State.Loading -> Box(modifier = Modifier.fillMaxSize()) {
            MagicLoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }
        is State.Error -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error Loading Content")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun CardScreen(cardList: List<MagicCard>, onCardClick: (MagicCard) -> Unit) {
    CardList(magicCardList = cardList, onCardClick)
}

@ExperimentalFoundationApi
@Composable
fun CardList(magicCardList: List<MagicCard>, onCardClick: (MagicCard) -> Unit) {
    LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 180.dp)) {
        itemsIndexed(magicCardList) { _, magicCard ->
            CardView(imageUrl = magicCard.normalImage ?: "") { onCardClick(magicCard) }
        }
    }
}

@Composable
fun CardView(imageUrl: String, onCardClick: () -> Unit) {
    val scale = remember { mutableStateOf(1f)}
    val animScale = animateFloatAsState(targetValue = scale.value)
    Image(
        painter = rememberCoilPainter(
            imageUrl,
            fadeIn = true,
            previewPlaceholder = R.drawable.placeholder_card_normal
        ),
        modifier = Modifier
            .width(width = 240.dp)
            .padding(4.dp)
            .aspectRatio(5f / 7f)
            .clickable {
                if (scale.value != 1f) {
                    scale.value = 1f
                } else {
                    scale.value = 1.5f
                }
                onCardClick()
            }
            .graphicsLayer {
                scaleX = animScale.value
                scaleY = animScale.value
            },
        contentDescription = "",
    )
}

@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewCardList() {
    val magicCardList = MutableList(30) { magicCard }
    CardList(magicCardList = magicCardList) {}
}

@Preview
@Composable
fun PreviewCardView() {
    CardView("https://c1.scryfall.com/file/scryfall-cards/normal/front/0/2/028aeebc-4073-4595-94da-02f9f96ea148.jpg?1562825445") {}
}