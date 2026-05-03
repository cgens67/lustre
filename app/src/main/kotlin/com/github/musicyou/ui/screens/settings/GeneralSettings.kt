package com.github.musicyou.ui.screens.settings

import android.app.Activity
import android.app.LocaleManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.AddLink
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.core.net.toUri
import com.github.musicyou.LocalPlayerPadding
import com.github.musicyou.R
import com.github.musicyou.enums.AppLanguage
import com.github.musicyou.enums.NavigationLabelsVisibility
import com.github.musicyou.enums.QuickPicksSource
import com.github.musicyou.utils.isAtLeastAndroid12
import com.github.musicyou.utils.isAtLeastAndroid13
import com.github.musicyou.utils.isShowingThumbnailInLockscreenKey
import com.github.musicyou.utils.navigationLabelsVisibilityKey
import com.github.musicyou.utils.preferences
import com.github.musicyou.utils.quickPicksSourceKey
import com.github.musicyou.utils.rememberPreference
import com.github.musicyou.utils.toast

@Composable
fun GeneralSettings() {
    val playerPadding = LocalPlayerPadding.current

    val context = LocalContext.current
    var navigationLabelsVisibility by rememberPreference(
        navigationLabelsVisibilityKey,
        NavigationLabelsVisibility.Visible
    )
    var quickPicksSource by rememberPreference(quickPicksSourceKey, QuickPicksSource.Trending)
    var isShowingThumbnailInLockscreen by rememberPreference(
        isShowingThumbnailInLockscreenKey,
        false
    )

    var currentLanguage by remember {
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales.toLanguageTags()
        } else {
            context.preferences.getString("app_language", "") ?: ""
        }
        mutableStateOf(AppLanguage.entries.find { it.code == code } ?: AppLanguage.SYSTEM)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp + playerPadding)
    ) {
        EnumValueSelectorSettingsEntry(
            title = stringResource(id = R.string.app_language),
            selectedValue = currentLanguage,
            onValueSelected = { language ->
                currentLanguage = language
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val localeManager = context.getSystemService(LocaleManager::class.java)
                    localeManager.applicationLocales = LocaleList.forLanguageTags(language.code)
                } else {
                    context.preferences.edit { putString("app_language", language.code) }
                    (context as? Activity)?.recreate()
                }
            },
            icon = Icons.Outlined.Language,
            valueText = { it.displayName }
        )

        EnumValueSelectorSettingsEntry(
            title = stringResource(id = R.string.navigation_bar_label_visibility),
            selectedValue = navigationLabelsVisibility,
            onValueSelected = { navigationLabelsVisibility = it },
            icon = Icons.Outlined.Visibility,
            valueText = { context.getString(it.resourceId) }
        )

        EnumValueSelectorSettingsEntry(
            title = stringResource(id = R.string.quick_picks_source),
            selectedValue = quickPicksSource,
            onValueSelected = { quickPicksSource = it },
            icon = Icons.AutoMirrored.Outlined.List,
            valueText = { context.getString(it.resourceId) }
        )

        if (isAtLeastAndroid12) {
            val intent = Intent(
                Settings.ACTION_APP_OPEN_BY_DEFAULT_SETTINGS,
                "package:${context.packageName}".toUri()
            )

            SettingsEntry(
                title = stringResource(id = R.string.open_supported_links_by_default),
                text = stringResource(id = R.string.configure_supported_links),
                icon = Icons.Outlined.AddLink,
                onClick = {
                    try {
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        context.toast("Couldn't find supported links settings, please configure them manually")
                    }
                }
            )
        }

        if (!isAtLeastAndroid13) {
            SwitchSettingEntry(
                title = stringResource(id = R.string.show_song_cover),
                text = stringResource(id = R.string.show_song_cover_description),
                icon = Icons.Outlined.Image,
                isChecked = isShowingThumbnailInLockscreen,
                onCheckedChange = { isShowingThumbnailInLockscreen = it }
            )
        }
    }
}
