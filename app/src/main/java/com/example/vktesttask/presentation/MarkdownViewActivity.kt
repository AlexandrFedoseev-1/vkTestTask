package com.example.vktesttask.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vktesttask.R
import com.example.vktesttask.presentation.viewmodels.LoadMarkdownViewModel
import com.example.vktesttask.presentation.viewmodels.LoadMarkdownViewModelFactory
import kotlin.jvm.java

class MarkdownViewActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var viewModel: LoadMarkdownViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markdown_view)
        viewModel = ViewModelProvider(this, LoadMarkdownViewModelFactory(contentResolver))[LoadMarkdownViewModel::class.java]
        container = findViewById(R.id.markdownContainer)

        val rawText = intent.getStringExtra("rawText")
        if (rawText!=null){
            val document = viewModel.parseMarkdown(rawText)
            MarkdownRenderer(this).render(container, document)
        }
        

        findViewById<Button>(R.id.editButton).setOnClickListener {

            val fileUri = intent.getParcelableExtra<Uri>("fileUri")
            val intent = Intent(this, MarkdownEditActivity::class.java).apply {
                putExtra("rawText", rawText)
                putExtra("fileUri", fileUri)
            }
            startActivity(intent)
        }
    }
}