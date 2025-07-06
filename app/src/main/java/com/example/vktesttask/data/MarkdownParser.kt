package com.example.vktesttask.data

import com.example.vktesttask.domain.MarkdownDocument
import com.example.vktesttask.domain.MarkdownElement

class MarkdownParser  {

    fun parse(markdown: String): MarkdownDocument {
        val elements = mutableListOf<MarkdownElement>()
        val lines = markdown.lines()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            when {
                isHeading(line) -> {
                    elements.add(parseHeading(line))
                }

                isImage(line) -> {
                    elements.add(parseImage(line))
                }

                isTableStart(line, i, lines) -> {
                    val (header, rows, newIndex) = parseTable(lines, i)
                    elements.add(MarkdownElement.Table(header, rows))
                    i = newIndex - 1
                }

                line.isNotBlank() -> {
                    elements.add(MarkdownElement.Paragraph(parseInlineStyles(line)))
                }
            }

            i++
        }

        return MarkdownDocument(elements)
    }

    private fun isHeading(line: String): Boolean {
        return line.trim().startsWith("#")
    }

    private fun parseHeading(line: String): MarkdownElement.Heading {
        val match = Regex("^(#{1,6})\\s+(.*)").find(line)
        val level = match?.groups?.get(1)?.value?.length ?: 1
        val text = match?.groups?.get(2)?.value ?: line
        return MarkdownElement.Heading(level, text)
    }

    private fun isImage(line: String): Boolean {
        return Regex("!\\[.*]\\(.*\\)").matches(line)
    }

    private fun parseImage(line: String): MarkdownElement.Image {
        val match = Regex("!\\[(.*)]\\((.*)\\)").find(line)!!
        val alt = match.groupValues[1]
        val url = match.groupValues[2]
        return MarkdownElement.Image(alt, url)
    }

    private fun isTableStart(line: String, index: Int, lines: List<String>): Boolean {
        return index + 1 < lines.size && lines[index + 1].trim().matches(Regex("^[|\\s:-]+$"))
    }

    private fun parseTable(lines: List<String>, startIndex: Int): Triple<List<String>, List<List<String>>, Int> {
        val headerLine = lines[startIndex]
        val header = headerLine.trim('|', ' ').split("|").map { it.trim() }

        val rows = mutableListOf<List<String>>()
        var i = startIndex + 2
        while (i < lines.size && lines[i].contains("|")) {
            val row = lines[i].trim('|', ' ').split("|").map { it.trim() }
            rows.add(row)
            i++
        }

        return Triple(header, rows, i)
    }

    private fun parseInlineStyles(text: String): String {
        return text
            .replace(Regex("\\*\\*(.*?)\\*\\*"), "<b>$1</b>")
            .replace(Regex("\\*(.*?)\\*"), "<i>$1</i>")
            .replace(Regex("~~(.*?)~~"), "<s>$1</s>")
    }
}