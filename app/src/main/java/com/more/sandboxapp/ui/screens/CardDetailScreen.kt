package com.more.sandboxapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import cardAspectRatio
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.more.sandboxapp.R
import com.more.sandboxapp.database.MagicCard
import com.more.sandboxapp.models.State
import com.more.sandboxapp.models.Symbol
import com.more.sandboxapp.models.toInlineTextContentMap
import com.more.sandboxapp.ui.magicCard
import com.more.sandboxapp.ui.theme.typography
import com.more.sandboxapp.ui.viewmodels.CardDetailViewModel
import com.more.sandboxapp.ui.widgets.MagicLoadingIndicator

@Composable
fun CardDetailScreenContainer(cardDetailViewModel: CardDetailViewModel = hiltNavGraphViewModel(), magicCardId: String){
    cardDetailViewModel.fetchCardDetail(magicCardId)

    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .componentRegistry {
            add(SvgDecoder(LocalContext.current))
        }
        .build()
    val state by cardDetailViewModel.cardDetailFlow.collectAsState()

    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        when (state) {
            is State.Content -> CardDetailScreen((state as State.Content<MagicCard>).data, cardDetailViewModel.symbolMap)
            is State.Loading -> Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                MagicLoadingIndicator()
            }
            is State.Error -> Box(contentAlignment = Alignment.Center) {
                Text("Error getting card details.")
            }
        }
    }
}

@Composable
fun CardDetailScreen(card: MagicCard?, symbolMap: Map<String, Symbol>) {
    if (card != null) {
        CardDetail(card, symbolMap)
    } else {
        Text("Missing Card Data")
    }
}

@Composable
fun CardDetail(card: MagicCard, symbolMap: Map<String, Symbol>) {
    Box(modifier = Modifier
        .then(
            if (card.getAllColors().size > 1) {
                Modifier.background(Brush.horizontalGradient(card.getAllColors()))
            } else {
                Modifier.background(Brush.verticalGradient(listOf(card.getPrimaryColor(), Color.LightGray)))
            }
        )
        .fillMaxSize()){
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()) {
            Box {
                HeroImage(card.artCropImage ?: "")
                MiniCardImage(
                    url = card.normalImage ?: "",
                    modifier = Modifier
                        .width(96.dp)
                        .padding(start = 8.dp)
                        .aspectRatio(cardAspectRatio)
                        .align(Alignment.BottomStart)
                        .absoluteOffset(y = (96 / 2).dp))
            }
            Column(modifier = Modifier
                .absoluteOffset(x = 108.dp)) {
                Text(
                    text = card.name,
                    style = typography.h5,
                    color = Color.Black
                )
                card.manaCost?.let { manaCost ->
                    TextWithInlineImages(
                        text = manaCost,
                        symbolMap = symbolMap)
                }
            }

            Column(modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp)) {

                card.oracleText?.let { oracleText ->
                    TextWithInlineImages(
                        text = oracleText.replace("\n", "\n\n"),
                        symbolMap = symbolMap)
                }
                card.flavorText?.let { flavorText ->
                    if (card.oracleText != null) {
                        Divider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    TextWithInlineImages(
                        text = flavorText,
                        symbolMap = symbolMap,
                        fontStyle = FontStyle.Italic)
                }
            }
        }
    }
}

@Composable
fun TextWithInlineImages(text: String,
                         symbolMap: Map<String, Symbol>,
                         modifier: Modifier = Modifier,
                         fontStyle: FontStyle = FontStyle.Normal) {
    val symbols = symbolMap.keys
        Text(
            text = buildAnnotatedString {
                var match = text.findAnyOf(symbols)
                var start = 0
                while (match != null) {
                    val begin = match.first
                    val length = match.second.length

                    append(text.substring(start, begin))
                    appendInlineContent(text.substring(begin, begin + length))

                    start = begin + length
                    match = text.findAnyOf(symbols, start)
                }
                if (start < text.length) {
                    append(text.substring(start))
                }
            },
            color = Color.Black,
            fontStyle = fontStyle,
            modifier  = modifier,
            inlineContent = symbolMap.toInlineTextContentMap(::toInlineTextContent))
}


fun toInlineTextContent(symbol: Symbol): InlineTextContent {
    return InlineTextContent(
        Placeholder(
            width = 16.sp,
            height = 16.sp,
            placeholderVerticalAlign = PlaceholderVerticalAlign.Center
        )
    ) {
        Image(
            painter = rememberCoilPainter(
                request = symbol.svg_uri,
                fadeIn = true,
                previewPlaceholder = R.drawable.placeholder_card_normal
            ),
            contentDescription = "",
        )
    }
}

@Composable
fun MiniCardImage(url: String, modifier: Modifier = Modifier) {
    Image(
        painter = rememberCoilPainter(
            request = url,
            fadeIn = true,
            previewPlaceholder = R.drawable.placeholder_card_normal
        ),
        contentDescription = "",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}

@Composable
fun HeroImage(url: String) {
    Image(
        painter = rememberCoilPainter(
            request = url,
            fadeIn = true,
            previewPlaceholder = R.drawable.placeholder_card_art_crop
        ),
        contentDescription = "",
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}


@Preview(showSystemUi = true, widthDp = 600)
@Composable
fun PreviewCardDetailScreen() {
    CardDetailScreen(card = magicCard, symbolMap = mapOf("{U}" to Symbol("{U}", "")))
}