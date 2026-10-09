package com.pyschool.app.data.model

/** ความก้าวหน้าของบทเรียนหนึ่งบท (0–100) */
data class LessonProgress(val lessonId: Int, val progress: Int)

/** โจทย์ปรนัย 4 ข้อ; [code] เป็นโค้ดประกอบโจทย์ (ถ้ามี) */
data class QuizQuestion(
    val id: Long,
    val topic: String,
    val question: String,
    val code: String?,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

/** ผลการทำควิซหนึ่งครั้งที่บันทึกลง SQLite */
data class QuizAttempt(
    val id: Long,
    val score: Int,
    val total: Int,
    val takenAt: Long,
) {
    val percent: Int get() = if (total == 0) 0 else score * 100 / total
}

/** ข้อมูลสรุปสำหรับการ์ด Best Score / Quizzes Taken ในหน้า History */
data class HistorySummary(val bestPercent: Int, val attemptCount: Int)
