package com.example.mwalimu.ui.onboarding

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.mwalimu.MainActivity
import com.example.mwalimu.R
import com.example.mwalimu.ui.onboarding.OnboardingIllustrationView
import java.util.Locale

internal fun MainActivity.welcomeScreen(): View {
    val root = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setBackgroundColor(this@welcomeScreen.background)
        setPadding(dp(24), dp(12), dp(24), dp(12))
    }
    root.addView(brandHeader(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(44)
    ))

    val illustration = OnboardingIllustrationView(this).apply {
        scene = welcomePage
        contentDescription = getString(
            when (welcomePage) {
                1 -> R.string.illustration_questions
                2 -> R.string.illustration_progress
                else -> R.string.illustration_classroom
            }
        )
        isFocusable = true
        setOnTouchListener(swipeListener())
    }
    root.addView(illustration, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
    ).apply {
        topMargin = dp(4)
        bottomMargin = dp(4)
    })

    val slides = MainActivity.WELCOME_SLIDES[welcomePage]
    root.addView(TextView(this).apply {
        text = slides.eyebrow
        setTextColor(blue)
        textSize = 11f
        letterSpacing = 0.14f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        gravity = Gravity.CENTER
    })
    root.addView(TextView(this).apply {
        text = slides.title
        setTextColor(ink)
        textSize = 25f
        letterSpacing = -0.035f
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        gravity = Gravity.CENTER
        setLineSpacing(dp(1).toFloat(), 1f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(10)
    })
    root.addView(TextView(this).apply {
        text = slides.description
        setTextColor(muted)
        textSize = 14f
        gravity = Gravity.CENTER
        setLineSpacing(dp(3).toFloat(), 1f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(10)
    })

    val footer = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }
    footer.addView(pageIndicators())
    footer.addView(primaryButton(
        if (welcomePage == MainActivity.WELCOME_SLIDES.lastIndex) "Commencer" else "Continuer"
    ) {
        if (welcomePage < MainActivity.WELCOME_SLIDES.lastIndex) {
            welcomePage += 1
            render()
        } else {
            stage = MainActivity.STAGE_PROFILE
            render()
        }
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(56)
    ).apply {
        topMargin = dp(10)
    })
    if (welcomePage < MainActivity.WELCOME_SLIDES.lastIndex) {
        footer.addView(textButton("Passer") {
            stage = MainActivity.STAGE_PROFILE
            render()
        }, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(38)
        ).apply {
            topMargin = dp(2)
        })
    }
    root.addView(footer, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(10)
    })
    return root
}

internal fun MainActivity.profileScreen(): View {
    val root = setupRoot()
    root.addView(stepHeader(2, "Votre profil") {
        stage = MainActivity.STAGE_WELCOME
        welcomePage = MainActivity.WELCOME_SLIDES.lastIndex
        render()
    })
    val scroll = setupScroll()
    val content = scrollContent()
    content.addView(sectionTitle(
        "Parlez-nous de vous",
        "Ces informations permettront de personnaliser votre apprentissage."
    ))
    content.addView(OnboardingIllustrationView(this).apply {
        scene = 3
        contentDescription = getString(R.string.illustration_profile)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(170)
    ).apply {
        topMargin = dp(2)
        bottomMargin = dp(4)
    })

    val nameInput = EditText(this).apply {
        setSingleLine(true)
        textSize = 16f
        setTextColor(ink)
        setHintTextColor(muted)
        hint = "Ex. : Aline"
        setPadding(dp(16), 0, dp(16), 0)
        background = roundedBackground(Color.WHITE, border, 14)
        inputType = android.text.InputType.TYPE_CLASS_TEXT or
            android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
        setText(learnerName)
        contentDescription = "Prénom ou pseudonyme"
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                learnerName = s?.toString().orEmpty()
            }
            override fun afterTextChanged(s: Editable?) = Unit
        })
    }
    content.addView(fieldGroup("Prénom ou pseudonyme", nameInput))

    val levelSpinner = Spinner(this).apply {
        adapter = ArrayAdapter(
            this@profileScreen,
            android.R.layout.simple_spinner_dropdown_item,
            MainActivity.SCHOOL_LEVELS
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        background = roundedBackground(Color.WHITE, border, 14)
        setPadding(dp(12), 0, dp(12), 0)
        contentDescription = "Niveau scolaire"
        val selectedIndex = MainActivity.SCHOOL_LEVELS.indexOf(schoolLevel).coerceAtLeast(0)
        setSelection(selectedIndex)
        onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                schoolLevel = if (position == 0) "" else MainActivity.SCHOOL_LEVELS[position]
            }
        }
    }
    content.addView(
        fieldGroup("Niveau scolaire", levelSpinner),
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(16)
        }
    )
    content.addView(infoCard(
        "Vos informations restent privées sur cet appareil.",
        "🔒"
    ), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(16)
    })
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
    ))
    root.addView(primaryButton("Continuer") {
        if (learnerName.isNotBlank() && schoolLevel.isNotBlank()) {
            stage = MainActivity.STAGE_TEACHER
            render()
        } else if (learnerName.isBlank()) {
            nameInput.error = "Saisissez votre prénom"
            nameInput.requestFocus()
        } else {
            showMessage("Sélectionnez votre niveau scolaire.")
        }
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(56)
    ).apply {
        setMargins(dp(24), dp(8), dp(24), dp(14))
    })
    return root
}

