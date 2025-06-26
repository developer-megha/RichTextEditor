package com.kitlabs.htmltexteditor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.kitlabs.htmltexteditor.databinding.ActivityMainBinding
import com.megha.richtexteditor.RichEditor
import com.megha.richtexteditor.adapter.ColorGridAdapter
import com.megha.richtexteditor.adapter.TextAlignmentAdapter
import com.megha.richtexteditor.adapter.FontSizeAdapter
import com.megha.richtexteditor.adapter.FontAdapter

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val colors = listOf(
        "#000000", "#e60000", "#ff9900", "#ffff00", "#008a00", "#0066cc", "#9933ff",
        "#ffffff", "#facccc", "#ffebcc", "#ffebcc", "#cce8cc", "#cce0f5", "#ebd6ff",
        "#bbbbbb", "#f06666", "#ffc266", "#ffff66", "#66b966", "#66a3e0", "#c285ff",
        "#888888", "#a10000", "#b26b00", "#b2b200", "#006100", "#0047b2", "#6b24b2",
        "#444444", "#5c0000", "#663d00", "#666600", "#003700", "#002966", "#3d1466"
        )

    private val fontList = listOf(
        "Arial", "Verdana", "Helvetica", "Tahoma", "Trebuchet MS",
        "Times New Roman", "Georgia", "Garamond", "Courier New", "Brush Script MT"
    )

    private val fontSize = listOf(8, 9, 10, 11, 12, 14, 16, 18, 24, 30)

    private val fontMap = mapOf(
        "Arial" to R.font.arial,
        "Verdana" to R.font.verdana,
        "Helvetica" to R.font.helvetica,
        "Tahoma" to R.font.tahoma,
        "Trebuchet MS" to R.font.trebuc,
        "Times New Roman" to R.font.times,
        "Georgia" to R.font.georgia,
        "Garamond" to R.font.garamond,
        "Courier New" to R.font.courier,
        "Brush Script MT" to R.font.brushscriptmt
    )

    private val alignmentList = listOf(R.drawable.ic_justify_left,
        R.drawable.ic_justify_center,
        R.drawable.ic_justify_right,
        R.drawable.ic_justify_full)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fontSpinner.adapter = FontAdapter(this, fontList, fontMap)

        binding.fontSizeSpinner.adapter = FontSizeAdapter(this, fontSize)

        binding.alignSpinner.adapter = TextAlignmentAdapter(this, alignmentList)

        binding.editor.setEditorHeight(200)
        binding.editor.setEditorFontSize(8)
        binding.editor.setEditorFontColor(colors[0])
        binding.editor.setFontFamily(fontList[0])
        binding.editor.setPadding(10, 10, 10, 10)
        binding.editor.setPlaceholder("Insert text here...")

        binding.editor.addJavascriptInterface(JsBridge(), "Android")

        binding.editor.setOnTextChangeListener(object : RichEditor.OnTextChangeListener{
            override fun onTextChange(text: String?) {
                binding.preview.text = text
            }
        })

        binding.fontSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedFont = fontList[position]
                binding.editor.setFontFamily(selectedFont)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.alignSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        binding.editor.setAlignLeft()
                    }
                    1 -> {
                        binding.editor.setAlignCenter()
                    }
                    2 -> {
                        binding.editor.setAlignRight()
                    }
                    else -> {
                        binding.editor.setJustifyFull()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.fontSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedFontSize = fontSize[position]
                binding.editor.setEditorFontSize(selectedFontSize)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.actionBold.setOnClickListener {
            binding.editor.setBold()
        }

        binding.actionItalic.setOnClickListener {
            binding.editor.setItalic()
        }

        binding.actionUnderline.setOnClickListener {
            binding.editor.setUnderline()
        }

        binding.actionInsertNumbers.setOnClickListener {
            binding.editor.setNumbers()
        }

        binding.actionInsertBullets.setOnClickListener {
            binding.editor.setBullets()
        }

        binding.actionTxtColor.setOnClickListener {
            showColorGridDialog(this) { selectedColor ->
                binding.editor.setTextColor(selectedColor)
            }
        }

        binding.actionBgColor.setOnClickListener {
            showColorGridDialog(this) { selectedColor ->
                binding.editor.setTextBackgroundColor(selectedColor)
            }
        }

        binding.actionClear.setOnClickListener {
            binding.editor.clearFormatting()
        }

        binding.actionInsertLink.setOnClickListener {
            binding.editor.evaluateJavascript(
                """
        (function() {
            var sel = window.getSelection();
            return (sel && !sel.isCollapsed && sel.toString().trim().length > 0);
        })();
        """.trimIndent()
            ) { hasSelection ->
                if (hasSelection == "true") {
                    showInsertLinkDialog(this) { url ->
                        binding.editor.insertLink(url)
                    }
                } else {
                    Toast.makeText(this, "Please select text to add a hyperlink.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showColorGridDialog(context: Context, onColorSelected: (String) -> Unit) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_color_picker, null)
        val gridView = dialogView.findViewById<GridView>(R.id.grid_colors)

        val adapter = ColorGridAdapter(context, colors)
        gridView.adapter = adapter

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        gridView.setOnItemClickListener { _, _, position, _ ->
            dialog.dismiss()
            onColorSelected(colors[position])
        }

        dialog.show()
    }

    private fun showInsertLinkDialog(context: Context, onLinkInsert: (url: String) -> Unit) {
        val urlEditText = EditText(context).apply {
            hint = "Enter URL"
            inputType = InputType.TYPE_TEXT_VARIATION_URI
        }

        AlertDialog.Builder(context)
            .setTitle("Insert Link")
            .setView(urlEditText)
            .setPositiveButton("Insert") { _, _ ->
                val url = urlEditText.text.toString().trim()
                if (url.isNotEmpty()) {
                    onLinkInsert(url)
                } else {
                    Toast.makeText(context, "URL is required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    inner class JsBridge {
        @JavascriptInterface
        fun onLinkClicked(url: String) {
            runOnUiThread {
                showLinkOptionsDialog(url)
            }
        }
    }

    fun showLinkOptionsDialog(currentUrl: String) {
        val options = arrayOf("Visit: $currentUrl", "Edit", "Remove")

        AlertDialog.Builder(this)
            .setTitle("Link Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> { // Visit
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl))
                        startActivity(intent)
                    }
                    1 -> { // Edit
                        showInsertUrlDialog { newUrl ->
                            binding.editor.evaluateJavascript("""
                            (function() {
                                var sel = window.getSelection();
                                if (sel.rangeCount > 0) {
                                    var node = sel.anchorNode.parentElement;
                                    if (node && node.tagName === 'A') {
                                        node.setAttribute('href', '$newUrl');
                                    }
                                }
                            })();
                        """.trimIndent(), null)
                        }
                    }
                    2 -> { // Remove
                        binding.editor.evaluateJavascript("""
                        (function() {
                            var sel = window.getSelection();
                            if (sel.rangeCount > 0) {
                                var node = sel.anchorNode.parentElement;
                                if (node && node.tagName === 'A') {
                                    var text = node.innerText;
                                    var span = document.createElement('span');
                                    span.textContent = text;
                                    node.parentNode.replaceChild(span, node);
                                }
                            }
                        })();
                    """.trimIndent(), null)
                    }
                }
            }
            .show()
    }

    private fun showInsertUrlDialog(defaultUrl: String = "", onUrlEntered: (String) -> Unit) {
        val editText = EditText(this).apply {
            hint = "Enter URL"
            inputType = InputType.TYPE_TEXT_VARIATION_URI
            setText(defaultUrl)
        }

        AlertDialog.Builder(this)
            .setTitle("Insert/Edit Link")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                val url = editText.text.toString().trim()
                if (url.isNotEmpty()) onUrlEntered(url)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

}