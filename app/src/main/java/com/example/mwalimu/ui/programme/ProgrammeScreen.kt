package com.example.mwalimu.ui.programme

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
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import com.example.mwalimu.MainActivity
import com.example.mwalimu.ui.components.HomeIcon
import com.example.mwalimu.ui.components.HomeIconView

private data class ProgrammeCourse(
    val subject: String,
    val chapter: String,
    val duration: String,
    val progress: Int,
    val status: String
)

private val programmeCourses = listOf(
    ProgrammeCourse("Mathématiques", "Chapitre 5 · Théorème de Pythagore", "24 min", 68, "En cours"),
    ProgrammeCourse("Français", "Chapitre 3 · Figures de style", "18 min", 100, "Terminé"),
    ProgrammeCourse("Sciences physiques", "Chapitre 2 · Forces et mouvement", "32 min", 35, "En cours"),
    ProgrammeCourse("Algèbre", "Chapitre 6 · Équations du second degré", "28 min", 0, "À commencer"),
    ProgrammeCourse("Biologie", "Chapitre 4 · Génétique", "26 min", 0, "Verrouillé")
)

internal fun MainActivity.programmeScreen(): View {
    var selectedFilter = "Tous"
    var query = ""
    lateinit var search: EditText
    val root = setupRoot()
    val header = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(16), dp(4), dp(16), dp(4))
    }
    header.addView(TextView(this).apply {
        text = "Programme"
        textSize = 20f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
    }, LinearLayout.LayoutParams(0, dp(48), 1f).apply { gravity = Gravity.CENTER_VERTICAL })
    header.addView(programmeHeaderButton(HomeIcon.SEARCH, "Rechercher") { search.requestFocus() })
    header.addView(programmeHeaderButton(HomeIcon.SETTINGS, "Paramètres") {
        openSettings()
    }, LinearLayout.LayoutParams(dp(38), dp(38)).apply { marginStart = dp(6) })
    root.addView(header)

    val scroll = ScrollView(this).apply {
        isVerticalScrollBarEnabled = false
        clipToPadding = false
    }
    val content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(8), dp(16), dp(18))
    }
    content.addView(TextView(this).apply {
        text = "Bonjour, ${learnerName.ifBlank { "Landry" }} 👋"
        textSize = 16f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
    })
    content.addView(TextView(this).apply {
        text = "Continuez votre apprentissage."
        textSize = 10f
        setTextColor(muted)
        includeFontPadding = false
    }, verticalParams(5))
    content.addView(progressCard(), verticalParams(12))
    content.addView(resumeCard(), verticalParams(12))

    val searchRow = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(12), 0, dp(12), 0)
        background = roundedBackground(Color.WHITE, border, 16)
    }
    searchRow.addView(HomeIconView(this, HomeIcon.SEARCH, Color.rgb(148, 163, 184)),
        LinearLayout.LayoutParams(dp(19), dp(19)))
    search = EditText(this).apply {
        hint = "Rechercher un cours..."
        textSize = 10f
        setTextColor(ink)
        setHintTextColor(Color.rgb(148, 163, 184))
        background = null
        setSingleLine(true)
        setPadding(dp(8), 0, 0, 0)
    }
    searchRow.addView(search, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
    content.addView(searchRow, verticalParams(14, dp(46)))

    val filtersHost = HorizontalScrollView(this).apply {
        isHorizontalScrollBarEnabled = false
        clipToPadding = false
        setPadding(dp(0), 0, dp(0), dp(2))
    }
    val filterRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
    val filters = listOf("Tous", "En cours", "Terminés", "Non commencés", "Mathématiques", "Français", "Sciences")
    val courseContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    val count = TextView(this).apply { textSize = 8f; setTextColor(Color.rgb(148, 163, 184)); includeFontPadding = false }
    fun refreshCourses() {
        courseContainer.removeAllViews()
        val filtered = programmeCourses.filter { course ->
            val matchesQuery = query.isBlank() || course.subject.contains(query, true) || course.chapter.contains(query, true)
            val matchesFilter = selectedFilter == "Tous" || when (selectedFilter) {
                "En cours" -> course.status == "En cours"
                "Terminés" -> course.status == "Terminé"
                "Non commencés" -> course.status == "À commencer"
                "Sciences" -> course.subject.contains("Sciences") || course.subject == "Biologie"
                else -> course.subject == selectedFilter
            }
            matchesQuery && matchesFilter
        }
        filtered.forEachIndexed { index, course ->
            courseContainer.addView(courseCard(course), verticalParams(if (index == 0) 0 else 8))
        }
        if (filtered.isEmpty()) {
            courseContainer.addView(TextView(this).apply {
                text = "Aucun cours trouvé\nEssayez un autre mot-clé ou filtre."
                textSize = 11f
                gravity = Gravity.CENTER
                setTextColor(muted)
                setPadding(dp(12), dp(24), dp(12), dp(24))
                background = roundedBackground(Color.WHITE, border, 18)
            })
        }
        count.text = "${filtered.size} cours disponibles"
    }
    filters.forEach { filter ->
        val chip = TextView(this).apply {
            text = filter
            textSize = 8f
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(dp(11), 0, dp(11), 0)
            isClickable = true
            isFocusable = true
            setOnClickListener {
                selectedFilter = filter
                for (i in 0 until filterRow.childCount) {
                    val item = filterRow.getChildAt(i) as TextView
                    val active = item.text == selectedFilter
                    item.setTextColor(if (active) Color.WHITE else muted)
                    item.background = roundedBackground(if (active) blue else Color.WHITE, if (active) blue else border, 20)
                }
                refreshCourses()
            }
        }
        val active = filter == selectedFilter
        chip.setTextColor(if (active) Color.WHITE else muted)
        chip.background = roundedBackground(if (active) blue else Color.WHITE, if (active) blue else border, 20)
        filterRow.addView(chip, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(29)).apply {
            if (filterRow.childCount > 0) marginStart = dp(6)
        })
    }
    filtersHost.addView(filterRow)
    content.addView(filtersHost, verticalParams(9, dp(32)))

    val sectionHeading = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    val headingTexts = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    headingTexts.addView(TextView(this).apply {
        text = "Mes cours"; textSize = 15f; setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD); includeFontPadding = false
    })
    headingTexts.addView(count, verticalParams(2))
    sectionHeading.addView(headingTexts, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    sectionHeading.addView(TextView(this).apply {
        text = "Tout voir"; textSize = 8f; setTextColor(blue)
        typeface = Typeface.create("sans-serif", Typeface.BOLD); isClickable = true
        setOnClickListener { selectedFilter = "Tous"; query = ""; search.setText(""); filterRow.getChildAt(0).performClick() }
    })
    content.addView(sectionHeading, verticalParams(15))
    content.addView(courseContainer)
    content.addView(dailyGoalCard(), verticalParams(10))
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
    search.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { query = s?.toString().orEmpty(); refreshCourses() }
        override fun afterTextChanged(s: Editable?) = Unit
    })
    refreshCourses()
    root.addView(programmeBottomNavigation(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(60)
    ).apply { setMargins(dp(16), dp(2), dp(16), dp(8)) })
    return phoneScreen(root)
}

