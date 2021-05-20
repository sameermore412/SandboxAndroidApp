package com.more.sandboxapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.google.accompanist.coil.LocalImageLoader
import com.google.accompanist.coil.rememberCoilPainter
import com.more.sandboxapp.ui.screens.CardDetailScreenContainer
import com.more.sandboxapp.ui.screens.CardScreenContainer
import com.more.sandboxapp.ui.screens.SetsScreenContainer
import com.more.sandboxapp.ui.theme.SandboxAppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalFoundationApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var titleState =  remember { mutableStateOf("Magic App") }
            var iconUrl = remember { mutableStateOf<String?>(null) }
            var appBarVisible = remember { mutableStateOf(true)}

            SandboxAppTheme {
                Scaffold(topBar = {
                    if (appBarVisible.value) {
                        AppBar(titleState = titleState, iconState = iconUrl)
                    }
                }) {
                    DefineNavigation(navController = navController, titleState, iconUrl, appBarVisible)
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun DefineNavigation(
    navController: NavHostController,
    titleState: MutableState<String>,
    iconState: MutableState<String?>,
    appBarVisible: MutableState<Boolean>
) {

    NavHost(navController = navController, startDestination = "sets") {
        composable(route = "sets") {
            appBarVisible.value = true
            SetsScreenContainer { magicSet ->
                titleState.value = magicSet.name
                iconState.value = magicSet.iconUrl
                navController.navigate("cards/${magicSet.id}")
            }
        }
        composable(route = "cards/{setId}",
            arguments = listOf(
                navArgument(name = "setId") { type = NavType.StringType},
            )) {
            appBarVisible.value = true
            val setId = it.arguments?.getString("setId")
            CardScreenContainer(setId = setId!!) { magicCard ->
                titleState.value = magicCard.name
                iconState.value = null
                navController.navigate("cardDetail/${magicCard.id}")
            }
        }
        composable(route = "cardDetail/{cardId}",
            arguments = listOf(
                navArgument(name = "cardId") {type = NavType.StringType}
            )) {
            val cardId = it.arguments?.getString("cardId")
            appBarVisible.value = false
            CardDetailScreenContainer(magicCardId = cardId!!)
        }
    }
}

@Composable
fun AppBar(titleState: MutableState<String>, iconState: MutableState<String?>) {
    TopAppBar(
        title = { AppBarTitle(titleState = titleState, iconState = iconState)},
        elevation = 12.dp)
}

@Composable
fun AppBarTitle(titleState: MutableState<String>, iconState: MutableState<String?>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .componentRegistry {
                add(SvgDecoder(LocalContext.current))
            }
            .build()

        iconState.value?.let {
            CompositionLocalProvider(LocalImageLoader provides imageLoader) {
                Image(
                    painter = rememberCoilPainter(
                        it,
                        fadeIn = true,
                    ),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(
                            color = MaterialTheme.colors.onPrimary,
                            shape = CircleShape
                        )
                        .padding(4.dp)
                        .size(32.dp)
                )
            }
        }
        Text(text = titleState.value)
    }
}