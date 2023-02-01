package com.lib.jetutils.composeUtils

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.res.ResourcesCompat
import androidx.emoji.widget.EmojiTextView
import com.lib.jetutils.colors.*
import com.lib.jetutils.utils.*

/**
 * Composable for custom country dialog
 * @param dialogProperties Dialog properties for the dialog box
 * @param onDismiss Callback for onDismiss method
 * @param content Custom composable
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomCountryCodeDialog(
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    onDismiss: () -> Unit,
    content: @Composable (List<Country>) -> Unit

) {
    val countries by remember {
        mutableStateOf(CountryCodeUtil.getCountryCodes())
    }
    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties,
    ) {
        content(countries)
    }
}

/**
 * Composable for country code dialog
 * @param dialogProperties Dialog properties for the dialog box
 * @param onCountrySelected Triggers when user select a country from the list
 * @param onDismiss Callback for onDismiss method
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun CountryCodeDialog(
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit = {}
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    val scrollState = rememberScrollState()

    val filteredCountries by remember {
        derivedStateOf {
            CountryCodeUtil.getCountryCodes().filter {
                it.name.containsIn(searchQuery) || it.phoneCode.containsIn(searchQuery)
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties,
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp),
            elevation = 4.dp,
            backgroundColor = whiteLight,
            shape = RoundedCornerShape(4)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select country code for phone number",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = black43,
                        fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_bold)),
                        fontSize = fontDimensionResource(
                            id = com.lib.jetutils.R.dimen.longDescTextSize
                        )
                    ),
                    textAlign = TextAlign.Center
                )

                TrailingIconSearchBar(
                    hint = "Search countries..", onQueryChange = {
                        searchQuery = it
                    }, modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 24.dp)
                )

                AnimatedVisibility(
                    visible = filteredCountries.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    EmptyListComposable(
                        message = "No countries found!"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp)
                        .padding(horizontal = 24.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState), verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    filteredCountries.forEach { countryCode ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clickable {
                                onCountrySelected(countryCode)
                                onDismiss()
                            }
                            .padding(all = 12.dp),
                            horizontalArrangement = Arrangement.Absolute.SpaceEvenly) {
                            AndroidView(
                                factory = {
                                    EmojiTextView(it).apply {
                                        text =
                                            "${countryCode.code.toEmoji()} ${countryCode.phoneCode}"
                                        setTextColor(ColorStateList.valueOf(Color.BLACK))
                                        textSize = 14f
                                        typeface =
                                            ResourcesCompat.getFont(
                                                context,
                                                com.lib.jetutils.R.font.rubik_semi_bold
                                            )
                                    }
                                },
                                update = {
                                    it.text =
                                        "${countryCode.code.toEmoji()} ${countryCode.phoneCode}"
                                },

                                )
                            Text(
                                text = countryCode.name,
                                color = black43,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_regular))
                                ), textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .fillMaxWidth(1f)
                            )
                        }
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
private fun NationalityDialog(
    dialogProperties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    countries: List<Country>,
    onCountrySelected: (Country) -> Unit,
    onDismiss: () -> Unit = {}
) {

    var searchQuery by remember {
        mutableStateOf("")
    }
    val filteredCountries = countries.filter { it.name.containsIn(searchQuery) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = dialogProperties,
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 20.dp),
            elevation = 4.dp,
            backgroundColor = whiteLight,
            shape = RoundedCornerShape(4)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Nationality",
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = black43,
                        fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_bold)),
                        fontSize = fontDimensionResource(
                            id = com.lib.jetutils.R.dimen.longDescTextSize
                        )
                    ),
                    textAlign = TextAlign.Center
                )

                TrailingIconSearchBar(
                    hint = "Search countries..", onQueryChange = {
                        searchQuery = it
                    }, modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(horizontal = 24.dp)
                )

                AnimatedVisibility(
                    visible = filteredCountries.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    EmptyListComposable(
                        message = "No countries found!"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 10.dp)
                        .padding(horizontal = 24.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(filteredCountries) { countryCode ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clickable {
                                onCountrySelected(countryCode)
                                onDismiss()
                            }
                            .padding(all = 12.dp),
                            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically) {
                            AndroidView(
                                factory = {
                                    EmojiTextView(it).apply {
                                        text =
                                            "${countryCode.code.toEmoji()}"
                                        setTextColor(ColorStateList.valueOf(Color.BLACK))
                                        textSize = 14f
                                    }
                                },
                                update = {
                                    it.text =
                                        "${countryCode.code.toEmoji()}"
                                },
                            )

                            Text(
                                text = countryCode.name,
                                color = black43,
                                style = TextStyle(
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_regular))
                                ), textAlign = TextAlign.Left,
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .fillMaxWidth(1f)
                            )
                        }
                    }
                }

            }
        }
    }
}

/**
 * Composable for Search bar with trailing icon
 * @param hint Hint for the search text input
 * @param defaultValue Default value for the search text (optional)
 * @param onSubmitQuery Invoked when user hits the done key in the keyboard
 * @param onQueryChange Invoked when user inputs value to the text field.
 * @param modifier Modifier for the composable
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TrailingIconSearchBar(
    modifier: Modifier = Modifier,
    hint: String,
    defaultValue: String = "",
    onSubmitQuery: (String) -> Unit = {},
    onQueryChange: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var querry by remember {
        mutableStateOf(TextFieldValue(defaultValue))
    }

    CustomSearchTextField(
        defaultValue = querry,
        onSubmitQuery = {
            onSubmitQuery(it)
            keyboardController?.hide()
        }, onQueryChange = {
            onQueryChange(it)
        },
        modifier = modifier,
        placeholderText = hint,
        leadingIcon = {
            Icon(
                painterResource(id = com.lib.jetutils.R.drawable.ic_search_normal),
                "search",
                modifier = Modifier.padding(
                    horizontal = dimensionResource(
                        id = com.lib.jetutils.R.dimen.padding20
                    )
                )
            )
        })

}

/**
 * Composable for custom search text field
 * @param modifier Modifier for the composable
 * @param defaultValue Default text field value
 * @param leadingIcon Leading icon for the search text field (optional)
 * @param trailingIcon Trailing icon for the search text field (optional)
 * @param placeholderText Placeholder(Hint) for the search text field
 * @param onSubmitQuery Invoked when user hits the done key in the keyboard
 * @param onQueryChange Invoked when user inputs value to the text field.
 */