internal fun MainActivity.teacherScreen(): View {
    val root = setupRoot()
    root.addView(stepHeader(3, "Votre enseignant") {
        stage = MainActivity.STAGE_PROFILE
        render()
    })
    val scroll = setupScroll()
    val content = scrollContent()
    content.addView(sectionTitle(
        "Choisissez votre enseignant",
        "Vous pourrez modifier ce choix à tout moment."
    ))

    val teacherChoices = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
    }
    teacherChoices.addView(teacherCard(
        "Professeur David", "Calme · Encourageant", "👨🏽‍🏫"
    ), LinearLayout.LayoutParams(0, dp(168), 1f).apply {
        marginEnd = dp(8)
    })
    teacherChoices.addView(teacherCard(
        "Professeure Amina", "Patiente · Pédagogue", "👩🏽‍🏫"
    ), LinearLayout.LayoutParams(0, dp(168), 1f).apply {
        marginStart = dp(8)
    })
    content.addView(teacherChoices, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(24)
    })

    content.addView(TextView(this).apply {
        text = "Choisissez une voix"
        textSize = 16f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(24)
        bottomMargin = dp(10)
    })
    MainActivity.VOICES.forEach { voice ->
        content.addView(voiceRow(voice), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(62)
        ).apply {
            bottomMargin = dp(8)
        })
    }
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
    ))
    root.addView(primaryButton("Continuer") {
        stage = MainActivity.STAGE_GOAL
        render()
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(56)
    ).apply {
        setMargins(dp(24), dp(8), dp(24), dp(14))
    })
    return root
}

internal fun MainActivity.goalScreen(): View {
    val root = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(this@goalScreen.background)
    }
    root.addView(goalStepHeader {
        stage = MainActivity.STAGE_TEACHER
        render()
    })
    val scroll = setupScroll()
    val content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(24), dp(24), dp(24), dp(8))
    }
    content.addView(goalHeading())
    content.addView(goalOptions(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(17)
    })
    content.addView(summaryCard(), LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(15)
    })
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
    ))
    val footer = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setPadding(dp(24), dp(6), dp(24), dp(4))
    }
    footer.addView(primaryButton("Commencer l’aventure   🚀") {
        stage = MainActivity.STAGE_HOME
        render()
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(54)
    ))
    footer.addView(TextView(this).apply {
        text = "Vous pourrez modifier ces paramètres plus tard."
        textSize = 9f
        setTextColor(muted)
        gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(22)
    ).apply {
        topMargin = dp(2)
    })
    root.addView(footer)
    return phoneScreen(root)
}


internal fun MainActivity.goalStepHeader(onBack: () -> Unit): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(24), dp(10), dp(24), dp(4))
    }
    row.addView(TextView(this).apply {
        text = "‹"
        textSize = 30f
        setTextColor(ink)
        gravity = Gravity.CENTER
        background = roundedBackground(Color.WHITE, border, 12)
        contentDescription = "Retour"
        isClickable = true
        isFocusable = true
        setOnClickListener { onBack() }
    }, LinearLayout.LayoutParams(dp(38), dp(38)))
    val progress = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
    }
    progress.addView(TextView(this).apply {
        text = "Étape 4 sur 4"
        textSize = 9f
        setTextColor(ink)
        gravity = Gravity.CENTER
    })
    progress.addView(FrameLayout(this).apply {
        background = roundedBackground(border, border, 4)
        addView(View(this@goalStepHeader).apply {
            background = roundedBackground(blue, blue, 4)
        }, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(3)
        ))
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(3)
    ).apply {
        topMargin = dp(7)
    })
    row.addView(progress, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        .apply {
            marginStart = dp(14)
            marginEnd = dp(14)
        })
    row.addView(View(this), LinearLayout.LayoutParams(dp(38), dp(38)))
    return row
}

