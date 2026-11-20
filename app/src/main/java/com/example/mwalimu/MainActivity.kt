package com.example.mwalimu

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.mwalimu.R
import com.example.mwalimu.ui.chat.ChatScreen
import com.example.mwalimu.ui.home.teacherHomeScreen
import com.example.mwalimu.ui.onboarding.goalScreen
import com.example.mwalimu.ui.onboarding.profileScreen
import com.example.mwalimu.ui.onboarding.speakText
import com.example.mwalimu.ui.onboarding.teacherScreen
import com.example.mwalimu.ui.onboarding.welcomeScreen
import com.example.mwalimu.ui.programme.programmeScreen
import com.example.mwalimu.ui.evaluation.evaluationScreen
import com.example.mwalimu.ui.settings.settingsScreen
import com.example.mwalimu.ui.theme.Colors
import java.util.Locale

class MainActivity : AppCompatActivity() {
    internal var stage = STAGE_WELCOME
    internal var welcomePage = 0
    internal var learnerName = ""
    internal var schoolLevel = ""
    internal var teacherName = "Professeure Amina"
    internal var teacherVoice = "Voix chaleureuse"
    internal var dailyGoalMinutes = 30
    internal var settingsReturnStage = STAGE_HOME
    internal var textToSpeech: TextToSpeech? = null
    internal var textToSpeechReady = false

    internal val blue by lazy { Color.parseColor(Colors.PRIMARY) }
    internal val green by lazy { Color.parseColor(Colors.SECONDARY) }
    internal val amber by lazy { Color.parseColor(Colors.ACCENT) }
    internal val ink by lazy { Color.parseColor(Colors.INK) }
    internal val muted by lazy { Color.parseColor(Colors.MUTED) }
    internal val background by lazy { Color.parseColor(Colors.BACKGROUND) }
    internal val border by lazy { Color.parseColor(Colors.BORDER) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }

