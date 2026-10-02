package org.example.app

import kotlinx.browser.window

// Tells Kotlin that the JavaScript library "canvas-confetti" exists
@JsModule("canvas-confetti")
@JsNonModule
private external val confettiModule: dynamic

// Fires a burst of confetti from a point on the screen (in pixels)
fun launchConfetti(x: Double, y: Double) {
    val fire = confettiModule.default ?: confettiModule

    val origin: dynamic = js("({})")
    origin.x = x / window.innerWidth
    origin.y = y / window.innerHeight

    val options: dynamic = js("({})")
    options.particleCount = 80
    options.spread = 70
    options.startVelocity = 30
    options.origin = origin
    options.colors = arrayOf("#5b21b6", "#7c3aed", "#c084fc", "#16a34a", "#86efac")

    fire(options)
}
