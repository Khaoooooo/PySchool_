package com.pyschool.app.data.content

/**
 * หนึ่งขั้นของการจำลองโค้ด
 * @param activeLine บรรทัดโค้ดที่กำลังทำงาน (นับจาก 0)
 * @param memory     ตัวแปรในหน่วยความจำ "หลัง" ขั้นนี้เสร็จ (ชื่อ → ค่า)
 * @param output     ข้อความที่โปรแกรมพิมพ์ออกมาจนถึงขั้นนี้
 */
data class VisualStep(
    val activeLine: Int,
    val memory: List<Pair<String, String>>,
    val output: List<String>,
    val note: String,
)

data class VisualScenario(
    val tabLabel: String,
    val code: String,
    val introNote: String,
    val steps: List<VisualStep>,
)

object VisualScenarios {
    val all: List<VisualScenario> = listOf(
        VisualScenario(
            tabLabel = "Variables",
            code = "name = \"Mina\"\nage = 20\nage = age + 1",
            introNote = "Press Run to store the first value.",
            steps = listOf(
                VisualStep(0, listOf("name" to "\"Mina\""), emptyList(),
                    "A new variable name is created and stores the text \"Mina\"."),
                VisualStep(1, listOf("name" to "\"Mina\"", "age" to "20"), emptyList(),
                    "age is created and stores the integer 20."),
                VisualStep(2, listOf("name" to "\"Mina\"", "age" to "21"), emptyList(),
                    "Python calculates 20 + 1 first, then stores 21 back into age."),
            ),
        ),
        VisualScenario(
            tabLabel = "If / Else",
            code = "age = 20\nif age >= 18:\n    print(\"Adult\")\nelse:\n    print(\"Child\")",
            introNote = "Which path will Python choose?",
            steps = listOf(
                VisualStep(0, listOf("age" to "20"), emptyList(),
                    "age stores 20."),
                VisualStep(1, listOf("age" to "20"), emptyList(),
                    "Is 20 >= 18? The condition is True, so Python enters the if block."),
                VisualStep(2, listOf("age" to "20"), listOf("Adult"),
                    "The if block runs and prints Adult. The else block is skipped."),
            ),
        ),
        VisualScenario(
            tabLabel = "Loop",
            code = "total = 0\nfor i in range(3):\n    total += i\nprint(total)",
            introNote = "Watch i and total change on every round.",
            steps = listOf(
                VisualStep(0, listOf("total" to "0"), emptyList(),
                    "total starts at 0."),
                VisualStep(1, listOf("total" to "0", "i" to "0"), emptyList(),
                    "Round 1: i takes the first value of range(3), which is 0."),
                VisualStep(2, listOf("total" to "0", "i" to "0"), emptyList(),
                    "total += i means total = 0 + 0, so total stays 0."),
                VisualStep(1, listOf("total" to "0", "i" to "1"), emptyList(),
                    "Round 2: i becomes 1."),
                VisualStep(2, listOf("total" to "1", "i" to "1"), emptyList(),
                    "total = 0 + 1 = 1."),
                VisualStep(1, listOf("total" to "1", "i" to "2"), emptyList(),
                    "Round 3: i becomes 2, the last value of range(3)."),
                VisualStep(2, listOf("total" to "3", "i" to "2"), emptyList(),
                    "total = 1 + 2 = 3."),
                VisualStep(3, listOf("total" to "3", "i" to "2"), listOf("3"),
                    "The loop is finished, so print(total) shows 3."),
            ),
        ),
    )
}
