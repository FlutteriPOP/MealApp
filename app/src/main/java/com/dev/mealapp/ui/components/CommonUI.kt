package com.dev.mealapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun ErrorView(error: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(24.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Something went wrong",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Retry Connection", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BadgeExpressive(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    textColor: Color = Color.Unspecified,
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = if (textColor == Color.Unspecified) MaterialTheme.colorScheme.onPrimaryContainer else textColor,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun YoutubePlayer(youtubeUrl: String, modifier: Modifier = Modifier, onReady: () -> Unit) {
    val videoId = remember(youtubeUrl) {
        try {
            if (youtubeUrl.contains("v=")) {
                youtubeUrl.substringAfter("v=").substringBefore("&")
            } else {
                youtubeUrl.substringAfterLast("/")
            }
        } catch (_: Exception) {
            ""
        }
    }

    if (videoId.isBlank()) return

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    Box(modifier = modifier.clip(RoundedCornerShape(0.dp))) {
        AndroidView(
            factory = { context ->
                YouTubePlayerView(context).apply {
                    enableAutomaticInitialization = false
                    lifecycleOwner.lifecycle.addObserver(this)
                    val options = IFramePlayerOptions.Builder(context)
                        .controls(0)
                        .autoplay(1)
                        .mute(1)
                        .build()

                    initialize(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(videoId, 0f)
                            onReady()
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            if (state == PlayerConstants.PlayerState.ENDED) {
                                youTubePlayer.loadVideo(videoId, 0f)
                            }
                        }
                    }, options)
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .scale(1.8f),
            onRelease = { view ->
                lifecycleOwner.lifecycle.removeObserver(view)
                view.release()
            }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(enabled = true, onClick = {})
        )
    }
}

