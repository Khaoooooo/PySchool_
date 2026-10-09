package com.pyschool.app.data.repository

import android.content.ContentValues
import android.database.Cursor
import com.pyschool.app.data.db.PySchoolContract.HistoryTable as H
import com.pyschool.app.data.db.PySchoolContract.QuestionTable as Q
import com.pyschool.app.data.db.DbDispatcher
import com.pyschool.app.data.db.PySchoolDbHelper
import com.pyschool.app.data.model.HistorySummary
import com.pyschool.app.data.model.QuizAttempt
import com.pyschool.app.data.model.QuizQuestion
import kotlinx.coroutines.withContext

class QuizRepository(private val dbHelper: PySchoolDbHelper) {

    /** สุ่มโจทย์จาก SQLite ทุกครั้งที่เริ่มควิซ */
    suspend fun getRandomQuestions(count: Int = QUESTIONS_PER_QUIZ): List<QuizQuestion> =
        withContext(DbDispatcher.io) {
            dbHelper.readableDatabase
                .rawQuery("SELECT * FROM ${Q.TABLE} ORDER BY RANDOM() LIMIT ?", arrayOf(count.toString()))
                .use { c -> buildList { while (c.moveToNext()) add(c.toQuestion()) } }
        }

    suspend fun saveAttempt(score: Int, total: Int): Long = withContext(DbDispatcher.io) {
        val values = ContentValues().apply {
            put(H.SCORE, score)
            put(H.TOTAL, total)
            put(H.TAKEN_AT, System.currentTimeMillis())
        }
        dbHelper.writableDatabase.insertOrThrow(H.TABLE, null, values)
    }

    /** รายการล่าสุดก่อน (หน้า History) */
    suspend fun getRecentAttempts(limit: Int = 20): List<QuizAttempt> = withContext(DbDispatcher.io) {
        dbHelper.readableDatabase
            .query(H.TABLE, null, null, null, null, null, "${H.TAKEN_AT} DESC", limit.toString())
            .use { c ->
                val id = c.getColumnIndexOrThrow(H.ID)
                val score = c.getColumnIndexOrThrow(H.SCORE)
                val total = c.getColumnIndexOrThrow(H.TOTAL)
                val at = c.getColumnIndexOrThrow(H.TAKEN_AT)
                buildList { while (c.moveToNext()) add(QuizAttempt(c.getLong(id), c.getInt(score), c.getInt(total), c.getLong(at))) }
            }
    }

    suspend fun getSummary(): HistorySummary = withContext(DbDispatcher.io) {
        dbHelper.readableDatabase.rawQuery(
            "SELECT COUNT(*), COALESCE(MAX(${H.SCORE} * 100 / ${H.TOTAL}), 0) FROM ${H.TABLE}", null
        ).use { c ->
            c.moveToFirst()
            HistorySummary(bestPercent = c.getInt(1), attemptCount = c.getInt(0))
        }
    }

    private fun Cursor.toQuestion() = QuizQuestion(
        id = getLong(getColumnIndexOrThrow(Q.ID)),
        topic = getString(getColumnIndexOrThrow(Q.TOPIC)),
        question = getString(getColumnIndexOrThrow(Q.QUESTION)),
        code = getString(getColumnIndexOrThrow(Q.CODE)),
        options = listOf(Q.OPTION_A, Q.OPTION_B, Q.OPTION_C, Q.OPTION_D).map { getString(getColumnIndexOrThrow(it)) },
        correctIndex = getInt(getColumnIndexOrThrow(Q.CORRECT_INDEX)),
        explanation = getString(getColumnIndexOrThrow(Q.EXPLANATION)),
    )

    companion object {
        const val QUESTIONS_PER_QUIZ = 10
    }
}
