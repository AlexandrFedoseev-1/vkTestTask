package com.example.vktesttask.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.vktesttask.R
import com.example.vktesttask.presentation.viewmodels.LoadMarkdownViewModel
import com.example.vktesttask.presentation.viewmodels.LoadMarkdownViewModelFactory
import java.io.File
import java.io.FileOutputStream

class LoadMarkdownActivity : AppCompatActivity() {

    private lateinit var viewModel: LoadMarkdownViewModel

    private lateinit var urlInput: EditText
    private lateinit var loadUrlButton: Button
    private lateinit var pickFileButton: Button
    private lateinit var loadingProgress: ProgressBar
    private lateinit var errorText: TextView
    private var fileUri: Uri? = null

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
                val path = getFilePathFromUri(it)
                if (path != null) {
                    viewModel.loadFromFile(path)
                    fileUri = it
                } else {
                    showError("Не удалось получить путь к файлу")
                }
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_markdown)

        initViews()
        initViewModel()
        observeViewModel()
        setupListeners()
    }

    private fun initViews() {
        urlInput = findViewById(R.id.urlInput)
        loadUrlButton = findViewById(R.id.loadUrlButton)
        pickFileButton = findViewById(R.id.pickFileButton)
        loadingProgress = findViewById(R.id.loadingProgress)
        errorText = findViewById(R.id.errorText)
    }

    private fun initViewModel() {

        viewModel = ViewModelProvider(
            this,
            LoadMarkdownViewModelFactory(contentResolver)
        )[LoadMarkdownViewModel::class.java]
    }

    private fun setupListeners() {
        loadUrlButton.setOnClickListener {
            val url = urlInput.text.toString()
            if (url.startsWith("http")) {
                viewModel.loadFromUrl(url)
            } else {
                showError("Введите корректный URL (http/https)")
            }
        }

        pickFileButton.setOnClickListener {
            pickFileLauncher.launch(arrayOf("text/markdown"))
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is LoadMarkdownViewModel.LoadState.Loading -> {
                    loadingProgress.visibility = View.VISIBLE
                    errorText.visibility = View.GONE
                }

                is LoadMarkdownViewModel.LoadState.Success -> {
                    loadingProgress.visibility = View.GONE
                    errorText.visibility = View.GONE
                    navigateToMarkdownView(state.rawText)
                }

                is LoadMarkdownViewModel.LoadState.Error -> {
                    loadingProgress.visibility = View.GONE
                    showError(state.message)
                }
            }
        }
    }

    private fun navigateToMarkdownView(rawText: String) {
        val intent = Intent(this, MarkdownViewActivity::class.java).apply {
            putExtra("rawText", rawText)
            putExtra("fileUri", fileUri)
        }
        startActivity(intent)
    }


    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val tempFile = File.createTempFile("md_temp", ".md", cacheDir)
            inputStream?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}