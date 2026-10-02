package org.example.app

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.dom.Div
import org.w3c.dom.Element
import org.w3c.dom.asList
import org.w3c.dom.events.Event

private const val BAR_HEIGHT = 6 // px

// A bar across the very top of the screen that fills up as you scroll down the page.
// It's green over green sections and purple over purple ones (sections mark themselves with data-band="green"/"purple").
@Composable
fun ScrollProgressBar() {
    var progress by remember { mutableStateOf(0.0) } // 0.0 = top of the page, 1.0 = bottom
    var green by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val update: (Event?) -> Unit = {
            val scrollable = document.documentElement!!.scrollHeight - window.innerHeight
            progress = if (scrollable > 0) (window.scrollY / scrollable).coerceIn(0.0, 1.0) else 0.0

            // The section under the bar is the last one whose top edge has scrolled past the bar
            val current = document.querySelectorAll("[data-band]").asList()
                .map { it as Element }
                .lastOrNull { it.getBoundingClientRect().top <= BAR_HEIGHT }
            green = current?.getAttribute("data-band") == "green"
        }
        update(null)
        window.addEventListener("scroll", update)
        window.addEventListener("resize", update)
        onDispose {
            window.removeEventListener("scroll", update)
            window.removeEventListener("resize", update)
        }
    }

    Div({
        style {
            property("position", "fixed")
            property("top", "0")
            property("left", "0")
            property("height", "${BAR_HEIGHT}px")
            property("width", "${progress * 100}%")
            property("background-color", if (green) SiteColors.DarkGreen.toString() else SiteColors.DarkPurple.toString())
            property("transition", "background-color 0.3s")
            property("z-index", "9998") // just below the cursor trail
            property("pointer-events", "none")
        }
    })
}