@Composable
private fun CustomSearchTextField(
    modifier: Modifier = Modifier,
    defaultValue: TextFieldValue = TextFieldValue(""),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    placeholderText: String,
    onSubmitQuery: (String) -> Unit,
    onQueryChange: (String) -> Unit
) {

    var query by remember {
        mutableStateOf(defaultValue)
    }

    BasicTextField(modifier = modifier
        .persistShortKeyForInput(query)
        .background(white, RoundedCornerShape(10.dp))
        .height(dimensionResource(id = com.lib.jetutils.R.dimen.searchBarHeight)),
        value = query,
        onValueChange = {
            query = it
            onQueryChange(query.text)
        },
        keyboardActions = KeyboardActions {
            // handle querry
            onSubmitQuery(query.text)
        },
        singleLine = true,
        cursorBrush = SolidColor(cream),
        textStyle = LocalTextStyle.current.copy(
            color = black,
            fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_regular)),
            fontSize = fontDimensionResource(id = com.lib.jetutils.R.dimen.longDescTextSize)
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                    if (query.text.isEmpty()) Text(
                        placeholderText,
                        style = TextStyle(
                            color = ash99,
                            fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_regular)),
                            fontSize = fontDimensionResource(id = com.lib.jetutils.R.dimen.longDescTextSize)
                        )
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}

/**
* Composable which is loaded for empty list
 * @param modifier Modifier for the composable
 * @param message Message to display
 * @param textFontSize Font size of the message
*/
@Composable
fun EmptyListComposable(
    modifier: Modifier = Modifier,
    message: String,
    textFontSize: TextUnit = fontDimensionResource(id = com.lib.jetutils.R.dimen.textSize18)
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = com.lib.jetutils.R.drawable.list_empty),
            modifier = Modifier.size(200.dp),
            contentDescription = "list empty state",
            contentScale = ContentScale.Crop
        )
        Text(
            text = message,
            style = TextStyle(
                color = black43,
                fontSize = textFontSize,
                fontFamily = FontFamily(Font(com.lib.jetutils.R.font.rubik_semi_bold)),
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = dimensionResource(id = com.lib.jetutils.R.dimen.padding34))
        )

    }
}



