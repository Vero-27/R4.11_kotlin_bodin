package com.example.to_do_list.android.view

import android.annotation.SuppressLint
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import com.example.to_do_list.android.view.EtatConfetti.Companion.tailleChangee
import kotlinx.coroutines.isActive
import kotlin.math.roundToInt

@SuppressLint("ModifierFactoryUnreferencedReceiver")
fun Modifier.confetti(
    contentColors: List<Color>,
    confettiShape: FormeConfetti,
    vitesse: Float,
    quantite: Float,
    estVisible: Boolean
) = composed {
    var etatConfetti by remember {
        mutableStateOf(
            EtatConfetti(
                confetti = emptyList(),
                vitesse = vitesse,
                couleurs = contentColors,
                formes = confettiShape
            )
        )
    }

    var derniereImage by remember { mutableStateOf(-1L) }

    LaunchedEffect(estVisible) {
        while (estVisible && isActive) {
            withFrameMillis { nouveau ->
                val tempsMillis = nouveau - derniereImage
                val premiereImage = derniereImage < 0
                derniereImage = nouveau
                if (premiereImage) return@withFrameMillis

                for (confetto in etatConfetti.confetti) {
                    etatConfetti.suivant(tempsMillis)
                }
            }
        }
    }

    onSizeChanged {
        etatConfetti = etatConfetti.tailleChangee(
            taille = it,
            quantite = quantite
        )
    }.drawBehind {
        if (estVisible) {
            for (confetto in etatConfetti.confetti) {
                confetto.dessiner(drawContext.canvas)
            }
        }
    }
}

enum class FormeConfetti {
    Mix,
    Rectangle,
    Cercle
}

data class EtatConfetti(
    val confetti: List<Confetto> = emptyList(),
    val couleurs: List<Color>,
    val formes: FormeConfetti,
    val taille: IntSize = IntSize.Zero,
    val vitesse: Float
) {

    fun suivant(tempsMillis: Long) {
        confetti.forEach {
            it.suivant(taille, tempsMillis, vitesse)
        }
    }

    companion object {
        fun EtatConfetti.tailleChangee(
            taille: IntSize,
            quantite: Float
        ): EtatConfetti {
            if (taille == this.taille) return this
            return copy(
                confetti = (0..taille.quantiteReelle(quantite)).map {
                    Confetto.creer(taille, couleurs, formes)
                },
                taille = taille
            )
        }

        private fun IntSize.quantiteReelle(quantite: Float): Int {
            return (width * height / 10_000 * quantite).roundToInt()
        }
    }
}

class Confetto(
    vecteur: Offset,
    private val couleur: Color,
    private val radius: Float,
    private val forme: FormeConfetti = FormeConfetti.Cercle,
    position: Offset
) {
    internal var position by mutableStateOf(position)
    private var vecteur by mutableStateOf(vecteur)
    private val paint: Paint = Paint().apply {
        isAntiAlias = true
        color = couleur
        style = PaintingStyle.Fill
    }

    fun suivant(
        bords: IntSize,
        tempsMillis: Long,
        vitesse: Float
    ) {
        val vitesse = vecteur * vitesse
        val borderTop = 0
        val borderLeft = 0
        val borderBottom = bords.height
        val borderRight = bords.width

        position = Offset(
            x = position.x + (vitesse.x / 1000f * tempsMillis),
            y = position.y + (vitesse.y / 1000f * tempsMillis),
        )
        val vx = if (position.x < borderLeft || position.x > borderRight) -vecteur.x else vecteur.x
        val vy = if (position.y < borderTop || position.y > borderBottom) -vecteur.y else vecteur.y

        if (vx != vecteur.x || vy != vecteur.y) {
            vecteur = Offset(vx, vy)
        }
    }

    fun dessiner(canvas: Canvas): Unit {

        when (forme) {
            FormeConfetti.Cercle -> {
                canvas.drawCircle(
                    radius = radius,
                    center = position,
                    paint = paint
                )
            }

            FormeConfetti.Rectangle -> {
                val rectangle = Rect(position.x, position.y, position.x + radius, position.y + radius)
                canvas.drawRect(
                    rect = rectangle,
                    paint = paint
                )
            }

            else -> {}
        }

    }

    companion object {

        fun creer(bords: IntSize, couleurs: List<Color>, formes: FormeConfetti): Confetto {
            val forme = if (formes == FormeConfetti.Mix) {
                if ((0..1).random() == 0) FormeConfetti.Cercle else FormeConfetti.Rectangle
            } else formes
            return Confetto(
                position = Offset(
                    (0..bords.width).random().toFloat(),
                    (0..bords.height).random().toFloat()
                ),
                vecteur = Offset(
                    // First, randomize direction. Second, randomize amplitude of speed vector.
                    listOf(
                        -1f,
                        1f
                    ).random() * ((bords.width.toFloat() / 100f).toInt()..(bords.width.toFloat() / 10f).toInt()).random()
                        .toFloat(),
                    listOf(
                        -1f,
                        1f
                    ).random() * ((bords.height.toFloat() / 100f).toInt()..(bords.height.toFloat() / 10f).toInt()).random()
                        .toFloat()
                ),
                couleur = couleurs.random(),
                radius = (5..25).random().toFloat(),
                forme = forme
            )
        }
    }
}
