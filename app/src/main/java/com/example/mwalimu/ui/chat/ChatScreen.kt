package com.example.mwalimu.ui.chat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.text.InputType
import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mwalimu.ui.theme.Colors
import com.example.mwalimu.ui.components.HomeIcon
import com.example.mwalimu.ui.components.HomeIconView

internal class ChatScreen(
    private val context: Context,
    private val teacherName: String,
    private val learnerName: String,
    private val schoolLevel: String,
    private val onBack: () -> Unit,
    private val onOpenTeacher: () -> Unit,
    private val onOpenProgramme: () -> Unit,
    private val onOpenEvaluation: () -> Unit,
    private val onReadAnswer: (String) -> Unit,
    private val onNotice: (String) -> Unit
) {
    private val blue = Color.parseColor(Colors.PRIMARY)
    private val green = Color.parseColor(Colors.SECONDARY)
    private val amber = Color.parseColor(Colors.ACCENT)
    private val ink = Color.parseColor(Colors.INK)
    private val muted = Color.parseColor(Colors.MUTED)
    private val border = Color.parseColor(Colors.BORDER)
    private val surface = Color.WHITE
    private val answerText =
        "Bien sûr, ${learnerName.ifBlank { "élève" }}. Dans un triangle rectangle, le carré de l’hypoténuse est égal à la somme des carrés des deux autres côtés."
    private lateinit var messages: LinearLayout

    fun createView(): View {
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.parseColor(Colors.BACKGROUND))
            setPadding(0, dp(4), 0, dp(8))
        }
        content.addView(header(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(42)
        ))
        content.addView(teacherCard(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(56)
        ).apply {
            setMargins(dp(12), dp(6), dp(12), dp(6))
        })

        val conversation = ScrollView(context).apply {
            isFillViewport = false
            isVerticalScrollBarEnabled = false
            clipToPadding = false
        }
        messages = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(6), dp(12), dp(8))
        }
        addInitialConversation()
        conversation.addView(messages)
        content.addView(conversation, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        content.addView(composer(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(56)
        ).apply {
            setMargins(dp(12), dp(3), dp(12), dp(8))
        })
        content.addView(bottomNavigation(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(64)
        ).apply {
            setMargins(dp(12), 0, dp(12), dp(2))
        })

        val phone = FrameLayout(context).apply {
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.rgb(229, 237, 250), Color.rgb(236, 246, 245))
            )
            addView(content, FrameLayout.LayoutParams(dp(360), dp(750), Gravity.CENTER))
        }
        ViewCompat.setOnApplyWindowInsetsListener(phone) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom)
            val contentParams = content.layoutParams as FrameLayout.LayoutParams
            contentParams.width = minOf(dp(360), view.width - insets.left - insets.right)
            contentParams.height = minOf(dp(750), view.height - insets.top - insets.bottom)
            content.layoutParams = contentParams
            windowInsets
        }
        ViewCompat.requestApplyInsets(phone)
        return phone
    }

    private fun header(): View {
        val header = FrameLayout(context)
        val title = TextView(context).apply {
            text = "Chat"
            textSize = 17f
            setTextColor(ink)
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
            includeFontPadding = false
        }
        header.addView(title, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ))
        header.addView(iconButton(HomeIcon.BACK, "Retour", onBack),
            FrameLayout.LayoutParams(dp(36), dp(36), Gravity.START or Gravity.CENTER_VERTICAL).apply {
                marginStart = dp(12)
            })
        val actions = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        actions.addView(iconButton(HomeIcon.SEARCH, "Rechercher") { showSearch() },
            LinearLayout.LayoutParams(dp(36), dp(36)))
        actions.addView(iconButton(HomeIcon.SETTINGS, "Options") { showOptions() },
            LinearLayout.LayoutParams(dp(36), dp(36)).apply {
                marginStart = dp(6)
            })
        header.addView(actions, FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            Gravity.END or Gravity.CENTER_VERTICAL
        ).apply {
            marginEnd = dp(12)
        })
        return header
    }

    private fun teacherCard(): View {
        val card = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(9), dp(6), dp(9), dp(6))
            background = rounded(surface, surface, 18)
            elevation = dp(3).toFloat()
        }
        card.addView(TeacherAvatar(context), LinearLayout.LayoutParams(dp(38), dp(38)))
        val identity = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
        }
        identity.addView(label(teacherName, 11f, ink, true), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        val online = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        online.addView(View(context).apply {
            background = oval(green)
        }, LinearLayout.LayoutParams(dp(5), dp(5)).apply {
            marginEnd = dp(5)
        })
        online.addView(label("Prête à vous aider", 8f, muted))
        identity.addView(online, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(3)
        })
        card.addView(identity, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
            .apply {
                marginStart = dp(8)
            })
        val status = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(7), dp(5), dp(7), dp(5))
            background = rounded(Color.rgb(236, 253, 245), Color.rgb(236, 253, 245), 12)
        }
        status.addView(HomeIconView(context, HomeIcon.SPEAKER, green),
            LinearLayout.LayoutParams(dp(11), dp(11)).apply {
                marginEnd = dp(4)
            })
        status.addView(label("En ligne", 7f, green, true))
        card.addView(status)
        return card
    }

    private fun addInitialConversation() {
        val date = label("Aujourd’hui", 7f, muted).apply {
            gravity = Gravity.CENTER
            setPadding(dp(8), dp(4), dp(8), dp(4))
            background = rounded(Color.rgb(241, 245, 249), Color.rgb(241, 245, 249), 10)
        }
        messages.addView(date, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            bottomMargin = dp(12)
        })
        messages.addView(userMessage(
            "Pouvez-vous m’expliquer le théorème de Pythagore simplement ?",
            "10:24"
        ), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dp(10)
        })

        val response = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.TOP
        }
        val assistantIcon = FrameLayout(context).apply {
            background = rounded(blue, blue, 10)
            addView(HomeIconView(context, HomeIcon.TEACHER, Color.WHITE),
                FrameLayout.LayoutParams(dp(17), dp(17), Gravity.CENTER))
        }
        response.addView(assistantIcon, LinearLayout.LayoutParams(dp(27), dp(27)).apply {
            topMargin = dp(9)
        })
        val responseContent = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        responseContent.addView(label(teacherName, 8f, muted, true), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = dp(5)
        })
        responseContent.addView(answerCard())
        response.addView(responseContent, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = dp(7)
            })
        messages.addView(response, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        messages.addView(responseActions(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dp(30)
        ).apply {
            topMargin = dp(6)
            bottomMargin = dp(6)
        })
    }

    private fun userMessage(message: String, time: String?): View {
        val row = FrameLayout(context)
        val bubble = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), dp(9), dp(11), dp(7))
            background = bubbleBackground(blue, 18)
            elevation = dp(3).toFloat()
        }
        bubble.addView(label(message, 10f, Color.WHITE), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ))
        if (time != null) {
            val footer = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }
            footer.addView(label(time, 6f, Color.rgb(219, 234, 254)))
            footer.addView(label("  ✓✓", 6f, Color.rgb(219, 234, 254)))
            bubble.addView(footer, LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(4)
            })
        }
        row.addView(bubble, FrameLayout.LayoutParams(dp(236), ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.END))
        return row
    }

    private fun answerCard(): View {
        val card = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(11), dp(10), dp(11), dp(10))
            background = rounded(surface, border, 17)
            elevation = dp(2).toFloat()
        }
        card.addView(label(answerText, 9f, muted).apply {
            setLineSpacing(dp(2).toFloat(), 1f)
        })

        val explanation = TextView(context).apply {
            text = highlightedText(
                "Le côté le plus long s’appelle l’hypoténuse.",
                "l’hypoténuse",
                Color.rgb(239, 246, 255),
                blue
            )
            textSize = 8f
            setTextColor(muted)
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            includeFontPadding = false
            setPadding(dp(8), dp(7), dp(3), dp(1))
        }
        card.addView(explanation, LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(3)
        })

        val formula = TextView(context).apply {
            text = highlightedText(
                "La formule est a² + b² = c².",
                "a² + b² = c²",
                Color.rgb(239, 246, 255),
                blue,
                true
            )
            textSize = 8f
            setTextColor(muted)
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            includeFontPadding = false
            setPadding(dp(8), dp(3), dp(3), dp(7))
        }
        card.addView(formula)
        card.addView(exampleCard(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(1)
        })
        card.addView(sourceCard(), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(7)
        })
        return card
    }

    private fun exampleCard(): View {
        val example = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(8), dp(7), dp(8), dp(7))
            background = rounded(Color.rgb(255, 251, 235), Color.rgb(253, 230, 138), 12)
        }
        val icon = TextView(context).apply {
            text = "✦"
            textSize = 14f
            setTextColor(amber)
            gravity = Gravity.CENTER
            background = rounded(Color.rgb(254, 243, 199), Color.rgb(254, 243, 199), 8)
        }
        example.addView(icon, LinearLayout.LayoutParams(dp(24), dp(24)))
        val details = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        details.addView(label("Exemple rapide", 7f, Color.rgb(146, 64, 14), true))
        details.addView(label("Si a = 3 et b = 4, alors c = 5.", 7f, muted),
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(3)
            })
        example.addView(details, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = dp(7)
            })
        return example
    }

    private fun sourceCard(): View {
        val source = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(7), dp(6), dp(7), dp(6))
            background = rounded(Color.rgb(248, 250, 252), Color.rgb(241, 245, 249), 12)
        }
        val book = FrameLayout(context).apply {
            background = rounded(Color.rgb(219, 234, 254), Color.rgb(219, 234, 254), 8)
            addView(HomeIconView(context, HomeIcon.BOOK, blue),
                FrameLayout.LayoutParams(dp(16), dp(16), Gravity.CENTER))
        }
        source.addView(book, LinearLayout.LayoutParams(dp(26), dp(26)))
        val sourceText = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }
        sourceText.addView(label("SOURCE", 6f, Color.rgb(148, 163, 184), true))
        val level = schoolLevel.ifBlank { "9e année" }
        sourceText.addView(label("Mathématiques · $level", 7f, ink, true),
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(2)
            })
        sourceText.addView(label("Chapitre 5 · Page 42", 6f, muted),
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = dp(2)
            })
        source.addView(sourceText, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            .apply {
                marginStart = dp(7)
            })
        val verified = FrameLayout(context).apply {
            background = rounded(Color.rgb(220, 252, 231), Color.rgb(220, 252, 231), 8)
            addView(label("✓", 11f, green, true).apply {
                gravity = Gravity.CENTER
            }, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        }
        source.addView(verified, LinearLayout.LayoutParams(dp(25), dp(25)))
        return source
    }

    private fun responseActions(): View {
        val row = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        val leftActions = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        leftActions.addView(smallAction(HomeIcon.COPY, "Copier") {
            val clipboard = context.getSystemService(ClipboardManager::class.java)
            if (clipboard == null) {
                onNotice("Le presse-papiers n’est pas disponible.")
            } else {
                clipboard.setPrimaryClip(ClipData.newPlainText("Réponse", answerText))
                onNotice("Réponse copiée.")
            }
        })
        leftActions.addView(smallAction(HomeIcon.SPEAKER, "Écouter") {
            onReadAnswer(answerText)
        }, spacedAction())
        leftActions.addView(smallAction(HomeIcon.REFRESH, "Réessayer") {
            onNotice("La génération de réponses n’est pas encore connectée.")
        }, spacedAction())
        row.addView(leftActions, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))

        val feedback = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        feedback.addView(smallAction(HomeIcon.THUMBS_UP, "Bonne réponse") {
            onNotice("Merci pour votre retour.")
        })
        feedback.addView(smallAction(HomeIcon.THUMBS_DOWN, "Mauvaise réponse") {
            onNotice("Merci pour votre retour.")
        }, spacedAction())
        row.addView(feedback)
        return row
    }

    private fun composer(): View {
        val bar = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(5), dp(5), dp(5), dp(5))
            background = rounded(surface, surface, 22)
            elevation = dp(3).toFloat()
        }
        bar.addView(iconButton(HomeIcon.MICROPHONE, "Saisie vocale") {
            onNotice("La saisie vocale n’est pas encore configurée.")
        }, LinearLayout.LayoutParams(dp(36), dp(36)).apply {
            marginEnd = dp(4)
        })
        val input = EditText(context).apply {
            hint = "Posez une question à votre professeur..."
            textSize = 9f
            setTextColor(ink)
            setHintTextColor(Color.rgb(148, 163, 184))
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            includeFontPadding = false
            background = null
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            imeOptions = EditorInfo.IME_ACTION_SEND
            maxLines = 1
            setPadding(dp(3), 0, dp(3), 0)
        }
        bar.addView(input, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f))
        val send = iconButton(HomeIcon.SEND, "Envoyer") {
            sendMessage(input)
        }.apply {
            background = rounded(Color.rgb(226, 232, 240), Color.rgb(226, 232, 240), 12)
            isEnabled = false
            alpha = 0.75f
        }
        bar.addView(send, LinearLayout.LayoutParams(dp(36), dp(36)).apply {
            marginStart = dp(4)
        })
        input.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val enabled = !s.isNullOrBlank()
                send.isEnabled = enabled
                send.alpha = if (enabled) 1f else 0.75f
                send.background = rounded(
                    if (enabled) blue else Color.rgb(226, 232, 240),
                    if (enabled) blue else Color.rgb(226, 232, 240),
                    12
                )
                val icon = send.getChildAt(0) as? HomeIconView
                icon?.setTint(if (enabled) Color.WHITE else Color.rgb(148, 163, 184))
            }
            override fun afterTextChanged(s: android.text.Editable?) = Unit
        })
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage(input)
                true
            } else {
                false
            }
        }
        return bar
    }

    private fun sendMessage(input: EditText) {
        val message = input.text.toString().trim()
        if (message.isEmpty()) return
        messages.addView(userMessage(message, null), LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = dp(10)
        })
        messages.post { (messages.parent as? ScrollView)?.fullScroll(View.FOCUS_DOWN) }
        input.text.clear()
        onNotice("Message affiché dans cette maquette. La connexion à l’assistant reste à intégrer.")
    }

    private fun bottomNavigation(): View {
        val navigation = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            setPadding(dp(5), dp(5), dp(5), dp(5))
            background = rounded(surface, surface, 22)
            elevation = dp(4).toFloat()
        }
        val items = listOf(
            Triple(HomeIcon.TEACHER, "Professeur", false),
            Triple(HomeIcon.CHAT, "Chat", true),
            Triple(HomeIcon.CALENDAR, "Programme", false),
            Triple(HomeIcon.EVALUATION, "Évaluation", false)
        )
        items.forEach { (icon, title, selected) ->
            val item = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                background = if (selected) rounded(blue, blue, 15) else null
                isClickable = true
                isFocusable = true
                contentDescription = title
                setOnClickListener {
                    if (title == "Professeur") onOpenTeacher()
                    else if (title == "Programme") onOpenProgramme()
                    else if (title == "Évaluation") onOpenEvaluation()
                    else if (!selected) onNotice("$title sera disponible prochainement.")
                }
            }
            item.addView(HomeIconView(
                context,
                icon,
                if (selected) Color.WHITE else Color.rgb(148, 163, 184)
            ), LinearLayout.LayoutParams(dp(18), dp(18)))
            item.addView(label(title, 7f, if (selected) Color.WHITE else muted, selected),
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = dp(3)
                })
            navigation.addView(item, LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f))
        }
        return navigation
    }

    private fun iconButton(icon: HomeIcon, description: String, action: () -> Unit): FrameLayout {
        val button = FrameLayout(context).apply {
            background = rounded(surface, surface, 12)
            elevation = dp(2).toFloat()
            contentDescription = description
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
        }
        button.addView(HomeIconView(context, icon, muted), FrameLayout.LayoutParams(
            dp(18), dp(18), Gravity.CENTER
        ))
        return button
    }

    private fun smallAction(icon: HomeIcon, description: String, action: () -> Unit) =
        FrameLayout(context).apply {
            background = rounded(surface, Color.rgb(241, 245, 249), 9)
            contentDescription = description
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
            addView(HomeIconView(context, icon, muted), FrameLayout.LayoutParams(
                dp(14), dp(14), Gravity.CENTER
            ))
            layoutParams = ViewGroup.LayoutParams(dp(27), dp(27))
        }

    private fun spacedAction() = LinearLayout.LayoutParams(dp(27), dp(27)).apply {
        marginStart = dp(4)
    }

    private fun showSearch() {
        val query = EditText(context).apply {
            hint = "Mot ou expression"
            textSize = 14f
            setSingleLine(true)
        }
        AlertDialog.Builder(context)
            .setTitle("Rechercher dans la conversation")
            .setView(query)
            .setNegativeButton("Fermer", null)
            .setPositiveButton("Rechercher") { _, _ ->
                val term = query.text.toString().trim()
                if (term.isEmpty()) return@setPositiveButton
                val found = "$answerText Pouvez-vous m’expliquer le théorème de Pythagore simplement ?"
                    .contains(term, ignoreCase = true)
                onNotice(if (found) "Expression trouvée dans la conversation."
                    else "Aucun résultat dans cette conversation.")
            }
            .show()
    }

    private fun showOptions() {
        AlertDialog.Builder(context)
            .setTitle("Options de conversation")
            .setMessage("Les paramètres du chat seront disponibles prochainement.")
            .setPositiveButton("Fermer", null)
            .show()
    }

    private fun label(
        text: CharSequence,
        size: Float,
        color: Int,
        bold: Boolean = false
    ) = TextView(context).apply {
        this.text = text
        textSize = size
        setTextColor(color)
        typeface = Typeface.create("sans-serif", if (bold) Typeface.BOLD else Typeface.NORMAL)
        includeFontPadding = false
        setLineSpacing(dp(2).toFloat(), 1f)
    }

    private fun highlightedText(
        text: String,
        highlight: String,
        fill: Int,
        foreground: Int,
        bold: Boolean = false
    ): SpannableString {
        val result = SpannableString(text)
        val start = text.indexOf(highlight)
        if (start >= 0) {
            val end = start + highlight.length
            result.setSpan(BackgroundColorSpan(fill), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            result.setSpan(ForegroundColorSpan(foreground), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (bold) {
                result.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return result
    }

    private fun bubbleBackground(fill: Int, radius: Int) = GradientDrawable().apply {
        setColor(fill)
        setCornerRadii(floatArrayOf(
            dp(radius).toFloat(), dp(radius).toFloat(),
            dp(radius).toFloat(), dp(radius).toFloat(),
            dp(radius).toFloat(), dp(radius).toFloat(),
            dp(5).toFloat(), dp(5).toFloat()
        ))
    }

    private fun rounded(fill: Int, stroke: Int, radius: Int) = GradientDrawable().apply {
        setColor(fill)
        cornerRadius = dp(radius).toFloat()
        setStroke(dp(1), stroke)
    }

    private fun oval(fill: Int) = GradientDrawable().apply {
        setColor(fill)
        shape = GradientDrawable.OVAL
    }

    private fun dp(value: Int): Int =
        (value * context.resources.displayMetrics.density + 0.5f).toInt()

    private class TeacherAvatar(context: Context) : View(context) {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val scale = minOf(width / 40f, height / 40f)
            if (scale <= 0f) return
            canvas.save()
            canvas.translate((width - scale * 40f) / 2f, (height - scale * 40f) / 2f)
            canvas.scale(scale, scale)
            paint.color = Color.rgb(219, 234, 254)
            canvas.drawCircle(20f, 20f, 20f, paint)
            paint.color = Color.rgb(37, 99, 235)
            canvas.drawRoundRect(RectF(6f, 24f, 34f, 42f), 9f, 9f, paint)
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawOval(RectF(8f, 5f, 32f, 29f), paint)
            paint.color = Color.rgb(135, 73, 38)
            canvas.drawOval(RectF(11f, 9f, 29f, 28f), paint)
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawCircle(16f, 18f, 1f, paint)
            canvas.drawCircle(24f, 18f, 1f, paint)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawArc(RectF(16f, 19f, 24f, 25f), 5f, 170f, false, paint)
            paint.style = Paint.Style.FILL
            canvas.restore()
        }
    }
}
