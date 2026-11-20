package com.example.mwalimu.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mwalimu.MainActivity
import com.example.mwalimu.ui.components.HomeIcon
import com.example.mwalimu.ui.components.HomeIconView
import com.example.mwalimu.ui.onboarding.OnboardingIllustrationView

internal fun MainActivity.teacherHomeScreen(): View {
    val root = setupRoot().apply {
        setPadding(0, dp(8), 0, dp(12))
    }
    val scroll = setupScroll().apply {
        isVerticalScrollBarEnabled = false
    }
    val content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(8), dp(16), dp(12))
    }

    content.addView(homeHeader())
    content.addView(teacherHero(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(8)
    })
    content.addView(lessonMessageCard(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(14)
    })
    content.addView(homeActionRow(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(16)
    })
    content.addView(homeStatsRow(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(64)
    ).apply {
        topMargin = dp(16)
    })

    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
    ))
    root.addView(homeBottomNavigation(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(66)
    ).apply {
        setMargins(dp(16), dp(4), dp(16), dp(8))
    })
    return phoneScreen(root)
}

private fun MainActivity.homeHeader(): View {
    val header = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    val greeting = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    greeting.addView(TextView(this).apply {
        val name = learnerName.ifBlank { "élève" }
        text = "Bonjour, $name 👋"
        setTextColor(ink)
        textSize = 16f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    })
    greeting.addView(TextView(this).apply {
        text = "Prêt à apprendre aujourd’hui ?"
        setTextColor(muted)
        textSize = 11f
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        includeFontPadding = false
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(2)
    })
    header.addView(greeting, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    header.addView(homeHeaderIcon(HomeIcon.NOTIFICATIONS, "Notifications") {
        showMessage("Vous n’avez pas de nouvelle notification.")
    })
    header.addView(homeHeaderIcon(HomeIcon.SETTINGS, "Paramètres") {
        openSettings()
    }, LinearLayout.LayoutParams(dp(42), dp(42)).apply {
        marginStart = dp(7)
    })
    return header
}

private fun MainActivity.homeHeaderIcon(icon: HomeIcon, description: String, onClick: () -> Unit) =
    FrameLayout(this).apply {
        background = roundedBackground(Color.WHITE, Color.WHITE, 12)
        elevation = dp(2).toFloat()
        contentDescription = description
        isClickable = true
        isFocusable = true
        setOnClickListener { onClick() }
        addView(HomeIconView(this@homeHeaderIcon, icon, ink), FrameLayout.LayoutParams(
            dp(20), dp(20), Gravity.CENTER
        ))
        layoutParams = LinearLayout.LayoutParams(dp(42), dp(42))
    }

private fun MainActivity.teacherHero(): View {
    val hero = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }
    hero.addView(OnboardingIllustrationView(this).apply {
        scene = 5
        contentDescription = "$teacherName, votre enseignante"
    }, LinearLayout.LayoutParams(dp(250), dp(190)))
    hero.addView(TextView(this).apply {
        text = teacherName
        setTextColor(ink)
        textSize = 14f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
        gravity = Gravity.CENTER
        maxLines = 1
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(-3)
    })
    hero.addView(TextView(this).apply {
        text = "●  En attente"
        setTextColor(muted)
        textSize = 9f
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        includeFontPadding = false
        gravity = Gravity.CENTER
        background = roundedBackground(Color.rgb(241, 245, 249), Color.rgb(241, 245, 249), 10)
        setPadding(dp(8), dp(3), dp(8), dp(3))
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(4)
    })
    return hero
}

private fun MainActivity.lessonMessageCard(): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(14), dp(14), dp(14), dp(14))
        background = roundedBackground(Color.WHITE, border, 16)
        elevation = dp(4).toFloat()
    }
    val heading = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    heading.addView(TextView(this).apply {
        text = "▣"
        setTextColor(blue)
        textSize = 11f
        contentDescription = "Sous-titres"
    })
    heading.addView(TextView(this).apply {
        text = "SOUS-TITRES"
        setTextColor(muted)
        textSize = 9f
        letterSpacing = 0.06f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
    }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
        marginStart = dp(6)
    })
    heading.addView(TextView(this).apply {
        text = "•••"
        setTextColor(green)
        textSize = 10f
        contentDescription = "Options du message"
    })
    card.addView(heading)
    card.addView(TextView(this).apply {
        text = "Bonjour ${learnerName.ifBlank { "élève" }} !"
        setTextColor(ink)
        textSize = 11f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(7)
    })
    card.addView(TextView(this).apply {
        text = "Je suis heureuse de vous retrouver aujourd’hui. Nous allons commencer votre prochaine leçon."
        setTextColor(muted)
        textSize = 10f
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        includeFontPadding = false
        setLineSpacing(dp(2).toFloat(), 1f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(3)
    })
    return card
}

private fun MainActivity.homeActionRow(): View {
    val actions = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }
    val buttons = listOf(
        Triple(HomeIcon.REPEAT, "Répéter") { playTeacherWelcome() },
        Triple(HomeIcon.PLAY, "Démarrer") { playTeacherWelcome() },
        Triple(HomeIcon.MICROPHONE, "Micro") {
            showMessage("La saisie vocale sera disponible prochainement.")
        },
        Triple(HomeIcon.MESSAGE, "Texte") { showTeacherText() }
    )
    buttons.forEachIndexed { index, (symbol, label, onClick) ->
        actions.addView(homeActionButton(symbol, label, index == 1, onClick),
            LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    }
    return actions
}

