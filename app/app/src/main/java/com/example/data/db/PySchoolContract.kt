package com.pyschool.app.data.db

/** ชื่อตารางและคอลัมน์ทั้งหมดอยู่ที่เดียว เพื่อกันพิมพ์ผิดใน SQL */
object PySchoolContract {
    const val DB_NAME = "pyschool.db"

    /** เพิ่มเลขเมื่อแก้ธนาคารโจทย์ใน QuestionSeed — ประวัติคะแนนจะไม่ถูกลบ */
    const val DB_VERSION = 1

    object LessonProgressTable {
        const val TABLE = "lesson_progress"
        const val ID = "lesson_id"
        const val PROGRESS = "progress"
        const val UPDATED_AT = "updated_at"
    }

    object QuestionTable {
        const val TABLE = "quiz_question"
        const val ID = "_id"
        const val TOPIC = "topic"
        const val QUESTION = "question"
        const val CODE = "code"
        const val OPTION_A = "option_a"
        const val OPTION_B = "option_b"
        const val OPTION_C = "option_c"
        const val OPTION_D = "option_d"
        const val CORRECT_INDEX = "correct_index"
        const val EXPLANATION = "explanation"
    }

    object HistoryTable {
        const val TABLE = "quiz_history"
        const val ID = "_id"
        const val SCORE = "score"
        const val TOTAL = "total"
        const val TAKEN_AT = "taken_at"
    }
}
