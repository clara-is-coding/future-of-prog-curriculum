package org.example.app.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.AlignItems
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.silk.style.toModifier
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.example.app.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Page
@Composable
fun HomePage() {
    LaunchedEffect(Unit) {
        document.title = "How to prepare for the future of programming"
    }

    ScrollProgressBar()

    // ---- PAGE WALLPAPER ----
    // One green background with the faint toad + Academy logo wallpaper behind the whole page,
    // so the pattern runs down the page without breaks between sections.
    // (the negative margin cancels the body's side padding)
    Div(
        Modifier
            .backgroundColor(SiteColors.LightGreen)
            .toAttrs {
                style {
                    property("margin", "0 -1rem")
                    // A see-through layer of green sits on top of the wallpaper, so it looks faint.
                    // Raise 0.85 to make it fainter, lower it to make it stronger.
                    val tint = "rgba(238, 247, 238, 0.85)"
                    property("background-image", "linear-gradient($tint, $tint), url(\"/toad-academy-tile.png\")")
                    property("background-size", "auto, 128px")
                    property("background-repeat", "no-repeat, repeat")
                }
            }
    ) {

    // ---- HEADER ----
    Div(
        Modifier
            .padding(top = 4.cssRem, bottom = 3.cssRem, leftRight = 1.cssRem)
            .toAttrs()
    ) {
    Div(
        Modifier
            .maxWidth(720.px)
            .toAttrs {
                style {
                    property("margin", "0 auto")
                    property("text-align", "center")
                    // Soft off-white highlight behind the title so the toads don't clash with the text
                    property("padding", "1.5rem 1rem")
                    property("border-radius", "24px")
                    property("background-color", "rgba(250, 249, 246, 0.9)")
                    property("box-shadow", "0 0 24px 16px rgba(250, 249, 246, 0.9)")
                }
            }
    ) {
        H1(
            Modifier
                .lineHeight(1.2)
                .margin(bottom = 1.cssRem)
                .toAttrs {
                    style {
                        property("font-size", "clamp(1.75rem, 5vw, 3rem)")
                        property("font-weight", "700")
                    }
                }
        ) { Text("How to prepare for the future of programming") }

        P(
            Modifier
                .fontFamily("Rubik Mono One", "monospace")
                .color(SiteColors.DarkPurple)
                .margin(bottom = 0.75.cssRem)
                .toAttrs {
                    style {
                        property("font-size", "clamp(1.5rem, 4vw, 2.5rem)")
                        property("font-weight", "400")
                        property("letter-spacing", "0.1em")
                    }
                }
        ) { Text("DON'T PANIC") }

        P(
            Modifier
                .fontSize(1.cssRem)
                .color(SiteColors.TextMuted)
                .margin(0.px)
                .toAttrs {
                    style { property("font-style", "italic") }
                }
        ) { Text("a mini-curriculum by Clara from "); A(href = "https://www.youtube.com/@JetBrainsAcademy") { Text("JetBrains Academy") } }
    }
    }

    // ---- MAIN CONTENT ----
    Div {
        UnderstandSection()
        DeepenSection()
        BroadenSection()
        FaqSection()
    }
    }
}


// ========== SECTION 1: UNDERSTAND ==========

@Composable
private fun UnderstandSection() {
    CurriculumSection {
        SectionTitle("📝 Understand the flaws of your education")
        P(
            Modifier
                .color(SiteColors.TextMuted)
                .lineHeight(1.7)
                .margin(bottom = 2.cssRem)
                .toAttrs()
        ) {
            Text("Reflect on your computing education from a zoomed-out perspective. Think about the people, the program, what it did or is doing well and what gaps you might be left with when you're done.")
        }
        TimerWidget()
        TextArea(
            attrs = ReflectionInputStyle.toModifier().toAttrs {
                attr("placeholder", "Pretend you are a toad being dropped out of an airplane and the earth is your educational journey. What do you see?")
                attr("rows", "10")
            }
        )
    }
}