private fun MainActivity.homeActionButton(
    icon: HomeIcon,
    label: String,
    primary: Boolean,
    onClick: () -> Unit
): View {
    val item = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
    }
    item.addView(FrameLayout(this).apply {
        background = roundedBackground(
            if (primary) blue else Color.WHITE,
            if (primary) blue else Color.WHITE,
            12
        )
        if (primary) elevation = dp(4).toFloat()
        contentDescription = label
        isClickable = true
        isFocusable = true
        setOnClickListener { onClick() }
        addView(HomeIconView(
            this@homeActionButton,
            icon,
            if (primary) Color.WHITE else muted
        ), FrameLayout.LayoutParams(dp(21), dp(21), Gravity.CENTER))
    }, LinearLayout.LayoutParams(dp(44), dp(44)))
    item.addView(TextView(this).apply {
        text = label
        textSize = 9f
        setTextColor(muted)
        gravity = Gravity.CENTER
        typeface = Typeface.create(
            "sans-serif",
            if (primary) Typeface.BOLD else Typeface.NORMAL
        )
        includeFontPadding = false
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(4)
    })
    return item
}

private fun MainActivity.homeStatsRow(): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
    }
    val stats = listOf(
        Triple(HomeIcon.BOOK, "Cours", "12"),
        Triple(HomeIcon.FLAME, "Série", "7 jours"),
        Triple(HomeIcon.CLOCK, "Étude", "35 min")
    )
    stats.forEachIndexed { index, (icon, label, value) ->
        row.addView(homeStatCard(icon, label, value, index), LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        ).apply {
            if (index > 0) marginStart = dp(7)
        })
    }
    return row
}

private fun MainActivity.homeStatCard(icon: HomeIcon, label: String, value: String, index: Int): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(8), dp(7), dp(6), dp(7))
        background = roundedBackground(Color.WHITE, Color.WHITE, 14)
        elevation = dp(2).toFloat()
    }
    val tint = when (index) {
        1 -> amber
        2 -> green
        else -> blue
    }
    card.addView(FrameLayout(this).apply {
        background = roundedBackground(
            when (index) {
                1 -> Color.rgb(255, 247, 237)
                2 -> Color.rgb(220, 252, 231)
                else -> Color.rgb(219, 234, 254)
            },
            Color.TRANSPARENT,
            8
        )
        addView(HomeIconView(this@homeStatCard, icon, tint), FrameLayout.LayoutParams(
            dp(16), dp(16), Gravity.CENTER
        ))
    }, LinearLayout.LayoutParams(dp(26), dp(26)))
    val labelsColumn = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    labelsColumn.addView(TextView(this).apply {
        text = label
        setTextColor(muted)
        textSize = 8f
        typeface = Typeface.create("sans-serif", Typeface.NORMAL)
        includeFontPadding = false
    })
    labelsColumn.addView(TextView(this).apply {
        text = value
        setTextColor(ink)
        textSize = 8f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        includeFontPadding = false
        maxLines = 1
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(2)
    })
    card.addView(labelsColumn, LinearLayout.LayoutParams(
        0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
    ).apply {
        marginStart = dp(5)
    })
    return card
}

private fun MainActivity.homeBottomNavigation(): View {
    val navigation = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(dp(5), dp(5), dp(5), dp(5))
        background = roundedBackground(Color.WHITE, Color.WHITE, 20)
        elevation = dp(5).toFloat()
    }
    val items = listOf(
        Triple(HomeIcon.TEACHER, "Professeur", true),
        Triple(HomeIcon.CHAT, "Chat", false),
        Triple(HomeIcon.CALENDAR, "Programme", false),
        Triple(HomeIcon.EVALUATION, "Évaluation", false)
    )
    items.forEach { (icon, label, selected) ->
        navigation.addView(navItem(icon, label, selected), LinearLayout.LayoutParams(
            0, ViewGroup.LayoutParams.MATCH_PARENT, 1f
        ))
    }
    return navigation
}

private fun MainActivity.navItem(icon: HomeIcon, label: String, selected: Boolean): View {
    val item = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        background = if (selected) roundedBackground(blue, blue, 13) else null
        isClickable = true
        isFocusable = true
        contentDescription = label
        setOnClickListener {
            when {
                label == "Chat" -> openChat()
                label == "Programme" -> openProgramme()
                label == "Évaluation" -> openEvaluation()
                !selected -> showMessage("$label sera disponible prochainement.")
            }
        }
    }
    item.addView(HomeIconView(
        this,
        icon,
        if (selected) Color.WHITE else Color.rgb(148, 163, 184)
    ), LinearLayout.LayoutParams(dp(18), dp(18)))
    item.addView(TextView(this).apply {
        text = label
        textSize = 8f
        setTextColor(if (selected) Color.WHITE else muted)
        gravity = Gravity.CENTER
        typeface = Typeface.create(
            "sans-serif",
            if (selected) Typeface.BOLD else Typeface.NORMAL
        )
        includeFontPadding = false
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(2)
    })
    return item
}
