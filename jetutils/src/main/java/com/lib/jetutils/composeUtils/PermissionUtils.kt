package com.lib.jetutils.composeUtils

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.lib.jetutils.R
import com.lib.jetutils.utils.openAppSettings

/**
 * Method to show the permission dialog with provided permissions.
 * @param visible Mutable state to handle the visibility of the dialog.
 * @param permissions List of permissions.
 * @param permissionText The display text for the dialog box, if user don't have a specific ui for requesting.
 * @param enterTransition Enter Animation.
 * @param exitTransition Exit Animation.
 * @param onAllPermissionsEnabled invoked when all permissions are enabled.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
@Composable
fun PermissionDialog(
    visible: MutableState<Boolean>,
    permissions: List<String>,
    permissionText: String? = null,
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    onAllPermissionsEnabled: @Composable () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    var checkPermission by remember {
        mutableStateOf(visible)
    }

    AnimatedVisibility(
        visible = checkPermission.value,
        enter = enterTransition,
        exit = exitTransition
    ) {

        PermissionsRequired(
            multiplePermissionsState = permissionState,
            permissionsNotGrantedContent = {
                NonGrantedUi(text = permissionText, onRequestPermission = {
                    permissionState.launchMultiplePermissionRequest()
                }, onDismiss = {
                    checkPermission.value = false
                })
            }, permissionsNotAvailableContent = {
                PermanentlyDeniedUi(dismissPopup = {
                    checkPermission.value = false
                })
            }) {
            onAllPermissionsEnabled()
        }
    }
}

@Composable
private fun NonGrantedUi(text: String?, onRequestPermission: () -> Unit, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(4)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
            ) {
                val (textRef, closeRef, buttonRef,
                    permTitleRef) = createRefs()
                Text(
                    text = stringResource(id = R.string.permission_alert),
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp
                    ), modifier = Modifier.constrainAs(permTitleRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(closeRef.top, 10.dp)
                        bottom.linkTo(closeRef.bottom, 10.dp)
                    }
                )

                Image(painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "close",
                    modifier = Modifier
                        .size(34.dp)
                        .constrainAs(closeRef) {
                            top.linkTo(parent.top, 10.dp)
                            end.linkTo(parent.end, 10.dp)
                        }
                        .clickable {
                            onDismiss()
                        })

                Text(
                    text = text ?: stringResource(id = R.string.default_permission_text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .padding(horizontal = 15.dp)
                        .constrainAs(textRef) {
                            start.linkTo(parent.start)
                            top.linkTo(closeRef.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(buttonRef.top)
                            width = Dimension.fillToConstraints
                        },
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .constrainAs(buttonRef) {
                            start.linkTo(parent.start)
                            top.linkTo(textRef.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(37.dp)   //add dynamic
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                onRequestPermission()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.request_permission_button),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.Default,
                                fontSize = 14.sp,
                            ), modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }


            }
        }
    }
}


@Composable
private fun PermanentlyDeniedUi(dismissPopup: () -> Unit) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = { dismissPopup() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
        )
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(4)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 24.dp
                    ),
            ) {
                val (textRef, closeRef, buttonRef,
                    permTitleRef) = createRefs()
                Text(
                    text = stringResource(id = R.string.permission_alert),
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        color = Color.Black,
                        fontFamily = FontFamily.Default,
                        fontSize = 14.sp
                    ), modifier = Modifier.constrainAs(permTitleRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(closeRef.top, 10.dp)
                        bottom.linkTo(closeRef.bottom, 10.dp)
                    }
                )

                Image(painter = painterResource(id = R.drawable.close_icon),
                    contentDescription = "close",
                    modifier = Modifier
                        .size(34.dp)
                        .constrainAs(closeRef) {
                            top.linkTo(parent.top, 10.dp)
                            end.linkTo(parent.end, 10.dp)
                        }
                        .clickable {
                            dismissPopup()
                        })

                Text(
                    text = stringResource(id = R.string.permission_permanently_denied),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .padding(horizontal = 15.dp)
                        .constrainAs(textRef) {
                            start.linkTo(parent.start)
                            top.linkTo(closeRef.bottom)
                            end.linkTo(parent.end)
                            bottom.linkTo(buttonRef.top)
                            width = Dimension.fillToConstraints
                        },
                    textAlign = TextAlign.Left,
                    color = Color.Black,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default
                    )
                )
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .constrainAs(buttonRef) {
                            start.linkTo(parent.start)
                            top.linkTo(textRef.bottom)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints
                        }, horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .height(37.dp)   //add dynamic
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                context.openAppSettings()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.open_app_settings),
                            style = TextStyle(
                                color = Color.Black,
                                fontFamily = FontFamily.Default,
                                fontSize = 14.sp,
                            ), modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }
            }
        }
    }
}