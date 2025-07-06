package com.example.vktesttask.domain

import java.io.Serializable

sealed class MarkdownElement : Serializable {
    data class Heading(val level: Int, val text: String) : MarkdownElement()
    data class Paragraph(val text: String) : MarkdownElement()
    data class Bold(val text: String) : MarkdownElement()
    data class Italic(val text: String) : MarkdownElement()
    data class Strikethrough(val text: String) : MarkdownElement()
    data class Image(val altText: String, val url: String) : MarkdownElement()
    data class Table(val header: List<String>, val rows: List<List<String>>) : MarkdownElement()
}