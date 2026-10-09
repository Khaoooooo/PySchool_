package com.pyschool.app

import android.app.Application
import com.pyschool.app.data.db.PySchoolDbHelper
import com.pyschool.app.data.repository.ProgressRepository
import com.pyschool.app.data.repository.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** จุดเดียวที่สร้าง dependency ของแอป (ขนาดโปรเจกต์นี้ไม่จำเป็นต้องใช้ DI framework) */
class PySchoolApp : Application() {
    /** scope ที่อยู่ตลอดอายุแอป สำหรับงานบันทึกข้อมูลที่ต้องเสร็จแม้ Activity ถูกปิดไปแล้ว */
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val dbHelper by lazy { PySchoolDbHelper.getInstance(this) }
    val progressRepository by lazy { ProgressRepository(dbHelper) }
    val quizRepository by lazy { QuizRepository(dbHelper) }
}
