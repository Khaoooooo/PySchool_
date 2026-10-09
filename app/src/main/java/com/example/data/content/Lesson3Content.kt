package com.pyschool.app.data.content

import com.pyschool.app.data.content.Block.Code
import com.pyschool.app.data.content.Block.Mapping
import com.pyschool.app.data.content.Block.Paragraph
import com.pyschool.app.data.content.Block.Tip

/** บทที่ 3 — Control Flow */
object Lesson3Content {
    val content = LessonContent(
        title = "Control Flow",
        subtitle = "Make Python decide and repeat.",
        sections = listOf(
            Section(
                "if / else",
                listOf(
                    Paragraph("An if statement runs code only when its condition is True. Add else for the other case."),
                    Code(
                        "age = 20\n\nif age >= 18:\n    print(\"Adult\")\nelse:\n    print(\"Child\")",
                        "Adult",
                    ),
                    Tip("Python uses indentation (4 spaces) to know which lines belong inside the if."),
                ),
            ),
            Section(
                "elif: more than two choices",
                listOf(
                    Paragraph("Python checks conditions from top to bottom and runs only the first branch that is True."),
                    Code(
                        "score = 72\n\nif score >= 80:\n    print(\"Grade A\")\nelif score >= 60:\n    print(\"Grade B\")\nelse:\n    print(\"Try again\")",
                        "Grade B",
                    ),
                ),
            ),
            Section(
                "for loop",
                listOf(
                    Paragraph("A for loop repeats code a known number of times, or once for each item in a collection."),
                    Code("for i in range(5):\n    print(i)", "0\n1\n2\n3\n4"),
                    Mapping(
                        listOf(
                            "range(5)" to "0, 1, 2, 3, 4",
                            "range(1, 4)" to "1, 2, 3",
                            "range(0, 10, 2)" to "0, 2, 4, 6, 8",
                        )
                    ),
                    Code(
                        "fruits = [\"apple\", \"banana\"]\n\nfor fruit in fruits:\n    print(fruit)",
                        "apple\nbanana",
                    ),
                ),
            ),
            Section(
                "while loop",
                listOf(
                    Paragraph("A while loop keeps repeating as long as its condition stays True."),
                    Code("x = 0\n\nwhile x < 3:\n    print(x)\n    x += 1", "0\n1\n2"),
                    Tip("Always change something inside a while loop. If the condition never becomes False, the loop runs forever."),
                ),
            ),
            Section(
                "break and continue",
                listOf(
                    Paragraph("Use these two keywords to control a loop from the inside."),
                    Mapping(
                        listOf(
                            "break" to "Stop the loop completely",
                            "continue" to "Skip the rest of this round and go to the next one",
                        )
                    ),
                    Code(
                        "for i in range(1, 6):\n    if i == 3:\n        continue\n    if i == 5:\n        break\n    print(i)",
                        "1\n2\n4",
                    ),
                ),
            ),
        ),
    )
}