@Composable
private fun TimerWidget() {
    var selectedMinutes by remember { mutableStateOf<Int?>(null) }
    var totalSeconds by remember { mutableStateOf(0) }
    var remainingSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isDone by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--
                if (remainingSeconds <= 0) {
                    isRunning = false
                    isDone = true
                }
            }
        }
    }

    fun selectDuration(minutes: Int) {
        isRunning = false
        isDone = false
        selectedMinutes = minutes
        totalSeconds = minutes * 60
        remainingSeconds = minutes * 60
    }

    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    }

    val displayText = if (selectedMinutes == null) "--:--" else formatTime(remainingSeconds)
    val displayColor = when {
        isDone -> SiteColors.Success
        isRunning -> SiteColors.Accent
        else -> SiteColors.Text
    }
    val toggleLabel = when {
        isDone -> "Done!"
        isRunning -> "Pause"
        selectedMinutes == null || remainingSeconds == totalSeconds -> "Start"
        else -> "Resume"
    }

    Div(
        Modifier
            .backgroundColor(SiteColors.AccentLight)
            .border(1.px, LineStyle.Solid, SiteColors.AccentBorder)
            .borderRadius(12.px)
            .padding(1.5.cssRem)
            .margin(bottom = 1.5.cssRem)
            .toAttrs()
    ) {
        P(
            Modifier
                .margin(bottom = 1.cssRem)
                .fontSize(0.95.cssRem)
                .toAttrs {
                    style { property("font-weight", "500") }
                }
        ) { Text("Set a timer for your reflection:") }

        // Duration buttons
        Div(
            Modifier
                .display(DisplayStyle.Flex)
                .gap(0.5.cssRem)
                .margin(bottom = 1.25.cssRem)
                .toAttrs()
        ) {
            listOf(10, 20, 30).forEach { minutes ->
                val isSelected = selectedMinutes == minutes
                Button(
                    attrs = TimerOptionStyle.toModifier()
                        .then(
                            if (isSelected)
                                Modifier.backgroundColor(SiteColors.Accent).color(Colors.White)
                            else Modifier
                        )
                        .toAttrs { onClick { selectDuration(minutes) } }
                ) { Text("$minutes min") }
            }
        }

        // Clock display
        Div(
            Modifier
                .fontFamily("JetBrains Mono", "monospace")
                .fontSize(3.5.cssRem)
                .lineHeight(1)
                .color(displayColor)
                .margin(bottom = 1.cssRem)
                .toAttrs {
                    style {
                        property("font-weight", "200")
                        property("letter-spacing", "0.05em")
                        property("transition", "color 0.3s")
                    }
                }
        ) { Text(displayText) }

        // Controls
        Div(Modifier.display(DisplayStyle.Flex).gap(0.75.cssRem).toAttrs()) {
            Button(
                attrs = TimerBtnStyle.toModifier().toAttrs {
                    if (selectedMinutes == null || isDone) attr("disabled", "")
                    onClick { if (!isDone) isRunning = !isRunning }
                }
            ) { Text(toggleLabel) }

            Button(
                attrs = TimerBtnStyle.toModifier().toAttrs {
                    if (selectedMinutes == null) attr("disabled", "")
                    onClick {
                        isRunning = false
                        isDone = false
                        remainingSeconds = totalSeconds
                    }
                }
            ) { Text("Reset") }
        }
    }
}


// ========== SECTION 2: DEEPEN ==========

