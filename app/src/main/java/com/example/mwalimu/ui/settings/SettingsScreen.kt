package com.example.mwalimu.ui.settings

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.mwalimu.MainActivity
import com.example.mwalimu.ui.components.HomeIcon
import com.example.mwalimu.ui.components.HomeIconView

internal fun MainActivity.settingsScreen(): View {
    val root = setupRoot()
    val header = LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(16), dp(4), dp(16), dp(4))
    }
    val back = FrameLayout(this).apply {
        background = roundedBackground(Color.WHITE, border, 12); isClickable = true; isFocusable = true
        contentDescription = "Retour"; setOnClickListener { closeSettings() }
        addView(HomeIconView(this@settingsScreen, HomeIcon.BACK, ink), FrameLayout.LayoutParams(dp(19), dp(19), Gravity.CENTER))
    }
    header.addView(back, LinearLayout.LayoutParams(dp(38), dp(38)))
    header.addView(TextView(this).apply {
        text = "Paramètres"; textSize = 19f; setTextColor(ink)
        typeface = Typeface.create("sans-serif", Typeface.BOLD); includeFontPadding = false
    }, LinearLayout.LayoutParams(0, dp(48), 1f).apply { marginStart = dp(12); gravity = Gravity.CENTER_VERTICAL })
    root.addView(header)

    val scroll = ScrollView(this).apply { isVerticalScrollBarEnabled = false; clipToPadding = false }
    val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(16), dp(8), dp(16), dp(20)) }
    content.addView(TextView(this).apply {
        text = "Personnalisez votre expérience d’apprentissage."; textSize = 10f; setTextColor(muted)
    })

    content.addView(settingsSection("MON PROFIL"), sectionParams(18))
    val nameValue = valueText(learnerName.ifBlank { "Ajouter votre nom" })
    val nameRow = settingsRow("Nom de l’élève", nameValue, "›") {
        val input = EditText(this).apply { setText(learnerName); hint = "Votre nom"; setSingleLine(true) }
        AlertDialog.Builder(this).setTitle("Nom de l’élève").setView(input)
            .setNegativeButton("Annuler", null)
            .setPositiveButton("Enregistrer") { _, _ ->
                learnerName = input.text.toString().trim()
                nameValue.text = learnerName.ifBlank { "Ajouter votre nom" }
            }.show()
    }
    val levelValue = valueText(schoolLevel.ifBlank { "Choisir un niveau" })
    val levelRow = settingsRow("Niveau scolaire", levelValue, "›") {
        val levels = MainActivity.SCHOOL_LEVELS.drop(1).toTypedArray()
        AlertDialog.Builder(this).setTitle("Niveau scolaire").setItems(levels) { _, index ->
            schoolLevel = levels[index]
            levelValue.text = schoolLevel
        }.show()
    }
    content.addView(settingsCard(listOf(nameRow, levelRow)))

    content.addView(settingsSection("APPRENTISSAGE"), sectionParams(18))
    val goalValue = valueText("${dailyGoalMinutes} min par jour")
    val goalChoices = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER }
    MainActivity.GOALS.forEach { minutes ->
        val selected = minutes == dailyGoalMinutes
        val chip = TextView(this).apply {
            text = "$minutes min"; textSize = 8f; gravity = Gravity.CENTER
            setTextColor(if (selected) Color.WHITE else muted)
            background = roundedBackground(if (selected) blue else Color.WHITE, if (selected) blue else border, 18)
            isClickable = true; isFocusable = true
            setOnClickListener {
                dailyGoalMinutes = minutes
                goalValue.text = "$minutes min par jour"
                for (i in 0 until goalChoices.childCount) {
                    val choice = goalChoices.getChildAt(i) as TextView
                    val active = choice.text == "$minutes min"
                    choice.setTextColor(if (active) Color.WHITE else muted)
                    choice.background = roundedBackground(if (active) blue else Color.WHITE, if (active) blue else border, 18)
                }
            }
        }
        goalChoices.addView(chip, LinearLayout.LayoutParams(0, dp(32), 1f).apply { if (goalChoices.childCount > 0) marginStart = dp(5) })
    }
    val goalCard = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(12), dp(12), dp(12), dp(12))
        background = roundedBackground(Color.WHITE, border, 16)
    }
    goalCard.addView(TextView(this).apply { text = "Objectif quotidien"; textSize = 11f; setTextColor(ink); typeface = Typeface.DEFAULT_BOLD })
    goalCard.addView(goalValue, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(3) })
    goalCard.addView(goalChoices, LinearLayout.LayoutParams(-1, dp(32)).apply { topMargin = dp(10) })
    content.addView(goalCard)

    content.addView(settingsSection("VOTRE PROFESSEUR"), sectionParams(18))
    val teacherValue = valueText(teacherName)
    val teacherRow = settingsRow("Professeur", teacherValue, "›") {
        val teachers = arrayOf("Professeure Amina", "Professeur Jean", "Professeure Marie")
        AlertDialog.Builder(this).setTitle("Choisir un professeur").setItems(teachers) { _, index ->
            teacherName = teachers[index]
            teacherValue.text = teacherName
        }.show()
    }
    val voiceValue = valueText(teacherVoice)
    val voiceRow = settingsRow("Voix du professeur", voiceValue, "›") {
        val voices = MainActivity.VOICES.toTypedArray()
        AlertDialog.Builder(this).setTitle("Voix du professeur").setItems(voices) { _, index ->
            teacherVoice = voices[index]
            voiceValue.text = teacherVoice
        }.show()
    }
    content.addView(settingsCard(listOf(teacherRow, voiceRow)))

    content.addView(settingsSection("PRÉFÉRENCES"), sectionParams(18))
    val reminders = Switch(this).apply {
        text = "Rappels d’apprentissage"; textSize = 10f; setTextColor(ink)
        isChecked = true; buttonTintList = android.content.res.ColorStateList.valueOf(blue)
    }
    val reminderCard = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL; setPadding(dp(12), dp(4), dp(10), dp(4))
        background = roundedBackground(Color.WHITE, border, 16)
        addView(reminders, LinearLayout.LayoutParams(-1, dp(48)))
        addView(TextView(this@settingsScreen).apply {
            text = "Recevez un rappel pour garder votre rythme."; textSize = 8f; setTextColor(muted)
        }, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(9) })
    }
    content.addView(reminderCard)

    content.addView(TextView(this).apply {
        text = "MWALIMU · Version 1.0"; textSize = 8f; setTextColor(Color.rgb(148,163,184)); gravity = Gravity.CENTER
    }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(22) })
    scroll.addView(content)
    root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
    return phoneScreen(root)
}

