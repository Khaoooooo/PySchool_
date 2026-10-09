package com.pyschool.app.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pyschool.app.data.LessonCatalog
import com.pyschool.app.data.db.PySchoolContract.HistoryTable
import com.pyschool.app.data.db.PySchoolContract.LessonProgressTable
import com.pyschool.app.data.db.PySchoolContract.QuestionTable

/**
 * ศูนย์กลางของ SQLite: สร้างตาราง, seed ข้อมูลเริ่มต้น และจัดการ upgrade
 * ใช้ผ่าน [getInstance] เท่านั้น (singleton) เพื่อไม่ให้เปิด connection ซ้ำ
 *
 * ตาราง
 *  - lesson_progress : ความก้าวหน้ารายบท (แสดงบนหน้า Home)
 *  - quiz_question   : ธนาคารโจทย์ (สุ่มด้วย ORDER BY RANDOM())
 *  - quiz_history    : คะแนนแต่ละครั้ง (แสดงบนหน้า History)
 */
class PySchoolDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, PySchoolContract.DB_NAME, null, PySchoolContract.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        createLessonProgressTable(db)
        createQuestionTable(db)
        createHistoryTable(db)
        seedLessonProgress(db)
        seedQuestions(db)
    }

    /**
     * โจทย์เป็นข้อมูลที่เรา "เป็นเจ้าของ" จึงสร้างใหม่ได้ ส่วน history เป็นข้อมูลผู้ใช้จึงต้องเก็บไว้
     * (ถ้าแก้ schema ของ history ในอนาคตให้เพิ่ม ALTER TABLE ที่นี่)
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${QuestionTable.TABLE}")
        createQuestionTable(db)
        seedQuestions(db)
    }

    // ---------- DDL ----------

    private fun createLessonProgressTable(db: SQLiteDatabase) = db.execSQL(
        """
        CREATE TABLE ${LessonProgressTable.TABLE} (
            ${LessonProgressTable.ID} INTEGER PRIMARY KEY,
            ${LessonProgressTable.PROGRESS} INTEGER NOT NULL DEFAULT 0
                CHECK (${LessonProgressTable.PROGRESS} BETWEEN 0 AND 100),
            ${LessonProgressTable.UPDATED_AT} INTEGER NOT NULL DEFAULT 0
        )
        """.trimIndent()
    )

    private fun createQuestionTable(db: SQLiteDatabase) = db.execSQL(
        """
        CREATE TABLE ${QuestionTable.TABLE} (
            ${QuestionTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${QuestionTable.TOPIC} TEXT NOT NULL,
            ${QuestionTable.QUESTION} TEXT NOT NULL,
            ${QuestionTable.CODE} TEXT,
            ${QuestionTable.OPTION_A} TEXT NOT NULL,
            ${QuestionTable.OPTION_B} TEXT NOT NULL,
            ${QuestionTable.OPTION_C} TEXT NOT NULL,
            ${QuestionTable.OPTION_D} TEXT NOT NULL,
            ${QuestionTable.CORRECT_INDEX} INTEGER NOT NULL CHECK (${QuestionTable.CORRECT_INDEX} BETWEEN 0 AND 3),
            ${QuestionTable.EXPLANATION} TEXT NOT NULL
        )
        """.trimIndent()
    )

    private fun createHistoryTable(db: SQLiteDatabase) = db.execSQL(
        """
        CREATE TABLE ${HistoryTable.TABLE} (
            ${HistoryTable.ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${HistoryTable.SCORE} INTEGER NOT NULL,
            ${HistoryTable.TOTAL} INTEGER NOT NULL CHECK (${HistoryTable.TOTAL} > 0),
            ${HistoryTable.TAKEN_AT} INTEGER NOT NULL
        )
        """.trimIndent()
    )

    // ---------- Seed ----------

    private fun seedLessonProgress(db: SQLiteDatabase) {
        LessonCatalog.lessons.forEach { lesson ->
            val values = ContentValues().apply {
                put(LessonProgressTable.ID, lesson.id)
                put(LessonProgressTable.PROGRESS, 0)
                put(LessonProgressTable.UPDATED_AT, 0)
            }
            db.insert(LessonProgressTable.TABLE, null, values)
        }
    }

    private fun seedQuestions(db: SQLiteDatabase) {
        QUESTION_SEEDS.forEach { seed ->
            require(seed.options.size == 4) { "Question needs exactly 4 options: ${seed.question}" }
            val values = ContentValues().apply {
                put(QuestionTable.TOPIC, seed.topic)
                put(QuestionTable.QUESTION, seed.question)
                put(QuestionTable.CODE, seed.code)
                put(QuestionTable.OPTION_A, seed.options[0])
                put(QuestionTable.OPTION_B, seed.options[1])
                put(QuestionTable.OPTION_C, seed.options[2])
                put(QuestionTable.OPTION_D, seed.options[3])
                put(QuestionTable.CORRECT_INDEX, seed.correctIndex)
                put(QuestionTable.EXPLANATION, seed.explanation)
            }
            db.insert(QuestionTable.TABLE, null, values)
        }
    }

    companion object {
        @Volatile private var instance: PySchoolDbHelper? = null

        fun getInstance(context: Context): PySchoolDbHelper =
            instance ?: synchronized(this) {
                instance ?: PySchoolDbHelper(context).also { instance = it }
            }
    }
}
