package com.pyschool.app.navigation

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.pyschool.app.R

/**
 * ตัวจัดการการเปลี่ยนหน้าทั้งแอป (Intent handler) — Activity ห้ามสร้าง Intent เอง
 *
 * กติกา back stack (ทำให้ปุ่ม Back คาดเดาได้):
 *  - Home เป็นราก เสมอ  → [Home] → [Home, Lesson] / [Home, Quiz] ...
 *  - สลับแท็บ: ปิดแท็บเดิมก่อนเปิดแท็บใหม่ ไม่ให้ stack พอก
 *  - Back to Home: CLEAR_TOP ล้างทุกหน้าที่อยู่เหนือ Home
 */
object AppNavigator {

    // ---- Intent extras (รวมคีย์ไว้ที่เดียว) ----
    const val EXTRA_SCORE = "extra_score"
    const val EXTRA_TOTAL = "extra_total"

    /** เปิดหน้าใหม่ทับหน้าปัจจุบัน; [finishCurrent] = true เมื่อไม่ต้องการให้ย้อนกลับมาหน้านี้ */
    fun open(
        from: Activity,
        screen: Screen,
        extras: Bundle? = null,
        finishCurrent: Boolean = false,
    ) {
        val intent = intentFor(from, screen).apply { extras?.let(::putExtras) }
        if (!start(from, intent)) return
        if (finishCurrent) from.finish()
    }

    /** กดแท็บใน Bottom Navigation */
    fun openTab(from: Activity, tab: Screen) {
        require(tab.isTab) { "$tab is not a bottom-navigation tab" }
        val current = Screen.of(from)
        if (current == tab) return
        if (tab == Screen.HOME) {
            goHome(from)
            return
        }
        // เปิดแท็บใหม่ก่อน แล้วปิดแท็บเดิม (ถ้าเป็นแท็บ) เพื่อให้ stack เหลือ [Home, tab]
        open(from, tab, finishCurrent = current?.isTab == true && current != Screen.HOME)
        from.overridePendingTransition(0, 0)
    }

    /** กลับ Home และล้างทุกหน้าที่ซ้อนอยู่ */
    fun goHome(from: Activity) {
        val intent = intentFor(from, Screen.HOME).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        start(from, intent)
    }

    /** Splash → Home โดยไม่ให้กด Back กลับมา Splash */
    fun finishSplash(from: Activity) = open(from, Screen.HOME, finishCurrent = true)

    // ---- Lessons ----

    /** บทถัดไป (บทสุดท้ายไป Visual Learn ตามโปรโตไทป์) แทนที่หน้าปัจจุบัน */
    fun nextLesson(from: Activity, current: Screen) {
        val index = Screen.lessons.indexOf(current)
        val next = Screen.lessons.getOrNull(index + 1) ?: Screen.VISUAL
        open(from, next, finishCurrent = true)
    }

    /** ปุ่ม Back ของบทเรียน: บทแรกกลับ Home, บทอื่นกลับบทก่อนหน้า */
    fun previousLesson(from: Activity, current: Screen) {
        val prev = Screen.lessons.getOrNull(Screen.lessons.indexOf(current) - 1)
        if (prev == null) from.finish() else open(from, prev, finishCurrent = true)
    }

    // ---- Quiz ----

    fun openQuizResult(from: Activity, score: Int, total: Int) {
        val extras = Bundle().apply {
            putInt(EXTRA_SCORE, score)
            putInt(EXTRA_TOTAL, total)
        }
        open(from, Screen.QUIZ_RESULT, extras, finishCurrent = true)
    }

    // ---- internal ----

    private fun intentFor(from: Activity, screen: Screen) =
        Intent().setComponent(ComponentName(from.packageName, screen.className))

    private fun start(from: Activity, intent: Intent): Boolean = try {
        from.startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(from, R.string.coming_soon, Toast.LENGTH_SHORT).show()
        false
    }
}
