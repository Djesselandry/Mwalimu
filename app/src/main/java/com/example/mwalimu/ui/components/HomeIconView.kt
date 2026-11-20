package com.example.mwalimu.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View

internal enum class HomeIcon {
    NOTIFICATIONS,
    SETTINGS,
    REPEAT,
    PLAY,
    MICROPHONE,
    MESSAGE,
    BOOK,
    FLAME,
    CLOCK,
    TEACHER,
    CHAT,
    CALENDAR,
    EVALUATION,
    BACK,
    SEARCH,
    COPY,
    SPEAKER,
    REFRESH,
    THUMBS_UP,
    THUMBS_DOWN,
    SEND
}

internal class HomeIconView(
    context: Context,
    private val icon: HomeIcon,
    private val tint: Int
) : View(context) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = tint
        style = Paint.Style.STROKE
        strokeWidth = 1.8f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun setTint(color: Int) {
        paint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val scale = minOf(width / 24f, height / 24f)
        if (scale <= 0f) return

        canvas.save()
        canvas.translate((width - 24f * scale) / 2f, (height - 24f * scale) / 2f)
        canvas.scale(scale, scale)
        when (icon) {
            HomeIcon.NOTIFICATIONS -> drawNotifications(canvas)
            HomeIcon.SETTINGS -> drawSettings(canvas)
            HomeIcon.REPEAT -> drawRepeat(canvas)
            HomeIcon.PLAY -> drawPlay(canvas)
            HomeIcon.MICROPHONE -> drawMicrophone(canvas)
            HomeIcon.MESSAGE, HomeIcon.CHAT -> drawMessage(canvas)
            HomeIcon.BOOK -> drawBook(canvas)
            HomeIcon.FLAME -> drawFlame(canvas)
            HomeIcon.CLOCK -> drawClock(canvas)
            HomeIcon.TEACHER -> drawTeacher(canvas)
            HomeIcon.CALENDAR -> drawCalendar(canvas)
            HomeIcon.EVALUATION -> drawEvaluation(canvas)
            HomeIcon.BACK -> drawBack(canvas)
            HomeIcon.SEARCH -> drawSearch(canvas)
            HomeIcon.COPY -> drawCopy(canvas)
            HomeIcon.SPEAKER -> drawSpeaker(canvas)
            HomeIcon.REFRESH -> drawRepeat(canvas)
            HomeIcon.THUMBS_UP -> drawThumbsUp(canvas, false)
            HomeIcon.THUMBS_DOWN -> drawThumbsUp(canvas, true)
            HomeIcon.SEND -> drawSend(canvas)
        }
        canvas.restore()
    }

    private fun drawNotifications(canvas: Canvas) {
        path(canvas) {
            moveTo(5f, 16f)
            lineTo(7f, 13.5f)
            lineTo(7f, 10f)
            cubicTo(7f, 3.8f, 17f, 3.8f, 17f, 10f)
            lineTo(17f, 13.5f)
            lineTo(19f, 16f)
            close()
        }
        canvas.drawLine(5f, 16f, 19f, 16f, paint)
        canvas.drawArc(RectF(9f, 17f, 15f, 21f), 0f, 180f, false, paint)
        canvas.drawCircle(12f, 3f, 1f, paint)
    }

    private fun drawSettings(canvas: Canvas) {
        canvas.drawCircle(12f, 12f, 3f, paint)
        canvas.drawCircle(12f, 12f, 7f, paint)
        repeat(8) {
            canvas.save()
            canvas.rotate(it * 45f, 12f, 12f)
            canvas.drawLine(12f, 2f, 12f, 4f, paint)
            canvas.restore()
        }
    }

    private fun drawRepeat(canvas: Canvas) {
        canvas.drawArc(RectF(5f, 5f, 19f, 19f), 45f, 285f, false, paint)
        path(canvas) {
            moveTo(15f, 4f)
            lineTo(19f, 5f)
            lineTo(18f, 9f)
        }
        path(canvas) {
            moveTo(9f, 20f)
            lineTo(5f, 19f)
            lineTo(6f, 15f)
        }
    }

    private fun drawPlay(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        path(canvas) {
            moveTo(8f, 5f)
            lineTo(19f, 12f)
            lineTo(8f, 19f)
            close()
        }
        paint.style = Paint.Style.STROKE
    }

    private fun drawMicrophone(canvas: Canvas) {
        canvas.drawRoundRect(RectF(9f, 3f, 15f, 14f), 3f, 3f, paint)
        canvas.drawArc(RectF(6f, 7f, 18f, 19f), 0f, 180f, false, paint)
        canvas.drawLine(12f, 19f, 12f, 22f, paint)
        canvas.drawLine(8f, 22f, 16f, 22f, paint)
    }

    private fun drawMessage(canvas: Canvas) {
        canvas.drawRoundRect(RectF(4f, 4f, 20f, 17f), 2f, 2f, paint)
        path(canvas) {
            moveTo(8f, 17f)
            lineTo(7f, 21f)
            lineTo(12f, 17f)
        }
    }

    private fun drawBook(canvas: Canvas) {
        path(canvas) {
            moveTo(4f, 5f)
            lineTo(9f, 4f)
            cubicTo(10.5f, 4f, 11.5f, 5f, 12f, 6f)
            lineTo(12f, 20f)
            cubicTo(10.5f, 18.5f, 8f, 18f, 4f, 19f)
            close()
        }
        path(canvas) {
            moveTo(20f, 5f)
            lineTo(15f, 4f)
            cubicTo(13.5f, 4f, 12.5f, 5f, 12f, 6f)
            lineTo(12f, 20f)
            cubicTo(13.5f, 18.5f, 16f, 18f, 20f, 19f)
            close()
        }
    }

    private fun drawFlame(canvas: Canvas) {
        path(canvas) {
            moveTo(12f, 3f)
            cubicTo(14f, 7f, 19f, 8f, 19f, 14f)
            cubicTo(19f, 18.5f, 16f, 21f, 12f, 21f)
            cubicTo(7f, 21f, 4f, 17.5f, 5f, 13f)
            cubicTo(5.5f, 10f, 8f, 8f, 9f, 6f)
            cubicTo(9f, 9f, 11f, 10f, 12f, 11f)
            cubicTo(13f, 8f, 13f, 6f, 12f, 3f)
            close()
        }
        path(canvas) {
            moveTo(12f, 14f)
            cubicTo(14f, 16f, 14f, 17f, 12f, 19f)
            cubicTo(10f, 18f, 10f, 16f, 12f, 14f)
        }
    }

    private fun drawClock(canvas: Canvas) {
        canvas.drawCircle(12f, 12f, 8f, paint)
        canvas.drawLine(12f, 7f, 12f, 12f, paint)
        canvas.drawLine(12f, 12f, 16f, 14f, paint)
    }

    private fun drawTeacher(canvas: Canvas) {
        path(canvas) {
            moveTo(3f, 9f)
            lineTo(12f, 4f)
            lineTo(21f, 9f)
            lineTo(12f, 14f)
            close()
        }
        path(canvas) {
            moveTo(7f, 12f)
            lineTo(7f, 17f)
            cubicTo(10f, 19.5f, 14f, 19.5f, 17f, 17f)
            lineTo(17f, 12f)
        }
        canvas.drawLine(21f, 9f, 21f, 16f, paint)
    }

    private fun drawCalendar(canvas: Canvas) {
        canvas.drawRoundRect(RectF(4f, 5f, 20f, 20f), 2f, 2f, paint)
        canvas.drawLine(4f, 9f, 20f, 9f, paint)
        canvas.drawLine(8f, 3f, 8f, 7f, paint)
        canvas.drawLine(16f, 3f, 16f, 7f, paint)
        canvas.drawCircle(9f, 13f, 0.6f, paint)
        canvas.drawCircle(15f, 13f, 0.6f, paint)
        canvas.drawCircle(9f, 17f, 0.6f, paint)
        canvas.drawCircle(15f, 17f, 0.6f, paint)
    }

    private fun drawEvaluation(canvas: Canvas) {
        path(canvas) {
            moveTo(7f, 3f)
            lineTo(15f, 3f)
            lineTo(19f, 7f)
            lineTo(19f, 21f)
            lineTo(5f, 21f)
            lineTo(5f, 5f)
            close()
        }
        path(canvas) {
            moveTo(14f, 3f)
            lineTo(14f, 8f)
            lineTo(19f, 8f)
        }
        path(canvas) {
            moveTo(8f, 14f)
            lineTo(10f, 16f)
            lineTo(14f, 12f)
        }
        canvas.drawLine(8f, 18.5f, 16f, 18.5f, paint)
    }

    private fun drawSearch(canvas: Canvas) {
        canvas.drawCircle(10.5f, 10.5f, 6f, paint)
        canvas.drawLine(15f, 15f, 20f, 20f, paint)
    }

    private fun drawBack(canvas: Canvas) {
        canvas.drawLine(19f, 12f, 5f, 12f, paint)
        canvas.drawLine(5f, 12f, 11f, 6f, paint)
        canvas.drawLine(5f, 12f, 11f, 18f, paint)
    }

    private fun drawCopy(canvas: Canvas) {
        canvas.drawRoundRect(RectF(8f, 4f, 19f, 17f), 1.5f, 1.5f, paint)
        path(canvas) {
            moveTo(5f, 8f)
            lineTo(5f, 20f)
            lineTo(16f, 20f)
        }
    }

    private fun drawSpeaker(canvas: Canvas) {
        path(canvas) {
            moveTo(4f, 9f)
            lineTo(8f, 9f)
            lineTo(13f, 5f)
            lineTo(13f, 19f)
            lineTo(8f, 15f)
            lineTo(4f, 15f)
            close()
        }
        canvas.drawArc(RectF(10f, 6f, 20f, 18f), -55f, 110f, false, paint)
        canvas.drawArc(RectF(10f, 3f, 24f, 21f), -55f, 110f, false, paint)
    }

    private fun drawThumbsUp(canvas: Canvas, down: Boolean) {
        canvas.save()
        if (down) {
            canvas.rotate(180f, 12f, 12f)
        }
        path(canvas) {
            moveTo(8f, 11f)
            lineTo(11f, 5f)
            cubicTo(11.5f, 3.5f, 14f, 4f, 14f, 6f)
            lineTo(13.5f, 9f)
            lineTo(18f, 9f)
            cubicTo(20f, 9f, 20f, 10.5f, 19f, 12f)
            lineTo(17f, 18f)
            cubicTo(16.5f, 19f, 15.5f, 19f, 14f, 19f)
            lineTo(8f, 17f)
            close()
        }
        canvas.drawRoundRect(RectF(4f, 10f, 8f, 18f), 1f, 1f, paint)
        canvas.restore()
    }

    private fun drawSend(canvas: Canvas) {
        path(canvas) {
            moveTo(4f, 11f)
            lineTo(20f, 4f)
            lineTo(14f, 20f)
            lineTo(11f, 13f)
            close()
        }
        canvas.drawLine(11f, 13f, 20f, 4f, paint)
    }

    private inline fun path(canvas: Canvas, build: Path.() -> Unit) {
        canvas.drawPath(Path().apply(build), paint)
    }
}
