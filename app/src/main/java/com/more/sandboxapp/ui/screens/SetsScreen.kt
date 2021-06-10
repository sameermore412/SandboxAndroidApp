package com.more.sandboxapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cardAspectRatio
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.more.sandboxapp.database.MagicSet
import com.more.sandboxapp.models.State
import com.more.sandboxapp.ui.viewmodels.SetsViewModel
import com.more.sandboxapp.ui.widgets.MagicLoadingIndicator

val setWidth = 180.dp

@ExperimentalMaterialApi
@Composable
fun SetsScreenContainer(setViewModel: SetsViewModel = hiltViewModel(), openSet: (MagicSet) -> Unit) {
    setViewModel.fetchCoreSets()
    setViewModel.fetchOfficialSets()

    val coreSetsState by setViewModel.coreSetsFlow.collectAsState()
    val officialSetsState by setViewModel.officialSetsFlow.collectAsState()

    if (coreSetsState is State.Content && officialSetsState is State.Content) {
        SetsScreen((officialSetsState as State.Content<List<MagicSet>>).data, (coreSetsState as State.Content<List<MagicSet>>).data, openSet)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            MagicLoadingIndicator()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SetsScreen(latestSets: List<MagicSet>, coreSets: List<MagicSet>, openSet: (MagicSet) -> Unit) {
    Box(contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            SetRow("Latest Sets", latestSets, openSet)
            SetRow("Core Sets", coreSets, openSet)
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SetRow(title: String, cardSetList: List<MagicSet>, openSet: (MagicSet) -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.h5, modifier = Modifier.padding(start = 8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
            items(cardSetList) { setItem ->
                SetCard(setItem, openSet)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun SetCard(magicSet: MagicSet, openSet: (MagicSet) -> Unit) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry {
            add(SvgDecoder(LocalContext.current))
        }
        .build()
    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        Card(
            onClick = {openSet(magicSet)},
            modifier = Modifier
                .width(setWidth)
                .aspectRatio(cardAspectRatio)
                .padding(end = 8.dp)
        ) {
            Image(
                painter = rememberCoilPainter(
                    magicSet.iconUrl,
                    fadeIn = true,),
                contentDescription = magicSet.name,
                modifier = Modifier.padding(20.dp)
            )
            Text(text = magicSet.name)
        }
    }
}

@Composable
fun AddSetCard(addAction: () -> Unit) {
    Card(modifier = Modifier
        .width(setWidth)
        .aspectRatio(cardAspectRatio)
        .padding(end = 8.dp)
        .clickable { addAction() }) {

        Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add",
            modifier = Modifier
                .requiredSize(80.dp)
                .background(color = Color.Companion.LightGray, shape = CircleShape))
    }
}