private fun MainActivity.verticalParams(top: Int, height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) =
    LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height).apply { topMargin = dp(top) }

private fun MainActivity.programmeHeaderButton(icon: HomeIcon, description: String, onClick: () -> Unit) =
    FrameLayout(this).apply {
        background = roundedBackground(Color.WHITE, Color.rgb(241, 245, 249), 12)
        elevation = dp(2).toFloat(); contentDescription = description
        isClickable = true; isFocusable = true; setOnClickListener { onClick() }
        addView(HomeIconView(this@programmeHeaderButton, icon, ink), FrameLayout.LayoutParams(dp(19), dp(19), Gravity.CENTER))
        layoutParams = LinearLayout.LayoutParams(dp(38), dp(38))
    }

private fun MainActivity.progressCard(): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(14), dp(15), dp(12), dp(15))
        background = GradientDrawable(GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.rgb(37, 99, 235), Color.rgb(29, 78, 216), Color.rgb(30, 64, 175))).apply { cornerRadius = dp(20).toFloat() }
        elevation = dp(8).toFloat(); clipToOutline = true
    }
    val copy = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    copy.addView(TextView(this).apply { text = "VOTRE PROGRESSION"; textSize = 7f; setTextColor(Color.rgb(191, 219, 254)); letterSpacing = .08f; typeface = Typeface.DEFAULT_BOLD })
    copy.addView(TextView(this).apply {
        text = "Un bel élan,\ncontinuez !"; textSize = 14f; setTextColor(Color.WHITE)
        typeface = Typeface.DEFAULT_BOLD; setLineSpacing(0f, 1f)
    }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(6) })
    val metrics = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    listOf("18" to "Cours terminés", "7" to "Cours restants").forEachIndexed { i, pair ->
        if (i > 0) metrics.addView(View(this@progressCard).apply { setBackgroundColor(Color.argb(80,255,255,255)) }, LinearLayout.LayoutParams(dp(1), dp(23)).apply { marginStart = dp(9); marginEnd = dp(9) })
        val item = LinearLayout(this@progressCard).apply { orientation = LinearLayout.VERTICAL }
        item.addView(TextView(this@progressCard).apply { text = pair.first; textSize = 14f; setTextColor(Color.WHITE); typeface = Typeface.DEFAULT_BOLD })
        item.addView(TextView(this@progressCard).apply { text = pair.second; textSize = 6f; setTextColor(Color.rgb(191, 219, 254)) })
        metrics.addView(item)
    }
    copy.addView(metrics, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(10) })
    card.addView(copy, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    val ring = FrameLayout(this).apply {
        background = GradientDrawable().apply { shape = GradientDrawable.OVAL; setColor(Color.rgb(36, 88, 207)); setStroke(dp(5), Color.WHITE) }
        elevation = dp(3).toFloat()
        val labels = LinearLayout(this@progressCard).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER }
        labels.addView(TextView(this@progressCard).apply { text = "72%"; textSize = 17f; setTextColor(Color.WHITE); typeface = Typeface.DEFAULT_BOLD; includeFontPadding = false })
        labels.addView(TextView(this@progressCard).apply { text = "Global"; textSize = 6f; setTextColor(Color.rgb(219, 234, 254)) })
        addView(labels, FrameLayout.LayoutParams(-1, -1))
    }
    card.addView(ring, LinearLayout.LayoutParams(dp(80), dp(80)))
    return card
}