private fun MainActivity.goalHeading(): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.TOP
    }
    row.addView(TextView(this).apply {
        text = "⚑"
        textSize = 22f
        setTextColor(blue)
        gravity = Gravity.CENTER
        background = roundedBackground(Color.rgb(219, 234, 254), Color.rgb(219, 234, 254), 12)
    }, LinearLayout.LayoutParams(dp(46), dp(46)).apply {
        marginEnd = dp(13)
    })
    val copy = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    copy.addView(TextView(this).apply {
        text = "Quel est votre objectif\nquotidien ?"
        textSize = 22f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        setLineSpacing(0f, 0.96f)
    })
    copy.addView(TextView(this).apply {
        text = "Choisissez le temps que vous souhaitez\nconsacrer chaque jour à vos études."
        textSize = 12f
        setTextColor(muted)
        setLineSpacing(dp(1).toFloat(), 1f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(5)
    })
    row.addView(copy)
    return row
}

private fun MainActivity.goalOptions(): View {
    val options = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    val firstRow = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
    }
    MainActivity.GOALS.take(3).forEachIndexed { index, goal ->
        firstRow.addView(goalCard(goal), LinearLayout.LayoutParams(0, dp(113), 1f).apply {
            if (index > 0) marginStart = dp(7)
        })
    }
    options.addView(firstRow)

    val secondRow = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }
    MainActivity.GOALS.drop(3).forEachIndexed { index, goal ->
        secondRow.addView(goalCard(goal), LinearLayout.LayoutParams(0, dp(113), 1f).apply {
            if (index > 0) marginStart = dp(7)
            if (index == 0) marginEnd = dp(3)
        })
    }
    options.addView(secondRow, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(7)
    })

    secondRow.post {
        val cardWidth = firstRow.getChildAt(0).width
        secondRow.layoutParams = (secondRow.layoutParams as LinearLayout.LayoutParams).apply {
            width = cardWidth * 2 + dp(7)
            gravity = Gravity.CENTER
        }
    }
    return options
}


private fun MainActivity.scrollContent() = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    setPadding(dp(24), dp(8), dp(24), dp(16))
}

private fun MainActivity.stepHeader(step: Int, title: String, onBack: () -> Unit): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(20), dp(16), dp(20), dp(12))
    }
    val back = TextView(this).apply {
        text = "‹"
        textSize = 32f
        setTextColor(ink)
        gravity = Gravity.CENTER
        background = roundedBackground(Color.WHITE, border, 16)
        contentDescription = "Retour"
        isClickable = true
        isFocusable = true
        setOnClickListener { onBack() }
    }
    row.addView(back, LinearLayout.LayoutParams(dp(48), dp(48)))
    val center = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
    }
    center.addView(TextView(this).apply {
        text = "Étape $step sur 4 · $title"
        textSize = 11f
        setTextColor(muted)
        gravity = Gravity.CENTER
    })
    val track = FrameLayout(this).apply {
        background = roundedBackground(border, border, 4)
    }
    val progress = View(this).apply {
        background = roundedBackground(blue, blue, 4)
    }
    track.addView(progress, FrameLayout.LayoutParams(0, dp(5), Gravity.START))
    track.post {
        progress.layoutParams = FrameLayout.LayoutParams(
            track.width * step / 4,
            dp(5),
            Gravity.START
        )
    }
    center.addView(track, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(5)
    ).apply {
        topMargin = dp(7)
    })
    row.addView(center, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
        .apply {
            marginStart = dp(14)
            marginEnd = dp(14)
        })
    row.addView(View(this), LinearLayout.LayoutParams(dp(48), dp(48)))
    return row
}

private fun MainActivity.brandHeader(): View {
    val header = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
    }
    val mark = TextView(this).apply {
        text = "✦"
        textSize = 20f
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER
        background = roundedBackground(blue, blue, 12)
        elevation = dp(5).toFloat()
    }
    header.addView(mark, LinearLayout.LayoutParams(dp(36), dp(36)))
    header.addView(TextView(this).apply {
        text = "MWALIMU"
        textSize = 15f
        letterSpacing = 0.1f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        marginStart = dp(9)
    })
    return header
}

