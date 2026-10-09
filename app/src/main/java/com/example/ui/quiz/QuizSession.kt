package com.pyschool.app.ui.quiz

import com.pyschool.app.data.model.QuizQuestion

/**
 * สถานะของการทำควิซหนึ่งรอบ (ตรรกะล้วน ไม่แตะ Android → ทดสอบง่าย)
 * กติกา: ตอบแล้วล็อกคำตอบของข้อนั้นทันที เพื่อแสดงเฉลยและคำอธิบายได้เลย
 */
class QuizSession(val questions: List<QuizQuestion>) {

    private val answers = IntArray(questions.size) { UNANSWERED }

    var index = 0
        private set

    val total: Int get() = questions.size
    val current: QuizQuestion get() = questions[index]
    val isFirst: Boolean get() = index == 0
    val isLast: Boolean get() = index == questions.lastIndex
    val isAnswered: Boolean get() = answers[index] != UNANSWERED
    val selectedOption: Int get() = answers[index]

    /** ความคืบหน้าตามหมายเลขข้อ (ข้อ 3/10 = 30%) */
    val progressPercent: Int get() = (index + 1) * 100 / total

    val score: Int get() = questions.indices.count { answers[it] == questions[it].correctIndex }

    fun select(option: Int) {
        if (!isAnswered) answers[index] = option
    }

    fun next() {
        if (!isLast) index++
    }

    fun previous() {
        if (!isFirst) index--
    }

    private companion object {
        const val UNANSWERED = -1
    }
}
