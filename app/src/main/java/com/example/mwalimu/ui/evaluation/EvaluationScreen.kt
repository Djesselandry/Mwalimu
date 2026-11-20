package com.example.mwalimu.ui.evaluation

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mwalimu.MainActivity
import com.example.mwalimu.ui.components.HomeIcon
import com.example.mwalimu.ui.components.HomeIconView

private data class QuizItem(
    val id: Int,
    val subject: String,
    val title: String,
    val questions: Int,
    val duration: String,
    val difficulty: String,
    val status: String,
    val score: Int?
)

private val quizItems = listOf(
    QuizItem(1, "Mathématiques", "Le théorème de Pythagore", 10, "12 min", "Intermédiaire", "En cours", 70),
    QuizItem(2, "Français", "Les figures de style", 15, "18 min", "Facile", "Terminé", 88),
    QuizItem(3, "Sciences", "Forces et mouvement", 12, "15 min", "Intermédiaire", "Non commencé", null),
    QuizItem(4, "Biologie", "La cellule vivante", 20, "22 min", "Avancé", "Terminé", 76)
)

internal fun MainActivity.evaluationScreen(): View {
    var filter = "Tous"
    var query = ""
    val favorites = mutableSetOf(1, 4)
    lateinit var search: EditText
    val root = setupRoot()
    val header = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(16), dp(8), dp(16), dp(5))
    }
    val titleBlock = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    titleBlock.addView(TextView(this).apply {
        text = "Évaluation"; textSize = 19f; setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD); includeFontPadding = false
    })
    titleBlock.addView(TextView(this).apply {
        text = "Testez vos connaissances, sans pression."; textSize = 8f
        setTextColor(muted); includeFontPadding = false
    }, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(4) })
    header.addView(titleBlock, LinearLayout.LayoutParams(0, dp(48), 1f).apply { gravity = Gravity.CENTER_VERTICAL })
    header.addView(evalIconButton(HomeIcon.SETTINGS, "Paramètres") {
        openSettings()
    })
    root.addView(header)

    val scroll = ScrollView(this).apply { isVerticalScrollBarEnabled = false; clipToPadding = false }
    val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(8), dp(16), dp(14)) }
    content.addView(featuredQuizCard { startQuiz(quizItems.first()) })

    val searchBox = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(11), 0, dp(10), 0); background = roundedBackground(Color.WHITE, border, 15)
    }
    searchBox.addView(HomeIconView(this, HomeIcon.SEARCH, Color.rgb(148, 163, 184)), LinearLayout.LayoutParams(dp(18), dp(18)))
    search = EditText(this).apply {
        hint = "Rechercher un quiz..."; textSize = 9f; setTextColor(ink)
        setHintTextColor(Color.rgb(148, 163, 184)); background = null; setSingleLine(true)
        setPadding(dp(8), 0, 0, 0)
    }
    searchBox.addView(search, LinearLayout.LayoutParams(0, -1, 1f))
    content.addView(searchBox, LinearLayout.LayoutParams(-1, dp(43)).apply { topMargin = dp(13) })

    val filters = listOf("Tous", "Non commencés", "Terminés", "Favoris")
    val filterScroll = HorizontalScrollView(this).apply { isHorizontalScrollBarEnabled = false; clipToPadding = false }
    val filterRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
    val list = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    val count = TextView(this).apply { textSize = 7f; setTextColor(Color.rgb(148, 163, 184)); includeFontPadding = false }

    fun updateList() {
        list.removeAllViews()
        val visible = quizItems.filter { quiz ->
            val matchesQuery = query.isBlank() || quiz.title.contains(query, true) || quiz.subject.contains(query, true)
            val matchesFilter = when (filter) {
                "Non commencés" -> quiz.status == "Non commencé"
                "Terminés" -> quiz.status == "Terminé"
                "Favoris" -> favorites.contains(quiz.id)
                else -> true
            }
            matchesQuery && matchesFilter
        }
        count.text = "${visible.size} évaluations hors ligne"
        visible.forEachIndexed { index, quiz ->
            list.addView(quizCard(quiz, favorites.contains(quiz.id),
                { favorites.toggle(quiz.id); updateList() }, { startQuiz(quiz) }),
                LinearLayout.LayoutParams(-1, -2).apply { if (index > 0) topMargin = dp(8) })
        }
        if (visible.isEmpty()) {
            list.addView(TextView(this).apply {
                text = "Aucun quiz trouvé\nModifiez votre recherche ou choisissez un autre filtre."
                textSize = 10f; setTextColor(muted); gravity = Gravity.CENTER
                setPadding(dp(12), dp(24), dp(12), dp(24)); background = roundedBackground(Color.WHITE, border, 17)
            })
        }
    }

    filters.forEach { label ->
        val chip = TextView(this).apply {
            text = label; textSize = 7f; typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
            setPadding(dp(11), 0, dp(11), 0); isClickable = true; isFocusable = true
            setOnClickListener {
                filter = label
                for (i in 0 until filterRow.childCount) {
                    val item = filterRow.getChildAt(i) as TextView
                    val active = item.text == filter
                    item.setTextColor(if (active) Color.WHITE else muted)
                    item.background = roundedBackground(if (active) blue else Color.WHITE, if (active) blue else border, 20)
                }
                updateList()
            }
        }
        val active = label == filter
        chip.setTextColor(if (active) Color.WHITE else muted)
        chip.background = roundedBackground(if (active) blue else Color.WHITE, if (active) blue else border, 20)
        filterRow.addView(chip, LinearLayout.LayoutParams(-2, dp(28)).apply { if (filterRow.childCount > 0) marginStart = dp(6) })
    }
    filterScroll.addView(filterRow)
    content.addView(filterScroll, LinearLayout.LayoutParams(-1, dp(31)).apply { topMargin = dp(8) })

    val heading = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    val headingCopy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    headingCopy.addView(TextView(this).apply { text = "Quiz disponibles"; textSize = 14f; setTextColor(ink); includeFontPadding = false })
    headingCopy.addView(count, LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(2) })
    heading.addView(headingCopy, LinearLayout.LayoutParams(0, -2, 1f))
    heading.addView(TextView(this).apply { text = "◉ Hors ligne"; textSize = 7f; setTextColor(green); typeface = Typeface.DEFAULT_BOLD })
    content.addView(heading, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(14); bottomMargin = dp(8) })
    content.addView(list)
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
    search.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { query = s?.toString().orEmpty(); updateList() }
        override fun afterTextChanged(s: Editable?) = Unit
    })
    updateList()
    root.addView(evaluationBottomNavigation(), LinearLayout.LayoutParams(-1, dp(60)).apply {
        setMargins(dp(16), dp(2), dp(16), dp(8))
    })
    return phoneScreen(root)
}

