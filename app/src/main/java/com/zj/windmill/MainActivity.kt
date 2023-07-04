package com.zj.windmill

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zj.windmill.data.remote.VideoUrlParser
import com.zj.windmill.model.Video
import com.zj.windmill.ui.home.HomeScreen
import com.zj.windmill.ui.play.PlayScreen
import com.zj.windmill.ui.search.SearchScreen
import com.zj.windmill.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalAnimationApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoUrlParser = VideoUrlParser(this)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberAnimatedNavController()
                    AnimatedNavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                onVideoClick = {
                                    navController.navigate("play")
                                },
                                onSearchClick = {
                                    navController.navigate("search")
                                },
                                onHistoryClick = {

                                }
                            )
                        }
                        composable("search") {
                            SearchScreen(
                                onBackClick = {
                                    navController.navigateUp()
                                },
                                onVideoClick = {}
                            )
                        }
                        composable("play/{video}") { backStackEntry ->
                            val video = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                backStackEntry.arguments?.getParcelable("video", Video::class.java)
                            } else {
                                @Suppress("DEPRECATION")
                                backStackEntry.arguments?.getParcelable("video")
                            }
                            PlayScreen(video!!, videoUrlParser = videoUrlParser)
                        }
                    }
                }
            }
        }
    }
}