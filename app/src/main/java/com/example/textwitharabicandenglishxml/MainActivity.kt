package com.example.textwitharabicandenglishxml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val linearLayout = findViewById<LinearLayout>(R.id.customLinearLayout)

        // Sample texts
        val texts = listOf("Hello, World!", "مرحبًا بك في Android",
            "سم المحلول الأتي (Zn⁺² + SO₄²⁻)"
        )
        texts.forEach{txt ->
            detectArabicOrEnglish(txt).forEach {
                val text = it.first
                val textView = TextView(this)
                textView.text = text
                textView.gravity = Gravity.CENTER
                textView.textDirection = when(it.second){
                    "rtl"->TextView.TEXT_DIRECTION_RTL
                    "ltr"->TextView.TEXT_DIRECTION_LTR
                    else->TextView.TEXT_DIRECTION_INHERIT
                }
                linearLayout.addView(textView)
            }
        }
    }
}
fun detectArabicOrEnglish(text: String): List<Pair<String, String>> {
    val arabicRegex = "\\p{InArabic}"
    val englishRegex = "\\p{InBasicLatin}"

    val result = mutableListOf<Pair<String, String>>()
    var currentSegment = StringBuilder()
    var currentDirection = ""

    for (char in text) {
        val charDirection =
            when {
                char.toString().matches(Regex(arabicRegex)) -> "rtl"
                char.toString().matches(Regex(englishRegex)) -> "ltr"
                else -> ""
            }

        if (charDirection != currentDirection) {
            if (currentSegment.isNotEmpty()) {
                result.add(Pair(currentSegment.toString(), currentDirection))
                currentSegment = StringBuilder()
            }
            currentDirection = charDirection
        }

        currentSegment.append(char)
    }

    if (currentSegment.isNotEmpty()) {
        result.add(Pair(currentSegment.toString(), currentDirection))
    }

    return result
}