        savedInstanceState?.let {
            stage = it.getInt(KEY_STAGE, STAGE_WELCOME)
            settingsReturnStage = it.getInt(KEY_SETTINGS_RETURN_STAGE, STAGE_HOME)
            welcomePage = it.getInt(KEY_WELCOME_PAGE, 0)
            learnerName = it.getString(KEY_LEARNER_NAME).orEmpty()
            schoolLevel = it.getString(KEY_SCHOOL_LEVEL).orEmpty()
            teacherName = it.getString(KEY_TEACHER_NAME) ?: teacherName
            teacherVoice = it.getString(KEY_TEACHER_VOICE) ?: teacherVoice
            dailyGoalMinutes = it.getInt(KEY_DAILY_GOAL, dailyGoalMinutes)
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (stage) {
                    STAGE_PROFILE -> {
                        stage = STAGE_WELCOME
                        welcomePage = WELCOME_SLIDES.lastIndex
                        render()
                    }
                    STAGE_TEACHER -> {
                        stage = STAGE_PROFILE
                        render()
                    }
                    STAGE_GOAL -> {
                        stage = STAGE_TEACHER
                        render()
                    }
                    STAGE_CHAT -> {
                        stage = STAGE_HOME
                        render()
                    }
                    STAGE_PROGRAMME -> openTeacherHome()
                    STAGE_EVALUATION -> openTeacherHome()
                    STAGE_SETTINGS -> {
                        stage = settingsReturnStage
                        render()
                    }
                    else -> finish()
                }
            }
        })
        render()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_STAGE, stage)
        outState.putInt(KEY_SETTINGS_RETURN_STAGE, settingsReturnStage)
        outState.putInt(KEY_WELCOME_PAGE, welcomePage)
        outState.putString(KEY_LEARNER_NAME, learnerName)
        outState.putString(KEY_SCHOOL_LEVEL, schoolLevel)
        outState.putString(KEY_TEACHER_NAME, teacherName)
        outState.putString(KEY_TEACHER_VOICE, teacherVoice)
        outState.putInt(KEY_DAILY_GOAL, dailyGoalMinutes)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        super.onDestroy()
    }

    internal fun render() {
        val content = when (stage) {
            STAGE_WELCOME -> welcomeScreen()
            STAGE_PROFILE -> profileScreen()
            STAGE_TEACHER -> teacherScreen()
            STAGE_GOAL -> goalScreen()
            STAGE_HOME -> teacherHomeScreen()
            STAGE_CHAT -> ChatScreen(
                context = this,
                teacherName = teacherName,
                learnerName = learnerName,
                schoolLevel = schoolLevel,
                onBack = { openTeacherHome() },
                onOpenTeacher = { openTeacherHome() },
                onOpenProgramme = { openProgramme() },
                onOpenEvaluation = { openEvaluation() },
                onReadAnswer = { readTeacherAnswer(it) },
                onNotice = { showMessage(it) }
            ).createView()
            STAGE_PROGRAMME -> programmeScreen()
            STAGE_EVALUATION -> evaluationScreen()
            STAGE_SETTINGS -> settingsScreen()
            else -> throw IllegalStateException("Étape inconnue : $stage")
        }
        setContentView(content)
    }

    internal fun phoneScreen(root: View): View {
        val screen = FrameLayout(this).apply {
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(
                    Color.rgb(229, 237, 250),
                    Color.rgb(236, 246, 245)
                )
            )
            addView(root, FrameLayout.LayoutParams(dp(360), dp(750), Gravity.CENTER))
        }
        ViewCompat.setOnApplyWindowInsetsListener(screen) { view, windowInsets ->
            val systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            val bodyParams = root.layoutParams as FrameLayout.LayoutParams
            bodyParams.width = minOf(dp(360), view.width - systemBars.left - systemBars.right)
            bodyParams.height = minOf(dp(750), view.height - systemBars.top - systemBars.bottom)
            root.layoutParams = bodyParams
            windowInsets
        }
        ViewCompat.requestApplyInsets(screen)
        return screen
    }

    internal fun teacherWelcomeText() =
        "Bonjour ${learnerName.ifBlank { "élève" }} ! Je suis heureuse de vous retrouver aujourd’hui. Nous allons commencer votre prochaine leçon."

    internal fun playTeacherWelcome() {
        speakMessage(teacherWelcomeText(), "mwalimu-teacher-welcome")
    }

    private fun readTeacherAnswer(message: String) {
        speakMessage(message, "mwalimu-chat-answer")
    }

    private fun speakMessage(message: String, utteranceId: String) {
        if (textToSpeechReady) {
            speakText(message, utteranceId)
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
                showMessage("La lecture vocale n’est pas disponible.")
                return@TextToSpeech
            }
            if (engine.setLanguage(Locale.FRANCE) < TextToSpeech.LANG_AVAILABLE) {
                engine.shutdown()
                textToSpeech = null
                showMessage("La voix française n’est pas installée sur cet appareil.")
                return@TextToSpeech
            }
            textToSpeechReady = true
            speakText(message, utteranceId)
        }
    }

    internal fun openTeacherHome() {
        stage = STAGE_HOME
        render()
    }

    internal fun openChat() {
        stage = STAGE_CHAT
        render()
    }

    internal fun openProgramme() {
        stage = STAGE_PROGRAMME
        render()
    }

    internal fun openEvaluation() {
        stage = STAGE_EVALUATION
        render()
    }

    internal fun openSettings() {
        settingsReturnStage = stage
        stage = STAGE_SETTINGS
        render()
    }

    internal fun closeSettings() {
        stage = settingsReturnStage
        render()
    }

    internal fun showTeacherText() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(teacherName)
            .setMessage(teacherWelcomeText())
            .setNegativeButton("Fermer", null)
            .setPositiveButton("Écouter") { _, _ -> playTeacherWelcome() }
            .show()
    }

    internal fun setupRoot() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(this@MainActivity.background)
    }

    internal fun setupScroll() = ScrollView(this).apply {
        isFillViewport = true
        clipToPadding = false
    }

    internal fun roundedBackground(
        fill: Int,
        stroke: Int,
        radiusDp: Int,
        strokeWidthDp: Int = 1
    ) = GradientDrawable().apply {
        setColor(fill)
        cornerRadius = dp(radiusDp).toFloat()
        setStroke(dp(strokeWidthDp), stroke)
    }

    internal fun showMessage(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    internal fun dp(value: Int): Int =
        (value * resources.displayMetrics.density + 0.5f).toInt()

    internal fun swipeListener(): View.OnTouchListener = SwipeListener()

    private inner class SwipeListener : View.OnTouchListener {
        private var startX = 0f
        private var startY = 0f

        override fun onTouch(view: View, event: MotionEvent): Boolean {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = event.x
                    startY = event.y
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    val deltaX = event.x - startX
                    val deltaY = event.y - startY
                    if (kotlin.math.abs(deltaX) > dp(48) &&
                        kotlin.math.abs(deltaX) > kotlin.math.abs(deltaY)
                    ) {
                        welcomePage = if (deltaX < 0) {
                            (welcomePage + 1).coerceAtMost(WELCOME_SLIDES.lastIndex)
                        } else {
                            (welcomePage - 1).coerceAtLeast(0)
                        }
                        render()
                    }
                    return true
                }
                MotionEvent.ACTION_CANCEL -> return true
            }
            return true
        }
    }

    internal data class WelcomeSlide(
        val eyebrow: String,
        val title: String,
        val description: String
    )

    companion object {
        internal const val STAGE_WELCOME = 0
        internal const val STAGE_PROFILE = 1
        internal const val STAGE_TEACHER = 2
        internal const val STAGE_GOAL = 3
        internal const val STAGE_HOME = 4
        internal const val STAGE_CHAT = 5
        internal const val STAGE_PROGRAMME = 6
        internal const val STAGE_EVALUATION = 7
        internal const val STAGE_SETTINGS = 8

        private const val KEY_STAGE = "onboarding.stage"
        private const val KEY_SETTINGS_RETURN_STAGE = "settings.returnStage"
        private const val KEY_WELCOME_PAGE = "onboarding.welcomePage"
        private const val KEY_LEARNER_NAME = "onboarding.learnerName"
        private const val KEY_SCHOOL_LEVEL = "onboarding.schoolLevel"
        private const val KEY_TEACHER_NAME = "onboarding.teacherName"
        private const val KEY_TEACHER_VOICE = "onboarding.teacherVoice"
        private const val KEY_DAILY_GOAL = "onboarding.dailyGoal"

        internal val WELCOME_SLIDES = listOf(
            WelcomeSlide(
                "APPRENEZ PARTOUT",
                "Bienvenue sur\nMWALIMU",
                "Votre enseignant intelligent vous accompagne partout, même sans Internet."
            ),
            WelcomeSlide(
                "UNE QUESTION ?",
                "Posez toutes vos\nquestions",
                "Écrivez ou parlez naturellement. MWALIMU vous aide à comprendre chaque leçon."
            ),
            WelcomeSlide(
                "AVANCEZ À VOTRE RYTHME",
                "Progressez chaque jour",
                "Suivez vos cours, passez des évaluations et développez vos connaissances à votre rythme."
            )
        )

        internal val SCHOOL_LEVELS = listOf(
            "Sélectionnez votre niveau",
            "7e année",
            "8e année",
            "9e année",
            "10e année",
            "1ère année",
            "2ème année",
            "3ème année",
            "Université"
        )

        internal val VOICES = listOf("Voix posée", "Voix chaleureuse", "Voix dynamique")
        internal val GOALS = listOf(10, 20, 30, 45, 60)
    }
}