@Composable
private fun DeepenSection() {
    CurriculumSection {
        SectionTitle("🤿 Deepen your knowledge")

        Subsection("Find a mentor") {
            TodoItem(label = { Text("Find someone who is where you want to be who is willing to talk to you") })
            TodoItem(label = { Text("Ask them how to get there") })
            BlockQuote {
                Text("Ideally someone who can personally mentor and motivate you. One of the scariest parts of being a new programmer in today's landscape is how distant we feel from the wisdom of people actually working in the field. How am I supposed to know how to be useful when I barely understand the landscape? Trust me, older developers love to complain about what they think is useless, just make sure they do it while you're in the room.")
            }
        }

        Subsection("Commit to a course") {
            TodoItem(label = { Text("Choose a course and seriously commit to it") })
            BlockQuote {
                Text("Don't worry too much about choosing the perfect specialization. The point here is just going with something and letting it take you somewhere. ")
                A(href = "https://academy.jetbrains.com/course/16630") { Text("This could be anything.") }
                Text(" ")
                A(href = "https://www.deeplearning.ai/courses/spec-driven-development-with-coding-agents") { Text("Anything at all.") }
                Text(" I mean we do have ")
                A(href = "https://academy.jetbrains.com/") { Text("specifically tailored specializations") }
                Text(". If not… that's fine. I won't be jealous. Probably.")
            }
        }

        Subsection("Curate your motivation") {
            HandwrittenNote("easy & fast ↓")
            TodoItem(label = { Text("Find inspiring podcasts & media that remind you why you love this subject") })
            TextArea(
                attrs = ReflectionInputStyle.toModifier().toAttrs {
                    attr("placeholder", "What inspires you to keep learning?")
                    attr("rows", "2")
                }
            )
            TodoItem(
                label = {
                    Text("ACTUALLY listen/watch/read them")
                },
                description = {
                    Text("Clara's computing inspo starter pack <3:")
                    Br()
                    A(href = "https://www.youtube.com/watch?v=dQw4w9WgXcQ") { Text("🎧 python 4 u by charlixcx") }
                    Br()
                    A(href = "https://www.youtube.com/watch?v=jYUZAF3ePFE") { Text("🎥 Linus torvalds basement reveal") }
                    Br()
                    A(href = "https://youtu.be/iaT_zvNN_zc?si=8IstuZAg2c_A9k3c") {Text("🎧 Love, Learning, and CS Education keynote by Amy Ko") }
                    Br()
                    A(href = "https://www.oreilly.com/content/a-short-history-of-the-oreilly-animals/") { Text("📕 One of those books with the birds on the cover") }
                }
            )

            HandwrittenNote("hard & lasting ↓")
            TodoItem(label = { Text("Regular meditation practice") })
            TodoItem(label = { Text("Find other people to study with") })
        }
    }
}


// ========== SECTION 3: BROADEN ==========

@Composable
private fun BroadenSection() {
    CurriculumSection {
        SectionTitle("🌳 Broaden your knowledge")

        TodoItem(
            label = {
                Text("Bonus: fall down a good old wikipedia rabbit hole")
            },
            description = {
                Text("Ever heard of an ")
                A(href = "https://en.wikipedia.org/wiki/Experience_machine") { Text("experience machine?") }
            }
        )

        TodoItem(
            label = { Text("Follow short courses in other domains") },
            description = {
                Text("Our computer science knowledge map can help you visualize what subjects you might find interesting to explore, or you can even consider departing from computing altogether. Bringing technical expertise to a writing course might lead to great science fiction, or ")
                A(href = "https://www.youtube.com/@JetBrainsAcademy") { Text("mid-tier youtube videos") }
                Text(". Or you could check out, like Docker or something idk.")
            }
        )

        TodoItem(
            label = { Text("Skill swap project") },
            description = {
                Text("Find someone with a different skillset and swap roles. Put the designer in charge of databases, backend in charge of frontend. Help each other understand your niche. Try out being bad at something for the sake of learning. It might be ugly and inefficient but that's not a crime!")
            }
        )

        TodoItem(
            label = { Text("Upgrade the \"person\" in \"technical person\"") },
            description = {
                Text("Prioritize building your communication skills, critical thinking, and creative problem solving. Take an improv class! Journal! Notice little moments for caring for the humans around you! Although this might be a less defined task than \"optimize this algorithm\", it doesn't mean progress is invisible.")
            }
        )
    }
}


