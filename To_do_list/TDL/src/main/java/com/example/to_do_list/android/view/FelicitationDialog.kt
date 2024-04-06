package com.example.to_do_list.android.view

import android.content.Context
import android.view.Window
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun FelicitationDialog(
    dialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    contenu: @Composable () -> Unit,
) {

    var dialogAnimeVisible by remember { mutableStateOf(false) }

    LaunchedEffect(dialogVisible) {
        if (dialogVisible) dialogAnimeVisible = true
    }

    if (dialogAnimeVisible) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            val fenetreDialog = fenetreDialog()

            SideEffect {
                fenetreDialog.let { fenetre ->
                    fenetre?.setDimAmount(0f)
                    fenetre?.setWindowAnimations(-1)
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                var animationIn by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { animationIn = true }
                AnimatedVisibility(
                    visible = animationIn && dialogVisible,
                    enter = fadeIn(),
                    exit = fadeOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .pointerInput(Unit) { detectTapGestures { onDismissRequest() } }
                            .background(Color.Black.copy(alpha = .56f))
                            .fillMaxSize()
                            .confetti(
                                contentColors = listOf(
                                    Color(0xFF80DEEA),
                                    Color(0xFFC5E1A5),
                                    Color(0xFFBA68C8),
                                    Color(0xFFFFCC80)
                                ),
                                confettiShape = FormeConfetti.Mix,
                                vitesse = 0.07F,
                                quantite = 0.3f,
                                estVisible = true
                            ),
                    )
                }
                AnimatedVisibility(
                    visible = animationIn && dialogVisible,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessHigh)) + scaleIn(
                        initialScale = .8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                    ),
                    exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
                ) {
                    Box(
                        Modifier
                            .pointerInput(Unit) { detectTapGestures { } }
                            .shadow(8.dp, shape = RoundedCornerShape(16.dp))
                            .width(300.dp)
                            .height(300.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                MaterialTheme.colorScheme.surface,
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        contenu()
                    }

                    DisposableEffect(Unit) {
                        onDispose {
                            dialogAnimeVisible = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Felicitation(
    onDismissRequest: () -> Unit,
    applicationContext: Context,
    url: String
) {
    Column(Modifier.background(MaterialTheme.colorScheme.surface)) {

        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { visible = true }

        AnimatedVisibility(
            visible = visible,
            enter = expandVertically(
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                expandFrom = Alignment.CenterVertically,
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(178.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF80DEEA),
                                Color(0xFFC5E1A5),
                                Color(0xFFBA68C8),
                                Color(0xFFFFCC80)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(145.dp),
                    model = ImageRequest.Builder(applicationContext)
                        .data(url)
                        .crossfade(enable = true)
                        .build(),
                    contentDescription = "Image",
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier.padding(4.dp),

        ) {
            Box(modifier = Modifier.height(8.dp))
            Text(
                text = "Félicitation ! ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Box(modifier = Modifier.height(8.dp))
            Text(text = "Vous avez terminé votre tache et avez gagné un nouveau personnage !")
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = .08f),
                        shape = RoundedCornerShape(10.dp)
                    )
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDismissRequest() }
                    .weight(1f)
                    .padding(vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Ok", fontWeight = FontWeight.Bold, color = Color(0xFFFF332C))
            }
        }
    }
}

@ReadOnlyComposable
@Composable
fun fenetreDialog(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window