private fun MainActivity.evalIconButton(icon: HomeIcon, description: String, action: () -> Unit) =
    FrameLayout(this).apply {
        background = roundedBackground(Color.WHITE, Color.rgb(241, 245, 249), 12)
        elevation = dp(2).toFloat(); contentDescription = description; isClickable = true; isFocusable = true
        setOnClickListener { action() }
        addView(HomeIconView(this@evalIconButton, icon, ink), FrameLayout.LayoutParams(dp(19), dp(19), Gravity.CENTER))
        layoutParams = LinearLayout.LayoutParams(dp(38), dp(38))
    }

private fun MainActivity.featuredQuizCard(onResume: () -> Unit): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(13), dp(12), dp(13), dp(12))
        background = GradientDrawable(GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.rgb(37, 99, 235), Color.rgb(29, 78, 216), Color.rgb(30, 64, 175))).apply { cornerRadius = dp(19).toFloat() }
        elevation = dp(7).toFloat(); clipToOutline = true
    }
    val top = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    top.addView(TextView(this).apply { text = "ϟ"; textSize = 15f; gravity = Gravity.CENTER; setTextColor(Color.rgb(254, 243, 199)); background = roundedBackground(Color.argb(45,255,255,255), Color.TRANSPARENT, 9) }, LinearLayout.LayoutParams(dp(25), dp(25)))
    top.addView(TextView(this).apply { text = "QUIZ EN COURS"; textSize = 6f; letterSpacing = .08f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.rgb(219,234,254)) }, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(7) })
    top.addView(TextView(this).apply { text = "3 / 10"; textSize = 6f; setTextColor(Color.WHITE); setPadding(dp(7), dp(4), dp(7), dp(4)); background = roundedBackground(Color.argb(45,255,255,255), Color.TRANSPARENT, 20) })
    card.addView(top)
    card.addView(TextView(this).apply { text = "Continuez votre dernier quiz"; textSize = 13f; setTextColor(Color.WHITE); typeface = Typeface.DEFAULT_BOLD }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(10) })
    card.addView(TextView(this).apply { text = "Mathématiques · Théorème de Pythagore"; textSize = 7f; setTextColor(Color.rgb(191,219,254)) }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(2) })
    val progress = FrameLayout(this).apply {
        background = roundedBackground(Color.argb(70,255,255,255), Color.TRANSPARENT, 5)
        addView(View(this@featuredQuizCard).apply { background = roundedBackground(Color.WHITE, Color.TRANSPARENT, 5) }, FrameLayout.LayoutParams(dp(36), -1))
    }
    card.addView(progress, LinearLayout.LayoutParams(dp(142), dp(4)).apply { topMargin = dp(10) })
    val resume = TextView(this).apply {
        text = "Reprendre   ➜"; textSize = 7f; setTextColor(Color.rgb(29,78,216)); typeface = Typeface.DEFAULT_BOLD
        gravity = Gravity.CENTER; setPadding(dp(10), 0, dp(10), 0); background = roundedBackground(Color.WHITE, Color.WHITE, 10)
        isClickable = true; setOnClickListener { onResume() }
    }
    card.addView(resume, LinearLayout.LayoutParams(-2, dp(29)).apply { topMargin = dp(9) })
    return card
}

