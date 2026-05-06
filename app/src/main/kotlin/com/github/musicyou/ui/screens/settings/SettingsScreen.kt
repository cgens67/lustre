package com.github.musicyou.ui.screens.settings

import android.content.pm.PackageManager
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.musicyou.LocalPlayerPadding
import com.github.musicyou.R
import com.github.musicyou.enums.SettingsSection
import com.github.musicyou.ui.components.ValueSelectorDialog
import com.github.musicyou.ui.styling.Dimensions

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun SettingsScreen(
    pop: () -> Unit,
    onGoToSettingsPage: (Int) -> Unit
) {
    val playerPadding = LocalPlayerPadding.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    
    val version = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
    } catch (e: PackageManager.NameNotFoundException) {
        "1.0.0"
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = pop) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(top = 8.dp, bottom = 16.dp + playerPadding)
        ) {
            
            // Top Wide Card (App Info / About mapping to Login styling)
            Surface(
                onClick = { onGoToSettingsPage(SettingsSection.About.ordinal) },
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Login,
                            contentDescription = null,
                            modifier = Modifier.padding(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "App Info",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Music You",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "v$version",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // 2x2 Grid for core settings
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingsGridCard(
                    section = SettingsSection.General,
                    onClick = { onGoToSettingsPage(SettingsSection.General.ordinal) },
                    modifier = Modifier.weight(1f)
                )
                SettingsGridCard(
                    section = SettingsSection.Player,
                    onClick = { onGoToSettingsPage(SettingsSection.Player.ordinal) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingsGridCard(
                    section = SettingsSection.Cache,
                    onClick = { onGoToSettingsPage(SettingsSection.Cache.ordinal) },
                    modifier = Modifier.weight(1f)
                )
                SettingsGridCard(
                    section = SettingsSection.Database,
                    onClick = { onGoToSettingsPage(SettingsSection.Database.ordinal) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(24.dp))

            // Bottom List
            Text(
                text = "MORE SETTINGS",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            SettingsListItem(
                section = SettingsSection.Gestures,
                onClick = { onGoToSettingsPage(SettingsSection.Gestures.ordinal) }
            )
            SettingsListItem(
                section = SettingsSection.Other,
                onClick = { onGoToSettingsPage(SettingsSection.Other.ordinal) }
            )
        }
    }
}

@Composable
fun SettingsGridCard(
    section: SettingsSection,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f),
        modifier = modifier.aspectRatio(1.1f)
    ) {
        Column(Modifier.padding(20.dp)) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(id = section.resourceId),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SettingsListItem(
    section: SettingsSection,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(id = section.resourceId),
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                Icon(
                    imageVector = section.icon,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
inline fun <reified T : Enum<T>> EnumValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    crossinline onValueSelected: (T) -> Unit,
    icon: ImageVector,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = Enum<T>::name,
    noinline trailingContent: @Composable (() -> Unit)? = null
) {
    ValueSelectorSettingsEntry(
        title = title,
        selectedValue = selectedValue,
        values = enumValues<T>().toList(),
        onValueSelected = onValueSelected,
        icon = icon,
        isEnabled = isEnabled,
        valueText = valueText,
        trailingContent = trailingContent,
    )
}

@Composable
inline fun <T> ValueSelectorSettingsEntry(
    title: String,
    selectedValue: T,
    values: List<T>,
    crossinline onValueSelected: (T) -> Unit,
    icon: ImageVector,
    isEnabled: Boolean = true,
    crossinline valueText: (T) -> String = { it.toString() },
    noinline trailingContent: @Composable (() -> Unit)? = null
) {
    var isShowingDialog by remember { mutableStateOf(false) }

    if (isShowingDialog) {
        ValueSelectorDialog(
            onDismiss = { isShowingDialog = false },
            title = title,
            selectedValue = selectedValue,
            values = values,
            onValueSelected = onValueSelected,
            valueText = valueText
        )
    }

    SettingsEntry(
        title = title,
        text = valueText(selectedValue),
        icon = icon,
        onClick = { isShowingDialog = true },
        isEnabled = isEnabled,
        trailingContent = trailingContent
    )
}

@Composable
fun SwitchSettingEntry(
    title: String,
    text: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isEnabled: Boolean = true
) {
    SettingsEntry(
        title = title,
        text = text,
        icon = icon,
        onClick = { onCheckedChange(!isChecked) },
        isEnabled = isEnabled
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = isEnabled
        )
    }
}

@Composable
fun SettingsEntry(
    title: String,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    trailingContent: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = {
            Text(text = title)
        },
        modifier = Modifier.clickable(enabled = isEnabled, onClick = onClick).alpha(if (isEnabled) 1F else Dimensions.lowOpacity),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = title
            )
        },
        supportingContent = {
            Text(text = text)
        },
        trailingContent = { trailingContent?.invoke() },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}