// ========== FAQs ==========

@Composable
private fun FaqSection() {
    CurriculumSection {
        SectionTitle("FAQs")
        FaqItem(
            question = "Why does this website exist?",
            answer = "Taking my own advice, I took a little break from my usual video production duties to brush up on my very rusty coding skills. JetBrains IDEs are free for educational purposes, and I used WebStorm to code up this interactive curriculum for all the homework I presented in this video. Do we have designers that could have done this for me? Yes. Is it a good website? No. But the point is, now I know what Kobweb is, and I feel pretty proud of myself."
        )
        FaqItem(
                    question = "Why did you use WebStorm for a Kotlin project?",
                    answer = "Because I am a fool."
        )
        FaqItem(
            question = "Does the toad have a parachute?",
            answer = "Yes of course. The toad is really amped to be skydiving and reflecting on its educational journey. By the time the toad reached the ground it decided that it would benefit from a more social environment instead of just coding alone all day. The toad plans to start a coding club at its local pond where it just might meet a new best friend, fall in love, finish its passion project, and get everything it ever wanted in life."
        )
    }
}


// ========== REUSABLE COMPONENTS ==========

@Composable
private fun SectionTitle(text: String) {
    H2(
        Modifier
            .fontSize(1.75.cssRem)
            .margin(bottom = 0.75.cssRem)
            .toAttrs {
                style { property("font-weight", "600") }
            }
    ) { Text(text) }
}

@Composable
private fun CurriculumSection(content: @Composable () -> Unit) {
    // Section band (see-through, so the page wallpaper shows behind the card)
    Div(
        Modifier
            .padding(topBottom = 3.cssRem, leftRight = 1.cssRem)
            .toAttrs {
                // Tells the scroll progress bar which colour this section is
                attr("data-band", "green")
            }
    ) {
        // Purple card
        Div(
            Modifier
                .maxWidth(720.px)
                .backgroundColor(SiteColors.LightPurple)
                .borderRadius(16.px)
                .padding(2.5.cssRem)
                .toAttrs {
                    style { property("margin", "0 auto") }
                }
        ) { content() }
    }
}

@Composable
private fun Subsection(title: String, content: @Composable () -> Unit) {
    Div(Modifier.margin(bottom = 2.5.cssRem).toAttrs()) {
        Div(
            Modifier
                .display(DisplayStyle.Flex)
                .alignItems(AlignItems.Center)
                .gap(0.6.cssRem)
                .fontSize(1.1.cssRem)
                .margin(bottom = 1.cssRem)
                .toAttrs {
                    style { property("font-weight", "600") }
                }
        ) {
            // Decorative dot
            Div(
                Modifier
                    .width(8.px).height(8.px)
                    .minWidth(8.px)
                    .backgroundColor(SiteColors.Accent)
                    .borderRadius(50.percent)
                    .toAttrs()
            )
            Text(title)
        }
        content()
    }
}

