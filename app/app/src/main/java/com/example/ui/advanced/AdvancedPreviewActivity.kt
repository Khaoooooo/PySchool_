package com.pyschool.app.ui.advanced

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.pyschool.app.R
import com.pyschool.app.databinding.ActivityAdvancedPreviewBinding
import com.pyschool.app.navigation.AppNavigator
import com.pyschool.app.navigation.Screen
import com.pyschool.app.ui.common.BaseActivity
import com.pyschool.app.ui.common.PythonHighlighter
import com.pyschool.app.ui.common.applyStatusBarPadding

class AdvancedPreviewActivity : BaseActivity() {

    private lateinit var binding: ActivityAdvancedPreviewBinding

    private data class TopicPreview(
        val badge: String,
        val title: String,
        val desc: String,
        val code: String,
        val result: String,
    )

    private val topics = mapOf(
        "exceptions" to TopicPreview(
            badge = "WEEK 11 - EXCEPTIONS",
            title = "Exceptions & Try...Except",
            desc = "เรียนรู้การดักจับข้อผิดพลาดระหว่างรันโปรแกรมอย่างมีประสิทธิภาพ ป้องกันแอปค้างด้วยบล็อก try...except",
            code = "try:\n    num = int(input(\"Enter number: \"))\nexcept ValueError:\n    print(\"Invalid input! Please enter a number.\")",
            result = "bad input -> friendly message"
        ),
        "files" to TopicPreview(
            badge = "WEEK 12 - FILES",
            title = "File Handling in Python",
            desc = "เรียนรู้วิธีเปิด อ่าน และบันทึกข้อมูลลงไฟล์ข้อความ (.txt) ด้วยคำสั่ง with open()",
            code = "with open(\"score.txt\", \"w\") as file:\n    file.write(\"98\")\n\nwith open(\"score.txt\", \"r\") as file:\n    print(\"Saved score:\", file.read())",
            result = "score.txt -> 98"
        ),
        "oop" to TopicPreview(
            badge = "WEEKS 13-14 - OOP",
            title = "Object-Oriented Programming",
            desc = "การเขียนโปรแกรมเชิงวัตถุ สร้างคลาส (Class) และอินสแตนซ์ (Instance) เพื่อจัดระเบียบโค้ด",
            code = "class Student:\n    def __init__(self, name):\n        self.name = name\n\nrina = Student(\"Rina\")\nprint(rina.name)",
            result = "rina.name -> \"Rina\""
        )
    )

    private var selectedTab = "exceptions"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvancedPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.root.applyStatusBarPadding()
        binding.header.headerTitle.setText(R.string.home_bonus_title)
        binding.header.backButton.setOnClickListener { finish() }

        binding.tabExceptions.setOnClickListener { selectTopic("exceptions") }
        binding.tabFiles.setOnClickListener { selectTopic("files") }
        binding.tabOop.setOnClickListener { selectTopic("oop") }

        binding.btnStartCore.setOnClickListener {
            AppNavigator.open(this, Screen.LESSON_1)
        }

        selectTopic("exceptions")
    }

    private fun selectTopic(key: String) {
        selectedTab = key
        val topic = topics[key] ?: return

        // Update Tab styles
        val whiteColor = ContextCompat.getColor(this, R.color.white)
        val purpleColor = ContextCompat.getColor(this, R.color.purple)
        val inkColor = ContextCompat.getColor(this, R.color.ink)
        val lineColor = ContextCompat.getColor(this, R.color.line)

        fun updateTabCard(card: com.google.android.material.card.MaterialCardView, text: android.widget.TextView, active: Boolean) {
            card.setCardBackgroundColor(if (active) purpleColor else whiteColor)
            card.strokeColor = if (active) purpleColor else lineColor
            card.strokeWidth = if (active) 0 else (1.5f * resources.displayMetrics.density).toInt()
            text.setTextColor(if (active) whiteColor else inkColor)
        }

        updateTabCard(binding.tabExceptions, binding.textExceptions, key == "exceptions")
        updateTabCard(binding.tabFiles, binding.textFiles, key == "files")
        updateTabCard(binding.tabOop, binding.textOop, key == "oop")

        // Render topic content
        binding.roadmapBadge.text = topic.badge
        binding.topicTitle.text = topic.title
        binding.topicDesc.text = topic.desc
        binding.previewCodeText.text = PythonHighlighter.highlight(this, topic.code)
        binding.resultText.text = topic.result
    }
}
