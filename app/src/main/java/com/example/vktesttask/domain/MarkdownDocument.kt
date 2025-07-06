package com.example.vktesttask.domain


import java.io.Serializable

data class MarkdownDocument(
    val elements: List<MarkdownElement>
): Serializable
