package org.example.app

import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import org.jetbrains.compose.web.css.*

@InitSilk
fun initSiteStyles(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("*, *::before, *::after") {
        Modifier.boxSizing(BoxSizing.BorderBox)
    }
    ctx.stylesheet.registerStyleBase("body") {
        Modifier
            .margin(0.px)
            .padding(leftRight = 1.cssRem)
            .fontFamily("JetBrains Sans", "Inter", "system-ui", "Helvetica", "Arial", "sans-serif")
            .color(SiteColors.Text)
            .backgroundColor(SiteColors.Bg)
    }
    ctx.stylesheet.registerStyleBase("a") {
        Modifier.color(SiteColors.Accent)
    }
    // Toad mouse pointer ("0 0" = the click point is the image's top-left corner)
    ctx.stylesheet.registerStyleBase("html") {
        Modifier.styleModifier { property("cursor", "url(\"/tode-cursor.png\") 0 0, auto") }
    }
    // Toad instead of the hand pointer on links and buttons too (disabled buttons keep the "not allowed" cursor)
    ctx.stylesheet.registerStyleBase("a, button:not(:disabled)") {
        Modifier.styleModifier { property("cursor", "url(\"/tode-cursor.png\") 0 0, auto") }
    }
    // Winking toad while the mouse button is held down
    ctx.stylesheet.registerStyleBase("html:active, html:active *") {
        Modifier.styleModifier { property("cursor", "url(\"/tode-winking-cursor.png\") 0 0, auto") }
    }
}

// Timer duration buttons (10 / 20 / 30 min)
val TimerOptionStyle = CssStyle {
    base {
        Modifier
            .padding(topBottom = 0.4.cssRem, leftRight = 1.1.cssRem)
            .border(1.px, LineStyle.Solid, SiteColors.Accent)
            .borderRadius(20.px)
            .backgroundColor(Colors.White)
            .color(SiteColors.Accent)
            .fontFamily("JetBrains Mono", "monospace")
            .fontSize(0.9.cssRem)
    }
    cssRule(":hover") {
        Modifier.backgroundColor(SiteColors.Accent).color(Colors.White)
    }
}

// Start / Pause / Reset buttons
val TimerBtnStyle = CssStyle {
    base {
        Modifier
            .padding(topBottom = 0.5.cssRem, leftRight = 1.5.cssRem)
            .borderRadius(8.px)
            .border(1.px, LineStyle.Solid, SiteColors.Border)
            .backgroundColor(Colors.White)
            .color(SiteColors.Text)
            .fontFamily("inherit")
            .fontSize(0.95.cssRem)
    }
    cssRule(":hover:not(:disabled)") {
        Modifier
            .color(SiteColors.Accent)
            .styleModifier { property("border-color", SiteColors.Accent) }
    }
    cssRule(":disabled") {
        Modifier.opacity(0.4).cursor(Cursor.NotAllowed)
    }
}

// Reflection textarea
val ReflectionInputStyle = CssStyle {
    base {
        Modifier
            .width(100.percent)
            .padding(topBottom = 1.cssRem, leftRight = 1.25.cssRem)
            .border(1.px, LineStyle.Solid, SiteColors.Border)
            .borderRadius(10.px)
            .fontFamily("inherit")
            .fontSize(1.cssRem)
            .lineHeight(1.7)
            .color(SiteColors.Text)
            .backgroundColor(Colors.White)
            .resize(Resize.Vertical)
    }
    cssRule(":focus") {
        Modifier.styleModifier {
            property("border-color", "#2563eb")
            property("outline", "none")
            property("box-shadow", "0 0 0 3px rgba(37, 99, 235, 0.1)")
        }
    }
    cssRule("::placeholder") {
        Modifier.color(SiteColors.TextMuted)
    }
}

// Todo item row (needs hover background)
val TodoRowStyle = CssStyle {
    base {
        Modifier
            .display(DisplayStyle.Flex)
            .alignItems(AlignItems.FlexStart)
            .gap(0.75.cssRem)
            .padding(topBottom = 0.6.cssRem, leftRight = 0.75.cssRem)
            .borderRadius(8.px)
    }
    cssRule(":hover") {
        Modifier.styleModifier { property("background-color", "rgba(37, 99, 235, 0.05)") }
    }
}
