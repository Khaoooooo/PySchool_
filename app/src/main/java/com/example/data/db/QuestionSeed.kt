package com.pyschool.app.data.db

/** ธนาคารโจทย์เริ่มต้น (ครอบคลุม 4 บทเรียน) ใส่ลง DB ตอนสร้างฐานข้อมูล */
internal data class QuestionSeed(
    val topic: String,
    val question: String,
    val code: String?,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
)

internal val QUESTION_SEEDS = listOf(
    QuestionSeed("DATA TYPES", "What is the type of this value?", "x = 10",
        listOf("str", "int", "float", "bool"), 1, "int stores whole numbers."),
    QuestionSeed("DATA TYPES", "What is the type of this value?", "x = 3.14",
        listOf("int", "str", "float", "bool"), 2, "float stores numbers with a decimal point."),
    QuestionSeed("DATA TYPES", "What is the type of this value?", "x = \"Hello\"",
        listOf("str", "int", "float", "bool"), 0, "Text inside quotes is a str."),
    QuestionSeed("DATA TYPES", "What is the type of this value?", "x = True",
        listOf("str", "int", "float", "bool"), 3, "True and False are bool values."),
    QuestionSeed("VARIABLES", "What does this code store in age?", "age = 20",
        listOf("The text \"age\"", "The integer 20", "A function", "Nothing"), 1,
        "A variable stores a value so you can use it later."),
    QuestionSeed("OPERATORS", "What is the result?", "print(10 + 5)",
        listOf("105", "15", "5", "Error"), 1, "+ adds two numbers: 10 + 5 = 15."),
    QuestionSeed("OPERATORS", "What is the result?", "print(10 % 3)",
        listOf("3", "0", "1", "3.33"), 2, "% gives the remainder of a division."),
    QuestionSeed("OPERATORS", "What is the result?", "print(age >= 18)  # age = 20",
        listOf("True", "False", "20", "18"), 0, "20 is greater than or equal to 18, so it is True."),
    QuestionSeed("OPERATORS", "What is the result?", "print(True and False)",
        listOf("True", "False", "None", "Error"), 1, "and is True only when both sides are True."),
    QuestionSeed("CONTROL FLOW", "What will this print?", "age = 15\nif age >= 18:\n    print(\"Adult\")\nelse:\n    print(\"Child\")",
        listOf("Adult", "Child", "Nothing", "Error"), 1, "15 >= 18 is False, so Python runs the else branch."),
    QuestionSeed("CONTROL FLOW", "How many times does this loop run?", "for i in range(5):\n    print(i)",
        listOf("4", "5", "6", "Forever"), 1, "range(5) produces 0, 1, 2, 3, 4 — five values."),
    QuestionSeed("CONTROL FLOW", "What is the final value of x?", "x = 0\nwhile x < 3:\n    x += 1",
        listOf("2", "3", "4", "0"), 1, "The loop stops as soon as x < 3 becomes False, at x = 3."),
    QuestionSeed("FUNCTIONS", "Which keyword creates a function?", null,
        listOf("func", "define", "def", "fun"), 2, "Python uses def to define a function."),
    QuestionSeed("FUNCTIONS", "What does this print?", "def add(a, b):\n    return a + b\n\nprint(add(5, 3))",
        listOf("53", "8", "None", "Error"), 1, "return sends a + b back to the caller: 5 + 3 = 8."),
)