private fun MainActivity.quizCard(quiz: QuizItem, favorite: Boolean, onFavorite: () -> Unit, onStart: () -> Unit): View {
    val card = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(dp(10), dp(10), dp(9), dp(10)); background = roundedBackground(Color.WHITE, border, 17); elevation = dp(2).toFloat() }
    val iconTint = when (quiz.status) { "Terminé" -> green; "Non commencé" -> Color.rgb(124, 58, 237); else -> blue }
    val iconBg = when (quiz.status) { "Terminé" -> Color.rgb(236,253,245); "Non commencé" -> Color.rgb(245,243,255); else -> Color.rgb(239,246,255) }
    card.addView(FrameLayout(this).apply {
        background = roundedBackground(iconBg, Color.TRANSPARENT, 12)
        addView(TextView(this@quizCard).apply { text = "▦"; textSize = 20f; gravity = Gravity.CENTER; setTextColor(iconTint) }, FrameLayout.LayoutParams(-1, -1))
    }, LinearLayout.LayoutParams(dp(36), dp(36)))
    val body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    val heading = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    val names = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    names.addView(TextView(this).apply { text = quiz.subject.uppercase(); textSize = 6f; setTextColor(blue); typeface = Typeface.DEFAULT_BOLD; letterSpacing = .05f })
    names.addView(TextView(this).apply { text = quiz.title; textSize = 9f; setTextColor(ink); typeface = Typeface.DEFAULT_BOLD; maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(3) })
    heading.addView(names, LinearLayout.LayoutParams(0, -2, 1f))
    val heart = TextView(this).apply {
        text = if (favorite) "♥" else "♡"; textSize = 15f; gravity = Gravity.CENTER
        setTextColor(if (favorite) Color.rgb(239,68,68) else Color.rgb(203,213,225))
        background = roundedBackground(if (favorite) Color.rgb(255,241,242) else Color.rgb(248,250,252), Color.TRANSPARENT, 9)
        isClickable = true; setOnClickListener { onFavorite() }
    }
    heading.addView(heart, LinearLayout.LayoutParams(dp(23), dp(23)))
    body.addView(heading)
    body.addView(TextView(this).apply { text = "◷ ${quiz.questions} questions     ◷ ${quiz.duration}"; textSize = 6f; setTextColor(muted) }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(6) })
    val footer = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    val difficultyColor = when (quiz.difficulty) { "Facile" -> Color.rgb(4,120,87); "Avancé" -> Color.rgb(109,40,217); else -> Color.rgb(180,83,9) }
    val difficultyBg = when (quiz.difficulty) { "Facile" -> Color.rgb(209,250,229); "Avancé" -> Color.rgb(237,233,254); else -> Color.rgb(254,243,199) }
    footer.addView(TextView(this).apply { text = quiz.difficulty; textSize = 6f; setTextColor(difficultyColor); setPadding(dp(6), dp(4), dp(6), dp(4)); background = roundedBackground(difficultyBg, Color.TRANSPARENT, 12) })
    quiz.score?.let { score ->
        footer.addView(TextView(this).apply { text = "✓ Meilleur score : ${score}%"; textSize = 5.5f; setTextColor(green); typeface = Typeface.DEFAULT_BOLD }, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(5) })
    } ?: footer.addView(View(this), LinearLayout.LayoutParams(0, 1, 1f))
    val actionText = when (quiz.status) { "En cours" -> "Reprendre"; "Terminé" -> "Recommencer"; else -> "Commencer" }
    footer.addView(TextView(this).apply {
        text = actionText; textSize = 6f; setTextColor(Color.WHITE); typeface = Typeface.DEFAULT_BOLD; gravity = Gravity.CENTER
        setPadding(dp(8), 0, dp(8), 0); background = roundedBackground(blue, blue, 8); isClickable = true; setOnClickListener { onStart() }
    }, LinearLayout.LayoutParams(-2, dp(23)))
    body.addView(footer, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(7) })
    card.addView(body, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(8) })
    return card
}

