package com.pyschool.app.data.content

/** ส่วนประกอบของเนื้อหา — เพิ่มชนิดใหม่ได้โดยแก้ที่ LessonRenderer ที่เดียว */
sealed interface Block {
    data class Paragraph(val text: String) : Block
    /** [output] คือผลลัพธ์ที่โปรแกรมพิมพ์ออกมา (ถ้ามี) */
    data class Code(val code: String, val output: String? = null) : Block
    /** การ์ดชนิดข้อมูลแบบ 2 คอลัมน์ (ชื่อ → ตัวอย่างค่า) */
    data class Chips(val items: List<Pair<String, String>>) : Block
    /** แถว "โค้ด → คำอธิบาย" */
    data class Mapping(val rows: List<Pair<String, String>>) : Block
    data class Tip(val text: String) : Block
}

data class Section(val title: String, val blocks: List<Block>)

data class LessonContent(val title: String, val subtitle: String, val sections: List<Section>)

object LessonContents {
    fun forLesson(lessonId: Int): LessonContent = when (lessonId) {
        1 -> Lesson1Content.content
        2 -> Lesson2Content.content
        3 -> Lesson3Content.content
        4 -> Lesson4Content.content
        else -> error("Unknown lesson id: $lessonId")
    }
}