private fun MainActivity.resumeCard(): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL; gravity = Gravity.TOP
        setPadding(dp(12), dp(11), dp(12), dp(11))
        background = roundedBackground(Color.WHITE, Color.rgb(219, 234, 254), 18); elevation = dp(3).toFloat()
    }
    card.addView(FrameLayout(this).apply {
        background = roundedBackground(Color.rgb(239, 246, 255), Color.TRANSPARENT, 13)
        addView(TextView(this@resumeCard).apply { text = "▦"; textSize = 23f; setTextColor(blue); gravity = Gravity.CENTER }, FrameLayout.LayoutParams(-1, -1))
    }, LinearLayout.LayoutParams(dp(40), dp(40)))
    val details = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    details.addView(TextView(this).apply { text = "REPRENDRE VOTRE DERNIER COURS"; textSize = 6f; setTextColor(blue); typeface = Typeface.DEFAULT_BOLD; letterSpacing = .04f })
    details.addView(TextView(this).apply { text = "Mathématiques"; textSize = 12f; setTextColor(ink); typeface = Typeface.DEFAULT_BOLD }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(3) })
    details.addView(TextView(this).apply { text = "Chapitre 5 · Théorème de Pythagore"; textSize = 7f; setTextColor(muted) }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(2) })
    val button = TextView(this).apply {
        text = "Continuer  ➜"; textSize = 9f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
        setPadding(dp(12), 0, dp(12), 0); background = roundedBackground(blue, blue, 10)
        elevation = dp(3).toFloat(); isClickable = true; setOnClickListener { openTeacherHome() }
    }
    details.addView(button, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(31)).apply { topMargin = dp(8) })
    card.addView(details, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(9) })
    return card
}