private fun MainActivity.startQuiz(quiz: QuizItem) {
    AlertDialog.Builder(this)
        .setTitle(quiz.title)
        .setMessage("${quiz.subject}\n${quiz.questions} questions · ${quiz.duration}\n\nLe quiz sera lancé dans la prochaine étape.")
        .setNegativeButton("Fermer", null)
        .setPositiveButton("Continuer") { _, _ -> showMessage("Quiz prêt à démarrer.") }
        .show()
}

private fun MainActivity.evaluationBottomNavigation(): View = LinearLayout(this).apply {
    orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(dp(5), dp(4), dp(5), dp(4))
    background = roundedBackground(Color.WHITE, Color.WHITE, 20); elevation = dp(5).toFloat()
    listOf(Triple(HomeIcon.TEACHER, "Professeur", false), Triple(HomeIcon.CHAT, "Chat", false), Triple(HomeIcon.CALENDAR, "Programme", false), Triple(HomeIcon.EVALUATION, "Évaluation", true)).forEach { (icon, label, selected) ->
        val item = LinearLayout(this@evaluationBottomNavigation).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER
            background = if (selected) roundedBackground(blue, blue, 13) else null
            isClickable = true; isFocusable = true; contentDescription = label
            setOnClickListener { when (label) { "Professeur" -> openTeacherHome(); "Chat" -> openChat(); "Programme" -> openProgramme() } }
        }
        item.addView(HomeIconView(this@evaluationBottomNavigation, icon, if (selected) Color.WHITE else Color.rgb(148,163,184)), LinearLayout.LayoutParams(dp(18), dp(18)))
        item.addView(TextView(this@evaluationBottomNavigation).apply { text = label; textSize = 7f; gravity = Gravity.CENTER; setTextColor(if (selected) Color.WHITE else muted); includeFontPadding = false }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(2) })
        addView(item, LinearLayout.LayoutParams(0, -1, 1f))
    }
}

private fun MutableSet<Int>.toggle(value: Int) {
    if (!add(value)) remove(value)
}