private fun MainActivity.pageIndicators(): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        setPadding(0, dp(4), 0, dp(4))
    }
    repeat(MainActivity.WELCOME_SLIDES.size) { index ->
        val indicator = View(this).apply {
            background = roundedBackground(
                if (index == welcomePage) blue else Color.rgb(203, 213, 225),
                if (index == welcomePage) blue else Color.rgb(203, 213, 225),
                8
            )
            contentDescription = if (index == welcomePage) {
                "Page ${index + 1} sur ${MainActivity.WELCOME_SLIDES.size}"
            } else null
        }
        row.addView(indicator, LinearLayout.LayoutParams(
            if (index == welcomePage) dp(24) else dp(7), dp(7)
        ).apply {
            marginStart = dp(3)
            marginEnd = dp(3)
        })
    }
    return row
}

private fun MainActivity.sectionTitle(title: String, description: String): View {
    val block = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
    }
    block.addView(TextView(this).apply {
        text = title
        textSize = 23f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        gravity = Gravity.CENTER
    })
    block.addView(TextView(this).apply {
        text = description
        textSize = 14f
        setTextColor(muted)
        gravity = Gravity.CENTER
        setLineSpacing(dp(2).toFloat(), 1f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(8)
    })
    return block
}

private fun MainActivity.fieldGroup(label: String, field: View): View {
    val group = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    group.addView(TextView(this).apply {
        text = label
        textSize = 13f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    })
    group.addView(field, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, dp(54)
    ).apply {
        topMargin = dp(7)
    })
    return group
}

private fun MainActivity.teacherCard(name: String, description: String, avatar: String): View {
    val selected = teacherName == name
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        setPadding(dp(8), dp(10), dp(8), dp(10))
        background = roundedBackground(
            Color.WHITE,
            if (selected) blue else border,
            18,
            if (selected) dp(2) else dp(1)
        )
        isClickable = true
        isFocusable = true
        contentDescription = "$name, $description${if (selected) ", sélectionné" else ""}"
        setOnClickListener {
            teacherName = name
            render()
        }
    }
    card.addView(TextView(this).apply {
        text = avatar
        textSize = 48f
        gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(dp(76), dp(76)))
    card.addView(TextView(this).apply {
        text = name
        textSize = 13f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(6)
    })
    card.addView(TextView(this).apply {
        text = description
        textSize = 10f
        setTextColor(muted)
        gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(4)
    })
    return card
}

