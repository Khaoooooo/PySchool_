package com.pyschool.app.data.content

import com.pyschool.app.data.content.Block.Code
import com.pyschool.app.data.content.Block.Mapping
import com.pyschool.app.data.content.Block.Paragraph
import com.pyschool.app.data.content.Block.Tip

/** บทที่ 4 — Functions */
object Lesson4Content {
    val content = LessonContent(
        title = "Functions",
        subtitle = "Write once. Use many times.",
        sections = listOf(
            Section(
                "What is a function?",
                listOf(
                    Paragraph("A function is a named block of code. Write it once with def, then run it whenever you need it by calling its name."),
                    Code(
                        "def greet():\n    print(\"Hello!\")\n\ngreet()\ngreet()",
                        "Hello!\nHello!",
                    ),
                ),
            ),
            Section(
                "Parameters and return",
                listOf(
                    Paragraph("Parameters are the inputs of a function. return sends the result back to the code that called it."),
                    Code(
                        "def add(a, b):\n    return a + b\n\nanswer = add(5, 3)\nprint(answer)",
                        "8",
                    ),
                    Mapping(
                        listOf(
                            "def" to "Create a function",
                            "add" to "The function name",
                            "(a, b)" to "Parameters (the inputs)",
                            "return" to "Send the result back",
                        )
                    ),
                ),
            ),
            Section(
                "Default values",
                listOf(
                    Paragraph("Give a parameter a default value and callers may leave it out."),
                    Code(
                        "def power(base, exponent=2):\n    return base ** exponent\n\nprint(power(4))\nprint(power(2, 5))",
                        "16\n32",
                    ),
                    Tip("Parameters with default values must come after the ones without."),
                ),
            ),
            Section(
                "print vs return",
                listOf(
                    Paragraph("print only shows a value on the screen. return hands the value back so the rest of your program can keep using it."),
                    Code(
                        "def double(n):\n    return n * 2\n\nresult = double(7)\nprint(result + 1)",
                        "15",
                    ),
                ),
            ),
            Section(
                "Variable scope",
                listOf(
                    Paragraph("A variable created inside a function exists only inside that function."),
                    Code(
                        "def show():\n    message = \"Inside\"\n    print(message)\n\nshow()\n# print(message) here would cause a NameError",
                        "Inside",
                    ),
                    Tip("Pass values in with parameters and send results out with return instead of sharing variables."),
                ),
            ),
        ),
    )
}
