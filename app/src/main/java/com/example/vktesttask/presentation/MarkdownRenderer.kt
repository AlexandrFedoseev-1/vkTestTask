package com.example.vktesttask.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.example.vktesttask.domain.MarkdownDocument
import com.example.vktesttask.domain.MarkdownElement
import java.net.URL

class MarkdownRenderer(
    private val context: Context
) {
    fun render(container: LinearLayout, document: MarkdownDocument) {
        container.removeAllViews()
        for (element in document.elements) {
            val view = when (element) {
                is MarkdownElement.Heading -> renderHeading(element)
                is MarkdownElement.Paragraph -> renderParagraph(element)
                is MarkdownElement.Image -> renderImage(element)
                is MarkdownElement.Table -> renderTable(element)
                else -> null
            }
            view?.let { container.addView(it) }
        }
    }

    private fun renderHeading(element: MarkdownElement.Heading): View {
        return TextView(context).apply {
            text = element.text
            textSize = (24 - element.level * 2).toFloat()
            setTypeface(null, Typeface.BOLD)
        }
    }

    private fun renderParagraph(element: MarkdownElement.Paragraph): View {
        return TextView(context).apply {
            text = HtmlCompat.fromHtml(element.text, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }

    private fun renderImage(element: MarkdownElement.Image): View {
        return ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                gravity = Gravity.START
                setMargins(0, 16, 0, 16)
            }
            scaleType = ImageView.ScaleType.FIT_START

            // Загрузка без сторонних библиотек
            Thread {
                try {
                    val input = URL(element.url).openStream()
                    val bitmap = BitmapFactory.decodeStream(input)
                    post {
                        setImageBitmap(bitmap)
                    }
                } catch (_: Exception) {
                }
            }.start()
        }
    }

    private fun renderTable(element: MarkdownElement.Table): View {
        val tableLayout = TableLayout(context)
        tableLayout.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        val border = GradientDrawable().apply {
            setStroke(1, Color.GRAY)
        }
        val allRows = listOf(element.header) + element.rows
        for (row in allRows) {
            val tableRow = TableRow(context)
            for (cell in row) {
                val textView = TextView(context).apply {
                    text = cell
                    setPadding(8, 4, 8, 4)
                    background = border
                }
                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }
        return tableLayout
    }
}