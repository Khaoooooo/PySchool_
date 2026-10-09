package com.pyschool.app.data.content

import com.pyschool.app.data.content.Block.Chips
import com.pyschool.app.data.content.Block.Code
import com.pyschool.app.data.content.Block.Mapping
import com.pyschool.app.data.content.Block.Paragraph
import com.pyschool.app.data.content.Block.Tip

/** บทที่ 1 — Data Types & Variables */
object Lesson1Content {
    val content = LessonContent(
        title = "Data Types & Variables",
        subtitle = "Learn how Python stores information.",
        sections = listOf(
            Section(
                "What is a Data Type?",
                listOf(
                    Paragraph("A data type tells Python what kind of information a value contains. It also decides what you can do with that value."),
                    Chips(listOf("int" to "10", "float" to "3.14", "str" to "\"Hello\"", "bool" to "True")),
                    Mapping(
                        listOf(
                            "int" to "Whole numbers, such as 10 or -3",
                            "float" to "Numbers with a decimal point",
                            "str" to "Text inside quotes",
                            "bool" to "Only True or False",
                        )
                    ),
                ),
            ),
            Section(
                "Variables",
                listOf(
                    Paragraph("A variable is a name that points to a value, so you can use the value later. Use = to give a variable its value."),
                    Code(
                        "name = \"Mina\"\nage = 20\nscore = 85.5\nis_student = True\n\nprint(name)\nprint(age)",
                        "Mina\n20",
                    ),
                    Tip("A variable stores a value so you can use it later."),
                    Paragraph("Variable names must follow a few rules:"),
                    Mapping(
                        listOf(
                            "my_age" to "Valid: letters, digits and _ are allowed",
                            "2cool" to "Invalid: cannot start with a digit",
                            "my-age" to "Invalid: no hyphens or spaces",
                            "Age / age" to "Two different names: Python is case-sensitive",
                        )
                    ),
                ),
            ),
            Section(
                "Check a type with type()",
                listOf(
                    Paragraph("The built-in type() function tells you the data type of any value."),
                    Code(
                        "print(type(10))\nprint(type(3.14))\nprint(type(\"Hello\"))\nprint(type(True))",
                        "<class 'int'>\n<class 'float'>\n<class 'str'>\n<class 'bool'>",
                    ),
                    Paragraph("A variable can be given a new value at any time, even one of a different type."),
                    Code("x = 10\nx = \"ten\"\nprint(x)", "ten"),
                ),
            ),
            Section(
                "Convert between types",
                listOf(
                    Paragraph("Use int(), float() and str() to convert a value from one type to another."),
                    Code(
                        "age = int(\"20\")\nprice = float(\"9.5\")\ntext = str(100)\n\nprint(age + 1)\nprint(price * 2)\nprint(text + \"!\")",
                        "21\n19.0\n100!",
                    ),
                    Tip("Types do not mix automatically: \"5\" + 5 causes a TypeError because a str and an int cannot be added. Convert first."),
                ),
            ),
            Section(
                "Print with f-strings",
                listOf(
                    Paragraph("Put f before a string and write variables inside { } to build a message."),
                    Code(
                        "name = \"Mina\"\nage = 20\nprint(f\"{name} is {age} years old\")",
                        "Mina is 20 years old",
                    ),
                ),
            ),
        ),
    )
}
