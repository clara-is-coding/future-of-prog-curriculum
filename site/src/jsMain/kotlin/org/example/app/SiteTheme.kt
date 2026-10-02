package org.example.app

import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.palette.background
import com.varabyte.kobweb.silk.theme.colors.palette.color

object SiteColors {
    val Bg = Color.rgb(0xfaf9f6)
    val Text = Color.rgb(0x1f1f2e)
    val TextMuted = Color.rgb(0x6b7280)
    val Success = Color.rgb(0x16a34a)
    val Border = Color.rgb(0xe5e7eb)
    val QuoteBorder = Color.rgb(0xc084fc)
    val HandwrittenPurple = Color.rgb(0x7c3aed)
    val LightPurple = Color.rgb(0xf3eefb)
    val LightGreen = Color.rgb(0xeef7ee)
    val DarkPurple = Color.rgb(0x5b21b6)
    val DarkGreen = Color.rgb(0x15803d)
    val Accent = DarkPurple
    val AccentLight = Color.rgb(0xede9fe)
    val AccentBorder = Color.rgb(0xddd6fe)
}

@InitSilk
fun initTheme(ctx: InitSilkContext) {
    // Lock both palettes to our light theme so the site never goes dark
    ctx.theme.palettes.light.background = SiteColors.Bg
    ctx.theme.palettes.light.color = SiteColors.Text
    ctx.theme.palettes.dark.background = SiteColors.Bg
    ctx.theme.palettes.dark.color = SiteColors.Text
}