private fun MainActivity.voiceRow(voice: String): View {
    val selected = teacherVoice == voice
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(14), 0, dp(14), 0)
        background = roundedBackground(
            if (selected) Color.rgb(239, 246, 255) else Color.WHITE,
            if (selected) blue else border,
            14,
            if (selected) dp(2) else dp(1)
        )
        isClickable = true
        isFocusable = true
        contentDescription = "$voice${if (selected) ", sélectionnée" else ""}"
        setOnClickListener {
            teacherVoice = voice
            render()
        }
    }
    row.addView(TextView(this).apply {
        text = "♫"
        textSize = 19f
        setTextColor(blue)
        gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(dp(36), dp(36)).apply {
        marginEnd = dp(12)
    })
    val labels = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    labels.addView(TextView(this).apply {
        text = voice
        textSize = 14f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    })
    labels.addView(TextView(this).apply {
        text = when (voice) {
            "Voix posée" -> "Calme et rassurante"
            "Voix dynamique" -> "Énergique et motivante"
            else -> "Chaleureuse et naturelle"
        }
        textSize = 11f
        setTextColor(muted)
    })
    row.addView(labels, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    val preview = TextView(this).apply {
        text = "▶"
        textSize = 14f
        setTextColor(blue)
        gravity = Gravity.CENTER
        contentDescription = "Écouter $voice"
        isClickable = true
        isFocusable = true
        setOnClickListener { previewVoice(voice) }
    }
    row.addView(preview, LinearLayout.LayoutParams(dp(42), dp(42)))
    row.addView(radioMark(selected))
    return row
}

private fun MainActivity.previewVoice(voice: String) {
    teacherVoice = voice
    render()

    if (textToSpeechReady) {
        speakVoicePreview()
        return
    }
    if (textToSpeech != null) {
        showMessage("La synthèse vocale est en cours de préparation.")
        return
    }

    textToSpeech = TextToSpeech(this) { status ->
        val engine = textToSpeech
        if (status != TextToSpeech.SUCCESS || engine == null) {
            textToSpeech = null
            showMessage("La prévisualisation vocale n’est pas disponible.")
            return@TextToSpeech
        }
        val languageStatus = engine.setLanguage(Locale.FRANCE)
        if (languageStatus < TextToSpeech.LANG_AVAILABLE) {
            engine.shutdown()
            textToSpeech = null
            showMessage("La voix française n’est pas installée sur cet appareil.")
            return@TextToSpeech
        }
        textToSpeechReady = true
        speakVoicePreview()
    }
}

private fun MainActivity.speakVoicePreview() {
    speakText("Bonjour et bienvenue sur MWALIMU. Apprenons ensemble.", "mwalimu-onboarding-voice-preview")
}

internal fun MainActivity.speakText(message: String, utteranceId: String) {
    val engine = textToSpeech ?: return
    engine.setPitch(
        when (teacherVoice) {
            "Voix posée" -> 0.9f
            "Voix dynamique" -> 1.1f
            else -> 1f
        }
    )
    engine.speak(
        message,
        TextToSpeech.QUEUE_FLUSH,
        null,
        utteranceId
    )
}

private fun MainActivity.goalCard(minutes: Int): View {
    val selected = dailyGoalMinutes == minutes
    val (subtitle, icon, iconTint) = when (minutes) {
        10 -> Triple("Un petit pas chaque jour", "☕", blue)
        20 -> Triple("Un rythme tout en douceur", "▤", green)
        30 -> Triple("Idéal pour bien progresser", "ϟ", blue)
        45 -> Triple("Pour aller plus loin", "◉", Color.rgb(124, 58, 237))
        else -> Triple("Un vrai défi quotidien", "➤", Color.rgb(244, 63, 94))
    }
    val card = FrameLayout(this).apply {
        background = roundedBackground(
            if (selected) blue else Color.WHITE,
            if (selected) blue else border,
            20,
            dp(1)
        )
        elevation = dp(5).toFloat()
        isClickable = true
        isFocusable = true
        contentDescription =
            "$minutes minutes. $subtitle${if (selected) ", sélectionné" else ""}"
        setOnClickListener {
            dailyGoalMinutes = minutes
            render()
        }
    }
    val content = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        setPadding(dp(6), dp(10), dp(6), dp(8))
        isClickable = false
    }
    val iconBadge = TextView(this).apply {
        text = icon
        textSize = 18f
        setTextColor(if (selected) Color.WHITE else iconTint)
        gravity = Gravity.CENTER
        background = roundedBackground(
            if (selected) Color.rgb(68, 124, 241) else Color.rgb(239, 246, 255),
            if (selected) Color.rgb(68, 124, 241) else Color.rgb(239, 246, 255),
            12
        )
    }
    content.addView(iconBadge, LinearLayout.LayoutParams(dp(35), dp(35)).apply {
        bottomMargin = dp(7)
    })
    content.addView(TextView(this).apply {
        text = "$minutes minutes"
        textSize = 11f
        setTextColor(if (selected) Color.WHITE else ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        gravity = Gravity.CENTER
    })
    content.addView(TextView(this).apply {
        text = subtitle
        textSize = 8f
        setTextColor(if (selected) Color.rgb(224, 231, 255) else muted)
        gravity = Gravity.CENTER
        maxLines = 2
        setLineSpacing(0f, 0.94f)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(4)
    })
    card.addView(content, FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
    ))
    if (selected) {
        card.addView(TextView(this).apply {
            text = "✓"
            textSize = 8f
            setTextColor(blue)
            gravity = Gravity.CENTER
            background = roundedBackground(Color.WHITE, Color.WHITE, 20)
        }, FrameLayout.LayoutParams(dp(18), dp(18), Gravity.TOP or Gravity.END).apply {
            topMargin = dp(7)
            marginEnd = dp(7)
        })
    }
    return card
}

