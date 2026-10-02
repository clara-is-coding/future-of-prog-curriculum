package org.example.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.navigation.BasePath
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.HTMLLinkElement

@InitSilk
fun initColorMode(ctx: InitSilkContext) {
    ctx.config.initialColorMode = ColorMode.LIGHT
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    // Inject the font stylesheet into <head> at runtime
    DisposableEffect(Unit) {
        val link = document.createElement("link") as HTMLLinkElement
        link.rel = "stylesheet"
        link.href = BasePath.prependTo("/fonts.css")
        document.head?.appendChild(link)
        onDispose { document.head?.removeChild(link) }
    }

    SilkApp {
        Surface(Modifier.minHeight(100.vh)) {
            content()
        }
        CursorTrail()
    }
}
