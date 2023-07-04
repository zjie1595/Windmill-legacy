@file:OptIn(ExperimentalComposeUiApi::class)

package com.zj.windmill.ui.widget

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.zj.windmill.R

@Composable
fun SearchBar(
    searchText: String,
    showSoftKeyboard: Boolean,
    onSearchTextChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearClick: () -> Unit,
    onSearch: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(true) }
    if (!showSoftKeyboard) {
        keyboardController?.hide()
    }

    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = {
                keyboardController?.hide()
                onBackClick.invoke()
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        isFocused = it.isFocused
                    }
                    .focusRequester(focusRequester),
                value = searchText,
                onValueChange = { onSearchTextChanged.invoke(it) },
                placeholder = {
                    Text(stringResource(R.string.search_video))
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    trailingIconColor = MaterialTheme.colors.surface,
                    cursorColor = MaterialTheme.colors.surface,
                    placeholderColor = MaterialTheme.colors.surface.copy(ContentAlpha.medium)
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = isFocused && searchText.isNotBlank(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { onClearClick.invoke() }) {
                            Icon(Icons.Filled.Close, contentDescription = null)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    onSearch.invoke()
                    keyboardController?.hide()
                }),
                maxLines = 1,
                singleLine = true
            )
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}