package com.github.musicyou.ui.styling

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("ClassName")
object Dimensions {
    val itemsVerticalPadding = 12.dp
    const val mediumOpacity = 0.75F
    const val lowOpacity = 0.5F
    val spacer = 24.dp

    object thumbnails {
        val album = 140.dp
        val artist = 200.dp
        val song = 64.dp
        val playlist = album

        object player {
            val song: Dp
                @Composable
                get() = with(LocalConfiguration.current) {
                    minOf(screenHeightDp, screenWidthDp)
                }.dp
        }
    }
}

inline val Dp.px: Int
    @Composable
    inline get() = with(LocalDensity.current) { roundToPx() }
