package com.pyschool.app.data

import androidx.annotation.StringRes
import com.pyschool.app.R
import com.pyschool.app.navigation.Screen

/** ข้อมูลคงที่ของบทเรียน */
data class Lesson(
    val id: Int,
    val moduleText: String,
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    val iconText: String,
    val chips: List<String>,
    val screen: Screen,
)

object LessonCatalog {
    val lessons: List<Lesson> = listOf(
        Lesson(
            1,
            "MODULE 01 · WEEKS 1-2",
            R.string.lesson1_title,
            R.string.lesson1_desc,
            "Py",
            listOf("Python Intro", "Variables"),
            Screen.LESSON_1,
        ),
        Lesson(
            2,
            "MODULE 02 · WEEKS 3-4",
            R.string.lesson2_title,
            R.string.lesson2_desc,
            "+ -",
            listOf("Operators", "Numeric Functions", "Strings"),
            Screen.LESSON_2,
        ),
        Lesson(
            3,
            "MODULE 03 · WEEK 5",
            R.string.lesson3_title,
            R.string.lesson3_desc,
            "<>",
            listOf("if/else", "For loop", "While loop"),
            Screen.LESSON_3,
        ),
        Lesson(
            4,
            "MODULE 04 · WEEK 6",
            R.string.lesson4_title,
            R.string.lesson4_desc,
            "fx",
            listOf("Functions", "Parameters", "Modules"),
            Screen.LESSON_4,
        ),
    )

    fun byId(id: Int): Lesson? = lessons.firstOrNull { it.id == id }
}