private fun MainActivity.courseCard(course: ProgrammeCourse): View {
    val card = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; setPadding(dp(11), dp(11), dp(11), dp(11)); background = roundedBackground(Color.WHITE, border, 17); elevation = dp(2).toFloat() }
    card.addView(FrameLayout(this).apply {
        background = roundedBackground(if (course.status == "Terminé") Color.rgb(236,253,245) else Color.rgb(239,246,255), Color.TRANSPARENT, 12)
        addView(TextView(this@courseCard).apply { text = "▦"; textSize = 20f; setTextColor(if (course.status == "Terminé") green else blue); gravity = Gravity.CENTER }, FrameLayout.LayoutParams(-1, -1))
    }, LinearLayout.LayoutParams(dp(38), dp(38)))
    val detail = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
    val titleRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    titleRow.addView(TextView(this).apply { text = course.subject; textSize = 10f; setTextColor(ink); typeface = Typeface.DEFAULT_BOLD; maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END }, LinearLayout.LayoutParams(0, -2, 1f))
    titleRow.addView(TextView(this).apply {
        text = course.status; textSize = 6f; setTextColor(if (course.status == "Terminé") Color.rgb(4,120,87) else blue)
        setPadding(dp(7), dp(4), dp(7), dp(4)); background = roundedBackground(if (course.status == "Terminé") Color.rgb(209,250,229) else Color.rgb(219,234,254), Color.TRANSPARENT, 20)
    }, LinearLayout.LayoutParams(-2, -2).apply { marginStart = dp(4) })
    detail.addView(titleRow)
    detail.addView(TextView(this).apply { text = course.chapter; textSize = 7f; setTextColor(muted); maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(3) })
    val meta = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
    meta.addView(TextView(this).apply { text = "◷  ${course.duration}"; textSize = 7f; setTextColor(muted) }, LinearLayout.LayoutParams(0, -2, 1f))
    meta.addView(TextView(this).apply { text = "${course.progress}%"; textSize = 7f; setTextColor(muted) })
    detail.addView(meta, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(7) })
    detail.addView(ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
        max = 100
        progress = course.progress
        progressTintList = android.content.res.ColorStateList.valueOf(blue)
        progressBackgroundTintList = android.content.res.ColorStateList.valueOf(Color.rgb(226, 232, 240))
        isIndeterminate = false
    }, LinearLayout.LayoutParams(-1, dp(4)).apply { topMargin = dp(4) })
    detail.addView(TextView(this).apply {
        text = when (course.status) { "Verrouillé" -> "Indisponible"; "Terminé" -> "Revoir"; "À commencer" -> "Commencer"; else -> "Continuer  ➜" }
        textSize = 8f; setTextColor(Color.WHITE); gravity = Gravity.CENTER; background = roundedBackground(if (course.status == "Verrouillé") muted else blue, Color.TRANSPARENT, 9)
        setPadding(dp(9), dp(7), dp(9), dp(7)); isClickable = course.status != "Verrouillé"
        setOnClickListener { if (course.status != "Verrouillé") openTeacherHome() }
    }, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, -2).apply { topMargin = dp(7) })
    card.addView(detail, LinearLayout.LayoutParams(0, -2, 1f).apply { marginStart = dp(9) })
    return card
}

private fun MainActivity.dailyGoalCard(): View = LinearLayout(this).apply {
    orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(dp(11), dp(10), dp(11), dp(10))
    background = roundedBackground(Color.WHITE, Color.rgb(220,252,231), 16)
    addView(TextView(this@dailyGoalCard).apply { text = "🔥"; textSize = 20f }, LinearLayout.LayoutParams(dp(32), dp(32)))
    val copy = LinearLayout(this@dailyGoalCard).apply { orientation = LinearLayout.VERTICAL }
    copy.addView(TextView(this@dailyGoalCard).apply { text = "Objectif du jour"; textSize = 8f; setTextColor(muted) })
    copy.addView(TextView(this@dailyGoalCard).apply { text = "Encore 10 minutes, ${learnerName.ifBlank { "Landry" }} !"; textSize = 9f; setTextColor(ink); typeface = Typeface.DEFAULT_BOLD })
    addView(copy, LinearLayout.LayoutParams(0, -2, 1f))
    addView(TextView(this@dailyGoalCard).apply { text = "20\n/ 30 min"; textSize = 8f; gravity = Gravity.CENTER; setTextColor(green); typeface = Typeface.DEFAULT_BOLD })
}

private fun MainActivity.programmeBottomNavigation(): View = LinearLayout(this).apply {
    orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER; setPadding(dp(5), dp(4), dp(5), dp(4))
    background = roundedBackground(Color.WHITE, Color.WHITE, 20); elevation = dp(5).toFloat()
        listOf(Triple(HomeIcon.TEACHER, "Professeur", false), Triple(HomeIcon.CHAT, "Chat", false), Triple(HomeIcon.CALENDAR, "Programme", true), Triple(HomeIcon.EVALUATION, "Évaluation", false)).forEach { (icon, label, selected) ->
        val item = LinearLayout(this@programmeBottomNavigation).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER
            background = if (selected) roundedBackground(blue, blue, 13) else null
            isClickable = true; isFocusable = true; contentDescription = label
            setOnClickListener { when(label) { "Professeur" -> openTeacherHome(); "Chat" -> openChat(); "Évaluation" -> openEvaluation() } }
        }
        item.addView(HomeIconView(this@programmeBottomNavigation, icon, if (selected) Color.WHITE else Color.rgb(148,163,184)), LinearLayout.LayoutParams(dp(18), dp(18)))
        item.addView(TextView(this@programmeBottomNavigation).apply { text = label; textSize = 7f; gravity = Gravity.CENTER; setTextColor(if (selected) Color.WHITE else muted); includeFontPadding = false }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(2) })
        addView(item, LinearLayout.LayoutParams(0, -1, 1f))
    }
}
