package com.pyschool.app.navigation

import android.app.Activity

/**
 * ทะเบียนหน้าจอทั้งหมดในแอป
 */
enum class Screen(val className: String, val isTab: Boolean = false) {
    SPLASH("com.pyschool.app.ui.splash.SplashActivity"),
    HOME("com.pyschool.app.ui.home.HomeActivity", isTab = true),
    LESSON_1("com.pyschool.app.ui.lesson.Lesson1Activity"),
    LESSON_2("com.pyschool.app.ui.lesson.Lesson2Activity"),
    LESSON_3("com.pyschool.app.ui.lesson.Lesson3Activity"),
    LESSON_4("com.pyschool.app.ui.lesson.Lesson4Activity"),
    VISUAL("com.pyschool.app.ui.visual.VisualLearnActivity", isTab = true),
    QUIZ("com.pyschool.app.ui.quiz.QuizActivity", isTab = true),
    QUIZ_RESULT("com.pyschool.app.ui.quiz.QuizResultActivity"),
    HISTORY("com.pyschool.app.ui.history.HistoryActivity", isTab = true),
    ABOUT("com.pyschool.app.ui.about.AboutActivity"),
    GAME_LAB("com.pyschool.app.ui.game.GameLabActivity"),
    ADVANCED_PREVIEW("com.pyschool.app.ui.advanced.AdvancedPreviewActivity");

    companion object {
        /** บทเรียนเรียงตามลำดับ ใช้กับปุ่ม Back / Next lesson */
        val lessons = listOf(LESSON_1, LESSON_2, LESSON_3, LESSON_4)

        /** แท็บของ Bottom Navigation */
        val tabs = listOf(HOME, VISUAL, QUIZ, HISTORY)

        fun of(activity: Activity): Screen? = entries.firstOrNull { it.className == activity.javaClass.name }
    }
}
