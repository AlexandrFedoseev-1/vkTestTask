package com.example.vktesttask.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vktesttask.R
import com.example.vktesttask.presentation.viewmodels.SaveMarkdownViewModel
import com.example.vktesttask.presentation.viewmodels.SaveMarkdownViewModelFactory
import kotlin.jvm.java

class MarkdownEditActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var viewModel: SaveMarkdownViewModel
    private var fileUri: Uri? = null
    private var saveContent: String? = null
    private val saveURLFileLauncher =
        registerForActivityResult(ActivityResultContracts.CreateDocument("text/markdown")) { uri: Uri? ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                fileUri = it

                saveContent?.let { content -> saveAndBack(content) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_markdown_edit)

        editText = findViewById(R.id.editMarkdownText)
        editText.setText(intent.getStringExtra("rawText"))

        fileUri = intent.getParcelableExtra<Uri>("fileUri")

        viewModel = ViewModelProvider(
            this,
            SaveMarkdownViewModelFactory(contentResolver)
        )[SaveMarkdownViewModel::class.java]

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val content = editText.text.toString()
            if (fileUri == null) {
                saveContent = content
                saveURLFileLauncher.launch("markdown.md")
            } else {
                saveAndBack(content)
            }
        }
    }

    private fun saveAndBack(content: String) {
        if (fileUri!=null){
            viewModel.save(fileUri!!, content)
            val intent = Intent(this, MarkdownViewActivity::class.java).apply {
                putExtra("rawText", content)
                putExtra("fileUri", fileUri)
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            Toast.makeText(this, "Сохранено в: $fileUri", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }else{
            Toast.makeText(this, "Ошибка при сохранении!", Toast.LENGTH_LONG).show()
        }

    }
}