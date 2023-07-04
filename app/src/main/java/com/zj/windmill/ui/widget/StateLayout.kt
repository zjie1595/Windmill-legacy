package com.zj.windmill.ui.widget

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zj.windmill.R

const val NONE: Int = -1
const val ERROR: Int = 0
const val LOADING: Int = 1
const val EMPTY: Int = 2
const val SUCCESS: Int = 3

@Composable
fun StateLayout(
    modifier: Modifier = Modifier,
    state: Int,
    content: @Composable () -> Unit
) {
    Crossfade(targetState = state) {
        if (state == SUCCESS) {
            content()
        } else {
            State(state = it, modifier = modifier)
        }
    }
}

@Composable
fun State(modifier: Modifier = Modifier, state: Int) {
    when (state) {
        ERROR -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.network_error),
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }
        LOADING -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
            }
        }
        EMPTY -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.empty_data),
                    modifier = modifier.align(Alignment.Center)
                )
            }
        }
    }
}