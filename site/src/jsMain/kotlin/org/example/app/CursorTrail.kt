package org.example.app

import androidx.compose.runtime.*
import com.varabyte.kobweb.navigation.BasePath
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.random.Random

private const val TOAD_TRAIL_LENGTH = 6      // how many fading toads follow the mouse
private const val TICK_MS = 40L              // how often the trail moves along
private const val SPARKLE_LIFE_MS = 700.0    // how long a sparkle lives
private const val CURSOR_SIZE = 20           // same size as tode-cursor.png

private val sparkleSymbols = listOf("✦", "✧", "⋆", "★")
private val sparkleColors = listOf("#5b21b6", "#7c3aed", "#c084fc", "#16a34a", "#86efac")

// One sparkle: where it was born, which way it drifts, and when it was born
private class Sparkle(
    val id: Int,
    val x: Double,
    val y: Double,
    val driftX: Double,
    val bornAt: Double,
    val symbol: String,
    val color: String,
)

// Draws a trail of fading toads plus falling sparkles behind the mouse pointer
@Composable
fun CursorTrail() {
    // Start off-screen so nothing shows before the mouse moves
    var mouseX by remember { mutableStateOf(-100.0) }
    var mouseY by remember { mutableStateOf(-100.0) }
    var toads by remember { mutableStateOf(List(TOAD_TRAIL_LENGTH) { -100.0 to -100.0 }) }
    var sparkles by remember { mutableStateOf(listOf<Sparkle>()) }
    var now by remember { mutableStateOf(0.0) }
    var nextId by remember { mutableStateOf(0) }

    // Listen to mouse movement on the whole page
    DisposableEffect(Unit) {
        val onMove: (Event) -> Unit = { event ->
            val e = event as MouseEvent
            mouseX = e.clientX.toDouble()
            mouseY = e.clientY.toDouble()
            // Only spawn a sparkle on some moves, so it doesn't get too busy
            if (Random.nextDouble() < 0.4) {
                sparkles = sparkles + Sparkle(
                    id = nextId++,
                    x = mouseX + CURSOR_SIZE / 2 + Random.nextDouble(-8.0, 8.0),
                    y = mouseY + CURSOR_SIZE / 2 + Random.nextDouble(-8.0, 8.0),
                    driftX = Random.nextDouble(-15.0, 15.0),
                    bornAt = window.performance.now(),
                    symbol = sparkleSymbols.random(),
                    color = sparkleColors.random(),
                )
            }
        }
        window.addEventListener("mousemove", onMove)
        onDispose { window.removeEventListener("mousemove", onMove) }
    }

    // A "clock" that moves the trail along: each toad takes the place of the one in front of it
    LaunchedEffect(Unit) {
        while (true) {
            delay(TICK_MS)
            toads = listOf(mouseX to mouseY) + toads.dropLast(1)
            if (sparkles.isNotEmpty()) {
                now = window.performance.now()
                sparkles = sparkles.filter { now - it.bornAt < SPARKLE_LIFE_MS }
            }
        }
    }

    // pointer-events: none means the trail never blocks clicks
    Div({
        style {
            property("position", "fixed")
            property("inset", "0")
            property("pointer-events", "none")
            property("z-index", "9999")
            property("overflow", "hidden")
        }
    }) {
        // Toad trail (skip index 0, that's where the real cursor is)
        toads.forEachIndexed { i, (x, y) ->
            if (i == 0 || (x == mouseX && y == mouseY)) return@forEachIndexed
            Img(src = BasePath.prependTo("/tode-cursor.png"), attrs = {
                style {
                    property("position", "absolute")
                    property("left", "${x}px")
                    property("top", "${y}px")
                    property("width", "${CURSOR_SIZE}px")
                    property("opacity", "${0.6 * (1 - i.toDouble() / TOAD_TRAIL_LENGTH)}")
                }
            })
        }

        // Sparkles: they fall, drift sideways, shrink and fade as they get older
        sparkles.forEach { s ->
            key(s.id) {
                val progress = ((now - s.bornAt) / SPARKLE_LIFE_MS).coerceIn(0.0, 1.0)
                Span({
                    style {
                        property("position", "absolute")
                        property("left", "${s.x + s.driftX * progress}px")
                        property("top", "${s.y + 25 * progress}px")
                        property("color", s.color)
                        property("font-size", "${14 * (1 - progress * 0.6)}px")
                        property("opacity", "${1 - progress}")
                        property("transform", "translate(-50%, -50%)")
                    }
                }) { Text(s.symbol) }
            }
        }
    }
}
