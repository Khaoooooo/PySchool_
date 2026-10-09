package com.pyschool.app.data.repository

import android.content.ContentValues
import com.pyschool.app.data.db.PySchoolContract.LessonProgressTable as T
import com.pyschool.app.data.db.DbDispatcher
import com.pyschool.app.data.db.PySchoolDbHelper
import com.pyschool.app.data.model.LessonProgress
import kotlinx.coroutines.withContext

class ProgressRepository(private val dbHelper: PySchoolDbHelper) {

    suspend fun getAll(): List<LessonProgress> = withContext(DbDispatcher.io) {
        dbHelper.readableDatabase.query(T.TABLE, null, null, null, null, null, "${T.ID} ASC").use { c ->
            val idCol = c.getColumnIndexOrThrow(T.ID)
            val progressCol = c.getColumnIndexOrThrow(T.PROGRESS)
            buildList { while (c.moveToNext()) add(LessonProgress(c.getInt(idCol), c.getInt(progressCol))) }
        }
    }

    /** บันทึกความก้าวหน้า โดย "ไม่ลดลง" (เปิดบทเดิมซ้ำแล้วเปอร์เซ็นต์ต้องไม่ถอยหลัง) */
    suspend fun updateProgress(lessonId: Int, progress: Int) = withContext(DbDispatcher.io) {
        val value = progress.coerceIn(0, 100)
        val values = ContentValues().apply {
            put(T.PROGRESS, value)
            put(T.UPDATED_AT, System.currentTimeMillis())
        }
        dbHelper.writableDatabase.update(
            T.TABLE, values, "${T.ID} = ? AND ${T.PROGRESS} < ?", arrayOf(lessonId.toString(), value.toString())
        )
    }
}
