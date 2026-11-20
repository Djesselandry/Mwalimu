package com.example.mwalimu.ui.onboarding

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.example.mwalimu.ui.theme.Colors

internal class OnboardingIllustrationView(context: Context) : View(context) {
    var scene: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val blue = Color.parseColor(Colors.PRIMARY)
    private val green = Color.parseColor(Colors.SECONDARY)
    private val amber = Color.parseColor(Colors.ACCENT)
    private val ink = Color.parseColor(Colors.INK)
    private val white = Color.WHITE

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scale = minOf(width / 320f, height / 270f)
        if (scale <= 0f) return
        canvas.save()
        canvas.translate((width - 320f * scale) / 2f, (height - 270f * scale) / 2f)
        canvas.scale(scale, scale)
        when (scene) {
            1 -> drawQuestions(canvas)
            2 -> drawProgress(canvas)
            3 -> drawProfile(canvas)
            5 -> drawTeacher(canvas)
            else -> drawClassroom(canvas)
        }
        canvas.restore()
    }

    private fun drawTeacher(canvas: Canvas) {
        circle(canvas, 160f, 138f, 105f, Color.rgb(239, 246, 255))
        paint.color = Color.rgb(191, 219, 254)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1.5f
        canvas.drawCircle(160f, 138f, 91f, paint)
        paint.style = Paint.Style.FILL

        circle(canvas, 160f, 108f, 48f, ink)
        roundRect(canvas, 87f, 107f, 233f, 240f, 46f, white)
        roundRect(canvas, 92f, 112f, 228f, 235f, 43f, blue)
        circle(canvas, 160f, 106f, 37f, Color.rgb(135, 73, 38))

        paint.color = ink
        canvas.drawOval(RectF(123f, 48f, 197f, 110f), paint)
        roundRect(canvas, 123f, 75f, 144f, 114f, 10f, ink)
        roundRect(canvas, 176f, 75f, 197f, 114f, 10f, ink)

        circle(canvas, 148f, 106f, 3f, ink)
        circle(canvas, 172f, 106f, 3f, ink)
        text(canvas, "⌣", 160f, 127f, 20f, white, false, true)

        paint.color = white
        val collar = android.graphics.Path().apply {
            moveTo(143f, 151f)
            lineTo(160f, 174f)
            lineTo(177f, 151f)
            lineTo(168f, 145f)
            lineTo(160f, 158f)
            lineTo(152f, 145f)
            close()
        }
        canvas.drawPath(collar, paint)
        text(canvas, "◇", 160f, 203f, 24f, white, true, true)

        badge(canvas, 45f, 104f, "♧", green)
        badge(canvas, 275f, 118f, "✦", amber)
    }

    private fun drawClassroom(canvas: Canvas) {
        circle(canvas, 160f, 142f, 114f, Color.rgb(219, 234, 254))
        circle(canvas, 242f, 164f, 64f, Color.rgb(220, 252, 231))
        roundRect(canvas, 62f, 22f, 258f, 80f, 15f, Color.rgb(15, 91, 112))
        roundRect(canvas, 62f, 22f, 258f, 80f, 15f, amber, 4f)
        text(canvas, "▦    A + B = C     △", 160f, 57f, 15f, white, true, true)
        roundRect(canvas, 42f, 187f, 110f, 197f, 4f, amber)
        roundRect(canvas, 48f, 198f, 122f, 208f, 4f, green)
        roundRect(canvas, 37f, 209f, 132f, 219f, 4f, blue)
        drawStudent(canvas, 90f, 102f, Color.rgb(249, 115, 22))
        drawRobot(canvas, 208f, 104f)
        badge(canvas, 38f, 92f, "✧", amber)
        badge(canvas, 279f, 100f, "Aa", blue)
        roundRect(canvas, 113f, 226f, 207f, 232f, 3f, blue)
    }

    private fun drawQuestions(canvas: Canvas) {
        circle(canvas, 160f, 148f, 111f, Color.rgb(224, 231, 255))
        roundRect(canvas, 12f, 35f, 167f, 82f, 16f, white)
        text(canvas, "Comment ça marche ?", 87f, 63f, 11f, blue, true, true)
        circle(canvas, 145f, 61f, 5f, amber)
        roundRect(canvas, 177f, 60f, 308f, 104f, 16f, white)
        text(canvas, "Je vais t’expliquer !", 239f, 87f, 10f, blue, true, true)
        drawStudent(canvas, 94f, 92f, Color.rgb(124, 58, 237))
        drawRobot(canvas, 217f, 95f)
    }

    private fun drawProgress(canvas: Canvas) {
        circle(canvas, 155f, 148f, 115f, Color.rgb(220, 252, 231))
        roundRect(canvas, 28f, 28f, 218f, 145f, 18f, white)
        text(canvas, "Progression", 78f, 54f, 12f, ink, true, true)
        text(canvas, "84%", 183f, 54f, 14f, green, true, true)
        val barBottom = 122f
        val heights = floatArrayOf(24f, 38f, 50f, 65f, 82f)
        val colors = intArrayOf(
            Color.rgb(191, 219, 254),
            Color.rgb(147, 197, 253),
            Color.rgb(96, 165, 250),
            Color.rgb(59, 130, 246),
            green
        )
        heights.forEachIndexed { index, height ->
            roundRect(
                canvas,
                47f + index * 28f,
                barBottom - height,
                65f + index * 28f,
                barBottom,
                5f,
                colors[index]
            )
        }
        text(canvas, "✓  Quiz terminé", 83f, 137f, 10f, green, true, true)
        drawStudent(canvas, 213f, 88f, green)
        badge(canvas, 258f, 45f, "★", amber)
        roundRect(canvas, 25f, 184f, 102f, 226f, 16f, white)
        text(canvas, "🔥  7 jours", 64f, 210f, 10f, amber, true, true)
    }

    private fun drawProfile(canvas: Canvas) {
        circle(canvas, 160f, 142f, 103f, Color.rgb(219, 234, 254))
        circle(canvas, 225f, 159f, 60f, Color.rgb(209, 250, 229))
        roundRect(canvas, 105f, 75f, 215f, 228f, 40f, blue)
        circle(canvas, 160f, 99f, 36f, Color.rgb(135, 73, 38))
        roundRect(canvas, 123f, 58f, 197f, 91f, 20f, ink)
        circle(canvas, 148f, 101f, 3f, ink)
        circle(canvas, 172f, 101f, 3f, ink)
        text(canvas, "⌣", 160f, 118f, 19f, white, false, true)
        text(canvas, "▤", 160f, 184f, 34f, white, true, true)
        badge(canvas, 52f, 110f, "▤", green)
        badge(canvas, 259f, 111f, "★", amber)
        badge(canvas, 253f, 197f, "✎", Color.rgb(124, 58, 237))
    }

    private fun drawStudent(canvas: Canvas, centerX: Float, top: Float, shirtColor: Int) {
        val headTop = top + 8f
        roundRect(canvas, centerX - 37f, headTop + 43f, centerX + 37f, top + 166f, 28f, shirtColor)
        circle(canvas, centerX, headTop + 30f, 31f, Color.rgb(135, 73, 38))
        roundRect(canvas, centerX - 36f, headTop - 1f, centerX + 36f, headTop + 28f, 18f, ink)
        circle(canvas, centerX - 11f, headTop + 31f, 2.8f, ink)
        circle(canvas, centerX + 11f, headTop + 31f, 2.8f, ink)
        text(canvas, "⌣", centerX, headTop + 54f, 17f, white, false, true)
        text(canvas, "▤", centerX, top + 134f, 23f, white, true, true)
    }

    private fun drawRobot(canvas: Canvas, centerX: Float, top: Float) {
        roundRect(
            canvas,
            centerX - 43f,
            top + 53f,
            centerX + 43f,
            top + 158f,
            25f,
            Color.rgb(239, 246, 255)
        )
        roundRect(
            canvas,
            centerX - 37f,
            top + 2f,
            centerX + 37f,
            top + 77f,
            27f,
            blue
        )
        paint.color = white
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 4f
        canvas.drawRoundRect(
            RectF(centerX - 40f, top - 1f, centerX + 40f, top + 80f),
            28f,
            28f,
            paint
        )
        paint.style = Paint.Style.FILL
        circle(canvas, centerX - 12f, top + 34f, 4f, white)
        circle(canvas, centerX + 12f, top + 34f, 4f, white)
        text(canvas, "⌣", centerX, top + 55f, 18f, white, false, true)
        text(canvas, "◇", centerX, top + 130f, 30f, blue, true, true)
    }

    private fun badge(canvas: Canvas, centerX: Float, centerY: Float, label: String, color: Int) {
        roundRect(canvas, centerX - 22f, centerY - 22f, centerX + 22f, centerY + 22f, 14f, white)
        text(canvas, label, centerX, centerY + 6f, 19f, color, true, true)
    }

    private fun circle(canvas: Canvas, cx: Float, cy: Float, radius: Float, color: Int) {
        paint.style = Paint.Style.FILL
        paint.color = color
        canvas.drawCircle(cx, cy, radius, paint)
    }

    private fun roundRect(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        radius: Float,
        color: Int,
        strokeWidth: Float = 0f
    ) {
        paint.color = color
        paint.style = if (strokeWidth > 0f) Paint.Style.STROKE else Paint.Style.FILL
        paint.strokeWidth = strokeWidth
        canvas.drawRoundRect(RectF(left, top, right, bottom), radius, radius, paint)
        paint.style = Paint.Style.FILL
    }

    private fun text(
        canvas: Canvas,
        value: String,
        x: Float,
        baseline: Float,
        size: Float,
        color: Int,
        bold: Boolean,
        centered: Boolean
    ) {
        paint.color = color
        paint.textSize = size
        paint.typeface = if (bold) {
            android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
        } else {
            android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.NORMAL)
        }
        paint.textAlign = if (centered) Paint.Align.CENTER else Paint.Align.LEFT
        paint.style = Paint.Style.FILL
        canvas.drawText(value, x, baseline, paint)
    }
}