private fun MainActivity.settingsSection(title: String) = TextView(this).apply {
    text = title; textSize = 8f; letterSpacing = .1f; setTextColor(muted); typeface = Typeface.DEFAULT_BOLD
}

private fun MainActivity.valueText(value: String) = TextView(this).apply {
    text = value; textSize = 8f; setTextColor(muted); gravity = Gravity.END or Gravity.CENTER_VERTICAL
    maxLines = 1; ellipsize = android.text.TextUtils.TruncateAt.END
}

private fun MainActivity.settingsRow(label: String, value: TextView, arrow: String, onClick: () -> Unit): View =
    LinearLayout(this).apply {
        orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
        setPadding(dp(12), dp(10), dp(12), dp(10)); isClickable = true; isFocusable = true
        setOnClickListener { onClick() }
        addView(TextView(this@settingsRow).apply { text = label; textSize = 10f; setTextColor(ink) }, LinearLayout.LayoutParams(0, -2, 1f))
        addView(value, LinearLayout.LayoutParams(0, dp(32), .9f))
        addView(TextView(this@settingsRow).apply { text = arrow; textSize = 20f; setTextColor(Color.rgb(148,163,184)); gravity = Gravity.CENTER }, LinearLayout.LayoutParams(dp(18), dp(32)))
    }

private fun MainActivity.settingsCard(rows: List<View>): View = LinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL; background = roundedBackground(Color.WHITE, border, 16)
    rows.forEachIndexed { index, row ->
        addView(row, LinearLayout.LayoutParams(-1, -2))
        if (index < rows.lastIndex) addView(View(this@settingsCard).apply { setBackgroundColor(Color.rgb(241,245,249)) }, LinearLayout.LayoutParams(-1, dp(1)).apply { marginStart = dp(12); marginEnd = dp(12) })
    }
}

private fun MainActivity.sectionParams(top: Int) = LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(top); bottomMargin = dp(8) }
