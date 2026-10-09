package com.pyschool.app.data.content

import com.pyschool.app.data.content.Block.Code
import com.pyschool.app.data.content.Block.Mapping
import com.pyschool.app.data.content.Block.Paragraph
import com.pyschool.app.data.content.Block.Tip

/** บทที่ 2 — Operators */
object Lesson2Content {
    val content = LessonContent(
        title = "Operators",
        subtitle = "Tell Python what to do with values.",
        sections = listOf(
            Section(
                "Arithmetic operators",
                listOf(
                    Paragraph("Arithmetic operators do math with numbers."),
                    Mapping(
                        listOf(
                            "+" to "Add: 10 + 3 = 13",
                            "-" to "Subtract: 10 - 3 = 7",
                            "*" to "Multiply: 10 * 3 = 30",
                            "/" to "Divide, always gives a float: 10 / 4 = 2.5",
                            "//" to "Floor division, drops the decimals: 10 // 4 = 2",
                            "%" to "Remainder: 10 % 4 = 2",
                            "**" to "Power: 2 ** 3 = 8",
                        )
                    ),
                    Code(
                        "a = 10\nb = 4\n\nprint(a + b)\nprint(a / b)\nprint(a // b)\nprint(a % b)\nprint(2 ** 3)",
                        "14\n2.5\n2\n2\n8",
                    ),
                    Tip("n % 2 == 0 is a quick way to check whether n is an even number."),
                ),
            ),
            Section(
                "Assignment operators",
                listOf(
                    Paragraph("Shortcuts that change a variable using its own value."),
                    Mapping(
                        listOf(
                            "x += 5" to "Same as x = x + 5",
                            "x -= 2" to "Same as x = x - 2",
                            "x *= 3" to "Same as x = x * 3",
                        )
                    ),
                    Code("score = 10\nscore += 5\nscore *= 2\nprint(score)", "30"),
                ),
            ),
            Section(
                "Comparison operators",
                listOf(
                    Paragraph("Comparison operators compare two values and give back True or False."),
                    Mapping(
                        listOf(
                            "==" to "Equal to",
                            "!=" to "Not equal to",
                            ">" to "Greater than",
                            "<" to "Less than",
                            ">=" to "Greater than or equal to",
                            "<=" to "Less than or equal to",
                        )
                    ),
                    Code(
                        "age = 20\n\nprint(age >= 18)\nprint(age == 21)\nprint(age != 21)",
                        "True\nFalse\nTrue",
                    ),
                    Tip("A single = stores a value. A double == compares two values."),
                ),
            ),
            Section(
                "Logical operators",
                listOf(
                    Paragraph("Logical operators combine True and False values."),
                    Mapping(
                        listOf(
                            "and" to "True only if both sides are True",
                            "or" to "True if at least one side is True",
                            "not" to "Flips True to False and False to True",
                        )
                    ),
                    Code(
                        "age = 20\nhas_id = False\n\nprint(age >= 18 and has_id)\nprint(age >= 18 or has_id)\nprint(not has_id)",
                        "False\nTrue\nTrue",
                    ),
                ),
            ),
            Section(
                "Order of operations",
                listOf(
                    Paragraph("Python follows the same order as math: ** first, then * / // %, then + -. Use parentheses to choose the order yourself."),
                    Code(
                        "print(2 + 3 * 4)\nprint((2 + 3) * 4)\nprint(2 ** 3 * 2)",
                        "14\n20\n16",
                    ),
                ),
            ),
        ),
    )
}