@Composable
private fun TodoItem(
    label: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null
) {
    var checked by remember { mutableStateOf(false) }

    Div(Modifier.margin(bottom = 0.5.cssRem).toAttrs()) {
        Div(
            TodoRowStyle.toModifier().toAttrs {
                onClick { event ->
                    checked = !checked
                    if (checked) launchConfetti(event.clientX.toDouble(), event.clientY.toDouble())
                }
            }
        ) {
            // Custom checkbox
            Div(
                Modifier
                    .minWidth(20.px).width(20.px).height(20.px)
                    .border(
                        2.px, LineStyle.Solid,
                        if (checked) SiteColors.Success else SiteColors.Border
                    )
                    .borderRadius(4.px)
                    .backgroundColor(if (checked) SiteColors.Success else Color.rgb(0xffffff))
                    .display(DisplayStyle.Flex)
                    .alignItems(AlignItems.Center)
                    .justifyContent(JustifyContent.Center)
                    .toAttrs {
                        style {
                            property("flex-shrink", "0")
                            property("margin-top", "2px")
                        }
                    }
            ) {
                if (checked) {
                    Span(
                        Modifier
                            .color(Colors.White)
                            .fontSize(12.px)
                            .lineHeight(1)
                            .toAttrs()
                    ) { Text("✓") }
                }
            }

            // Label text (with strikethrough when done)
            Span(
                Modifier
                    .fontSize(1.cssRem)
                    .lineHeight(1.5)
                    .then(
                        if (checked) Modifier.color(SiteColors.TextMuted)
                        else Modifier
                    )
                    .toAttrs {
                        if (checked) style { property("text-decoration", "line-through") }
                    }
            ) { label() }
        }

        if (description != null) {
            Div(
                Modifier
                    .fontSize(0.9.cssRem)
                    .color(SiteColors.TextMuted)
                    .lineHeight(1.65)
                    .padding(left = 0.75.cssRem)
                    .borderLeft(2.px, LineStyle.Solid, SiteColors.Border)
                    .toAttrs {
                        style { property("margin", "0.1rem 0 0.75rem 2.5rem") }
                    }
            ) { description() }
        }
    }
}

@Composable
private fun BlockQuote(content: @Composable () -> Unit) {
    Div(
        Modifier
            .padding(topBottom = 0.75.cssRem, leftRight = 1.25.cssRem)
            .borderLeft(3.px, LineStyle.Solid, SiteColors.QuoteBorder)
            .color(SiteColors.TextMuted)
            .lineHeight(1.7)
            .fontSize(0.95.cssRem)
            .toAttrs {
                style {
                    property("margin", "1.25rem 0 0")
                    property("font-style", "italic")
                }
            }
    ) { content() }
}

@Composable
private fun HandwrittenNote(text: String) {
    Span(
        Modifier
            .fontFamily("cursive")
            .fontSize(1.cssRem)
            .color(SiteColors.HandwrittenPurple)
            .display(DisplayStyle.InlineBlock)
            .toAttrs {
                style { property("margin", "1rem 0 0.25rem 0.5rem") }
            }
    ) { Text(text) }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var isOpen by remember { mutableStateOf(false) }

    Div(
        Modifier
            .border(1.px, LineStyle.Solid, SiteColors.Border)
            .borderRadius(10.px)
            .overflow(Overflow.Hidden)
            .toAttrs()
    ) {
        Div(
            Modifier
                .display(DisplayStyle.Flex)
                .justifyContent(JustifyContent.SpaceBetween)
                .alignItems(AlignItems.Center)
                .padding(topBottom = 1.cssRem, leftRight = 1.25.cssRem)
                .cursor(Cursor.Pointer)
                .fontSize(1.cssRem)
                .toAttrs {
                    style {
                        property("font-weight", "500")
                        property("user-select", "none")
                    }
                    onClick { isOpen = !isOpen }
                }
        ) {
            Text(question)
            Span(
                Modifier
                    .fontSize(1.25.cssRem)
                    .color(SiteColors.TextMuted)
                    .toAttrs {
                        style {
                            property("transition", "transform 0.2s")
                            if (isOpen) property("transform", "rotate(45deg)")
                        }
                    }
            ) { Text("+") }
        }
        if (isOpen) {
            P(
                Modifier
                    .margin(0.px)
                    .padding(topBottom = 1.cssRem, leftRight = 1.25.cssRem)
                    .color(SiteColors.TextMuted)
                    .lineHeight(1.7)
                    .borderTop(1.px, LineStyle.Solid, SiteColors.Border)
                    .toAttrs {
                        style { property("padding-bottom", "1.25rem") }
                    }
            ) { Text(answer) }
        }
    }
}
