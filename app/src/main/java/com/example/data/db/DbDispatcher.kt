package com.pyschool.app.data.db

import java.util.concurrent.Executors
import kotlinx.coroutines.asCoroutineDispatcher

/**
 * ทุกงานฐานข้อมูลวิ่งบน thread เดียวตามลำดับที่ส่งเข้ามา (FIFO)
 * จึงรับประกันว่า "บันทึก progress ตอนออกจากบทเรียน" เสร็จก่อน "อ่าน progress ที่หน้า Home" เสมอ
 */
object DbDispatcher {
    val io = Executors.newSingleThreadExecutor { Thread(it, "pyschool-db") }.asCoroutineDispatcher()
}
