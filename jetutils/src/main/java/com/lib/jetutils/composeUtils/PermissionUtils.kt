package com.lib.jetutils.composeUtils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState

/**
 *# Permissions
 * Method to show the permission dialog with provided permissions.
 * @param permissions List of permissions.
 * @param permissionText The display text for the dialog box, if user don't have a specific ui for requesting.
 * @param enterTransition Enter Animation.
 * @param exitTransition Exit Animation.
 * @param onPermissionNotGranted invoked when any of the permission is denied.
 * @param onPermissionNotAvailable invoked when the permission is not available. (showing rationale)
 * @param onAllPermissionsEnabled invoked when all permissions are enabled.
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalAnimationApi::class)
@Composable
fun PermissionDialog(
    permissions: List<String>,
    enterTransition: EnterTransition = fadeIn(),
    exitTransition: ExitTransition = fadeOut(),
    onPermissionNotGranted: @Composable (deniedPermissions: List<String>, permissionLauncher: MultiplePermissionsState) -> Unit,
    onPermissionNotAvailable: @Composable (deniedPermissions: List<String>) -> Unit,
    onAllPermissionsEnabled: @Composable () -> Unit,
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    var showPermission = remember {
        mutableStateOf(true)
    }

    AnimatedVisibility(
        visible = showPermission.value,
        enter = enterTransition,
        exit = exitTransition
    ) {

        PermissionsRequired(
            multiplePermissionsState = permissionState,
            permissionsNotGrantedContent = {
                onPermissionNotGranted(LocalContext.current.findDeniedPermissions(permissions), permissionState)
            }, permissionsNotAvailableContent = {
                // show UI for rationale
                onPermissionNotAvailable(LocalContext.current.findDeniedPermissions(permissions))
            }) {
            showPermission.value = false
            onAllPermissionsEnabled()
        }
    }
}

/**
* Method to find the denied permissions
 * @param requestedPermissions permission requested by the user
 * @return list of denied permission
*/
private fun Context.findDeniedPermissions(requestedPermissions: List<String>): List<String> {
    val deniedPermissions = arrayListOf<String>()
    requestedPermissions.forEach { permission ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val isDenied =
                checkSelfPermission(
                    permission
                ) == PackageManager.PERMISSION_DENIED
            if (isDenied)
                deniedPermissions.add(permission)
        }
    }
    return deniedPermissions
}