private fun MainActivity.summaryCard(): View {
    val card = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(15), dp(14), dp(15), dp(14))
        minimumHeight = dp(200)
        background = roundedBackground(Color.WHITE, border, 22)
    }
    val heading = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
    }
    heading.addView(TextView(this).apply {
        text = "♙"
        textSize = 17f
        setTextColor(blue)
        gravity = Gravity.CENTER
        background = roundedBackground(Color.rgb(239, 246, 255), Color.rgb(239, 246, 255), 10)
    }, LinearLayout.LayoutParams(dp(39), dp(39)).apply {
        marginEnd = dp(10)
    })
    val headingText = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    headingText.addView(TextView(this).apply {
        text = "RÉCAPITULATIF"
        textSize = 8f
        letterSpacing = 0.06f
        setTextColor(muted)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    })
    headingText.addView(TextView(this).apply {
        text = "Profil de l’élève"
        textSize = 11f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(2)
    })
    heading.addView(headingText, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    heading.addView(TextView(this).apply {
        text = "✿"
        textSize = 16f
        setTextColor(green)
        gravity = Gravity.CENTER
        contentDescription = "Profil récapitulé"
    })
    card.addView(heading)
    card.addView(View(this).apply {
        setBackgroundColor(Color.rgb(241, 245, 249))
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        height = dp(1)
        topMargin = dp(12)
        bottomMargin = dp(8)
    })

    val details = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    val firstLine = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
    }
    firstLine.addView(summaryField("Élève", learnerName.ifBlank { "—" }), weightedColumn())
    firstLine.addView(summaryField("Niveau", schoolLevel.ifBlank { "—" }), weightedColumn())
    details.addView(firstLine)
    val secondLine = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
    }
    secondLine.addView(summaryField("Enseignante", teacherName), weightedColumn())
    secondLine.addView(summaryField("Voix", teacherVoice), weightedColumn())
    details.addView(secondLine, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(6)
    })
    card.addView(details)

    val goalStrip = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(10), dp(8), dp(10), dp(8))
        minimumHeight = dp(40)
        background = roundedBackground(Color.rgb(239, 246, 255), Color.rgb(239, 246, 255), 10)
    }
    goalStrip.addView(TextView(this).apply {
        text = "◷"
        textSize = 13f
        setTextColor(blue)
    }, LinearLayout.LayoutParams(dp(20), ViewGroup.LayoutParams.WRAP_CONTENT))
    goalStrip.addView(TextView(this).apply {
        text = "Objectif quotidien"
        textSize = 9f
        setTextColor(blue)
    }, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
    goalStrip.addView(TextView(this).apply {
        text = "$dailyGoalMinutes min"
        textSize = 10f
        setTextColor(blue)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    })
    card.addView(goalStrip, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(8)
    })
    return card
}

private fun MainActivity.weightedColumn() = LinearLayout.LayoutParams(
    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
)

private fun MainActivity.summaryField(label: String, value: String): View {
    val field = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
    }
    field.addView(TextView(this).apply {
        text = label
        textSize = 7f
        setTextColor(muted)
    })
    field.addView(TextView(this).apply {
        text = value
        textSize = 9f
        setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
        maxLines = 1
        ellipsize = android.text.TextUtils.TruncateAt.END
    }, LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
    ).apply {
        topMargin = dp(2)
    })
    return field
}

private fun MainActivity.infoCard(message: String, icon: String): View {
    val row = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(14), dp(14), dp(14), dp(14))
        background = roundedBackground(Color.WHITE, border, 14)
    }
    row.addView(TextView(this).apply {
        text = icon
        textSize = 18f
        setTextColor(blue)
    }, LinearLayout.LayoutParams(dp(30), ViewGroup.LayoutParams.WRAP_CONTENT))
    row.addView(TextView(this).apply {
        text = message
        textSize = 12f
        setTextColor(muted)
    })
    return row
}

private fun MainActivity.radioMark(selected: Boolean): View = TextView(this).apply {
    text = if (selected) "✓" else ""
    textSize = 14f
    setTextColor(Color.WHITE)
    gravity = Gravity.CENTER
    background = roundedBackground(
        if (selected) blue else Color.TRANSPARENT,
        if (selected) blue else border,
        20,
        dp(1)
    )
    layoutParams = LinearLayout.LayoutParams(dp(22), dp(22))
}

private fun MainActivity.primaryButton(label: String, onClick: () -> Unit) = Button(this).apply {
    text = label
    textSize = 15f
    isAllCaps = false
    setTextColor(Color.WHITE)
    typeface = Typeface.create("sans-serif", Typeface.BOLD)
    background = roundedBackground(blue, blue, 18)
    elevation = dp(7).toFloat()
    setOnClickListener { onClick() }
}

private fun MainActivity.textButton(label: String, onClick: () -> Unit) = TextView(this).apply {
    text = label
    textSize = 13f
    setTextColor(muted)
    gravity = Gravity.CENTER
    isClickable = true
    isFocusable = true
    setOnClickListener { onClick() }
}
