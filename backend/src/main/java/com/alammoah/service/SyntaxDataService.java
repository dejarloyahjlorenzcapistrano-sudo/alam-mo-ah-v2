package com.alammoah.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SyntaxDataService {

    // ── Data Models ────────────────────────────────────────────────────────────

    public record SyntaxEntry(
        String id, String title, String syntax, String description,
        String usage, String example, List<String> tags
    ) {}

    public record Category(String title, List<SyntaxEntry> entries) {}

    public record Language(
        String id, String name, String color, String icon,
        String description, Map<String, Category> categories
    ) {}

    public record LanguageSummary(
        String id, String name, String color, String icon,
        String description, int categoryCount, int entryCount
    ) {}

    public record SearchResult(
        String id, String title, String syntax, String description,
        String usage, String example, List<String> tags,
        String language, String languageId, String languageColor,
        String languageIcon, String category
    ) {}

    // ── Data ───────────────────────────────────────────────────────────────────

    private final Map<String, Language> data = buildData();

    public List<LanguageSummary> getAllLanguages() {
        return data.values().stream().map(lang -> new LanguageSummary(
            lang.id(), lang.name(), lang.color(), lang.icon(), lang.description(),
            lang.categories().size(),
            lang.categories().values().stream().mapToInt(c -> c.entries().size()).sum()
        )).collect(Collectors.toList());
    }

    public Language getLanguage(String id) {
        return data.get(id);
    }

    public List<SearchResult> search(String q) {
        if (q == null || q.isBlank()) return List.of();
        String query = q.toLowerCase().trim();

        List<SearchResult> results = new ArrayList<>();
        for (Language lang : data.values()) {
            for (Map.Entry<String, Category> catEntry : lang.categories().entrySet()) {
                for (SyntaxEntry entry : catEntry.getValue().entries()) {
                    String searchable = String.join(" ",
                        entry.title(), entry.description(), entry.syntax(),
                        entry.usage() != null ? entry.usage() : "",
                        String.join(" ", entry.tags()), lang.name(),
                        catEntry.getValue().title()
                    ).toLowerCase();

                    if (searchable.contains(query)) {
                        results.add(new SearchResult(
                            entry.id(), entry.title(), entry.syntax(), entry.description(),
                            entry.usage(), entry.example(), entry.tags(),
                            lang.name(), lang.id(), lang.color(), lang.icon(),
                            catEntry.getValue().title()
                        ));
                    }
                }
            }
        }
        return results;
    }

    // ── Build Syntax Data ─────────────────────────────────────────────────────

    private Map<String, Language> buildData() {
        Map<String, Language> map = new LinkedHashMap<>();

        // ── JavaScript ──────────────────────────────────────────────────────────
        map.put("javascript", new Language("javascript", "JavaScript", "#F7DF1E", "JS",
            "The language of the web. Runs everywhere, breaks everywhere.",
            Map.of(
                "variables", new Category("Variables & Data Types", List.of(
                    new SyntaxEntry("js-let", "let", "let variableName = value;",
                        "Block-scoped variable. Can be reassigned. Preferred over var.",
                        "Use when the value will change over time, like a counter.",
                        "let score = 0;\nscore = 10;\nlet name = \"Juan\";\nconsole.log(score); // 10",
                        List.of("variables", "beginner")),
                    new SyntaxEntry("js-const", "const", "const NAME = value;",
                        "Block-scoped constant. Cannot be reassigned after declaration.",
                        "Use for values that should never change.",
                        "const PI = 3.14159;\nconst APP = \"Alam Mo Ah\";\n// PI = 4; // TypeError",
                        List.of("variables", "beginner")),
                    new SyntaxEntry("js-template", "Template Literals", "`Hello, ${expression}!`",
                        "String with embedded expressions using backticks.",
                        "Use instead of + concatenation. Much cleaner.",
                        "const name = \"Juan\";\nconst age = 20;\nconsole.log(`Hello, ${name}! Age: ${age}`);",
                        List.of("strings", "beginner")),
                    new SyntaxEntry("js-destructuring", "Destructuring",
                        "const { key } = object;\nconst [a, b] = array;",
                        "Unpack values from arrays or objects into variables.",
                        "Use to cleanly extract multiple values at once.",
                        "const student = { name: \"Maria\", grade: \"A\" };\nconst { name, grade } = student;\nconsole.log(name); // Maria",
                        List.of("variables", "intermediate"))
                )),
                "functions", new Category("Functions", List.of(
                    new SyntaxEntry("js-function", "Function Declaration",
                        "function name(params) {\n  return value;\n}",
                        "Named function. Hoisted — can be called before declaration.",
                        "Use for reusable blocks of logic.",
                        "function greet(name, greeting = \"Hello\") {\n  return `${greeting}, ${name}!`;\n}\nconsole.log(greet(\"Juan\")); // Hello, Juan!",
                        List.of("functions", "beginner")),
                    new SyntaxEntry("js-arrow", "Arrow Function",
                        "const name = (params) => expression;",
                        "Concise function syntax. No own `this` binding.",
                        "Use for short functions, callbacks, array methods.",
                        "const add = (a, b) => a + b;\nconst square = x => x * x;\nconsole.log(add(3, 4)); // 7\nconsole.log(square(5)); // 25",
                        List.of("functions", "beginner")),
                    new SyntaxEntry("js-async", "async / await",
                        "const fn = async () => {\n  const result = await promise;\n};",
                        "Handle asynchronous code cleanly without callback hell.",
                        "Use whenever waiting for: API calls, file reads, timers.",
                        "const getUser = async (id) => {\n  try {\n    const res = await fetch(`/api/users/${id}`);\n    const user = await res.json();\n    return user;\n  } catch (err) {\n    console.error(err);\n  }\n};",
                        List.of("async", "intermediate"))
                )),
                "arrays", new Category("Array Methods", List.of(
                    new SyntaxEntry("js-map", ".map()",
                        "const newArr = array.map(item => transformation);",
                        "Creates a NEW array by transforming each element.",
                        "Use when you want to transform every item in an array.",
                        "const grades = [78, 90, 65];\nconst boosted = grades.map(g => g + 5);\nconsole.log(boosted); // [83, 95, 70]",
                        List.of("arrays", "intermediate")),
                    new SyntaxEntry("js-filter", ".filter()",
                        "const filtered = array.filter(item => condition);",
                        "Returns new array with only elements that pass the test.",
                        "Use to keep only items matching a condition.",
                        "const scores = [45, 78, 90, 33, 60];\nconst passed = scores.filter(s => s >= 75);\nconsole.log(passed); // [78, 90]",
                        List.of("arrays", "intermediate")),
                    new SyntaxEntry("js-reduce", ".reduce()",
                        "const result = array.reduce((acc, item) => expr, initial);",
                        "Reduces array to a single value by accumulating.",
                        "Use to sum, count, or build objects from arrays.",
                        "const scores = [85, 90, 78];\nconst total = scores.reduce((sum, s) => sum + s, 0);\nconsole.log(total); // 253",
                        List.of("arrays", "intermediate")),
                    new SyntaxEntry("js-find", ".find() / .findIndex()",
                        "const item = array.find(item => condition);\nconst idx = array.findIndex(item => condition);",
                        "find() returns first match. findIndex() returns its index.",
                        "Use to locate a specific item, like finding a user by ID.",
                        "const students = [\n  { id: 1, name: \"Juan\" },\n  { id: 2, name: \"Maria\" }\n];\nconst found = students.find(s => s.id === 2);\nconsole.log(found.name); // Maria",
                        List.of("arrays", "intermediate"))
                )),
                "conditionals", new Category("Conditionals", List.of(
                    new SyntaxEntry("js-if", "if / else if / else",
                        "if (condition) { } else if (condition) { } else { }",
                        "Execute code based on whether conditions are true or false.",
                        "Use === for strict equality in JavaScript.",
                        "const grade = 85;\nif (grade >= 90) {\n  console.log(\"A\");\n} else if (grade >= 80) {\n  console.log(\"B\");\n} else {\n  console.log(\"F\");\n}",
                        List.of("conditionals", "beginner")),
                    new SyntaxEntry("js-ternary", "Ternary Operator",
                        "condition ? valueIfTrue : valueIfFalse",
                        "Shorthand for a simple if/else that returns a value.",
                        "Use for simple one-line conditions. Avoid nesting.",
                        "const score = 80;\nconst result = score >= 75 ? \"Passed\" : \"Failed\";\nconsole.log(result); // Passed",
                        List.of("conditionals", "beginner"))
                )),
                "loops", new Category("Loops", List.of(
                    new SyntaxEntry("js-for", "for loop",
                        "for (let i = 0; i < length; i++) { }",
                        "Classic loop with initializer, condition, and incrementer.",
                        "Use when you need the index or loop a specific number of times.",
                        "for (let i = 1; i <= 5; i++) {\n  console.log(i);\n}\n\nconst fruits = [\"mango\", \"banana\"];\nfor (let i = 0; i < fruits.length; i++) {\n  console.log(`${i}: ${fruits[i]}`);\n}",
                        List.of("loops", "beginner")),
                    new SyntaxEntry("js-forof", "for...of",
                        "for (const item of iterable) { }",
                        "Loops over values of any iterable: array, string, Set, Map.",
                        "Cleanest way to loop when you do not need the index.",
                        "const scores = [85, 92, 78];\nfor (const score of scores) {\n  console.log(score >= 75 ? \"Pass\" : \"Fail\");\n}",
                        List.of("loops", "beginner"))
                )),
                "objects", new Category("Objects & Classes", List.of(
                    new SyntaxEntry("js-object", "Object Literals",
                        "const obj = { key: value, method() { } };",
                        "Key-value pairs grouping related data and behavior.",
                        "Use to represent a thing with multiple properties.",
                        "const student = {\n  name: \"Juan\",\n  grade: 85,\n  getResult() {\n    return this.grade >= 75 ? \"Pass\" : \"Fail\";\n  }\n};\nconsole.log(student.getResult()); // Pass",
                        List.of("objects", "beginner")),
                    new SyntaxEntry("js-class", "class",
                        "class Name {\n  constructor() {}\n  method() {}\n}",
                        "Blueprint for creating objects with shared methods.",
                        "Use when creating multiple objects of the same type.",
                        "class Student {\n  constructor(name) {\n    this.name = name;\n    this.grades = [];\n  }\n  addGrade(g) { this.grades.push(g); }\n  getAvg() {\n    return this.grades.reduce((a,b)=>a+b,0)/this.grades.length;\n  }\n}\nconst juan = new Student(\"Juan\");\njuan.addGrade(85);\njuan.addGrade(90);\nconsole.log(juan.getAvg()); // 87.5",
                        List.of("objects", "intermediate"))
                ))
            )
        ));

        // ── Python ─────────────────────────────────────────────────────────────
        map.put("python", new Language("python", "Python", "#3776AB", "PY",
            "Readable, powerful, and beloved. Also great for AI/ML.",
            Map.of(
                "basics", new Category("Basics", List.of(
                    new SyntaxEntry("py-print", "print()",
                        "print(value, ..., sep=\" \", end=\"\\n\")",
                        "Outputs text to console. Most-used function in Python.",
                        "Use to display output or debug. Always your first tool.",
                        "print(\"Hello, World!\")\nprint(\"a\", \"b\", \"c\", sep=\"-\")  # a-b-c\nprint(\"Loading\", end=\"\")  # no newline",
                        List.of("output", "beginner")),
                    new SyntaxEntry("py-input", "input()",
                        "value = input(\"prompt: \")",
                        "Reads user input from keyboard. Always returns a string.",
                        "Always convert type if you need numbers: int(), float().",
                        "name = input(\"Enter name: \")\nage = int(input(\"Enter age: \"))\nprint(f\"Hello, {name}! Next year: {age + 1}\")",
                        List.of("input", "beginner")),
                    new SyntaxEntry("py-fstring", "f-strings",
                        "f\"text {variable} text\"",
                        "Format strings with embedded expressions. Python 3.6+.",
                        "Preferred over .format() or %. Supports expressions inside {}.",
                        "name = \"Maria\"\ngpa = 1.75\nprint(f\"Hello, {name}!\")\nprint(f\"GPA: {gpa:.2f}\")  # 2 decimal places\nprint(f\"Age + 1: {19 + 1}\")",
                        List.of("strings", "beginner"))
                )),
                "functions", new Category("Functions", List.of(
                    new SyntaxEntry("py-def", "def",
                        "def function_name(params):\n    body\n    return value",
                        "Define a reusable function. Indentation defines the block.",
                        "One function = one job. Use default params for optional args.",
                        "def greet(name, greeting=\"Hello\"):\n    return f\"{greeting}, {name}!\"\n\nprint(greet(\"Juan\"))             # Hello, Juan!\nprint(greet(\"Maria\", \"Kamusta\")) # Kamusta, Maria!",
                        List.of("functions", "beginner")),
                    new SyntaxEntry("py-lambda", "lambda",
                        "lambda params: expression",
                        "Anonymous one-liner function. No return statement needed.",
                        "Best as arguments to sorted(), map(), filter().",
                        "square = lambda x: x ** 2\nstudents = [{\"name\":\"Juan\",\"gpa\":1.75},{\"name\":\"Maria\",\"gpa\":1.25}]\nsorted_s = sorted(students, key=lambda s: s[\"gpa\"])\nprint(sorted_s[0][\"name\"]) # Maria",
                        List.of("functions", "intermediate"))
                )),
                "lists", new Category("Lists & Comprehensions", List.of(
                    new SyntaxEntry("py-list", "List Basics",
                        "my_list = [item1, item2]\nmy_list.append(item)\nmy_list[index]",
                        "Ordered, mutable collection. Can hold mixed types.",
                        "Most used data structure in Python. Like arrays in other languages.",
                        "fruits = [\"mango\", \"banana\", \"ube\"]\nprint(fruits[0])   # mango\nprint(fruits[-1])  # ube\nfruits.append(\"lanzones\")\nprint(fruits[1:3]) # [\"banana\", \"ube\"]",
                        List.of("lists", "beginner")),
                    new SyntaxEntry("py-listcomp", "List Comprehension",
                        "[expression for item in iterable if condition]",
                        "Create a new list in one readable line. Very Pythonic.",
                        "Replace simple for loops that build lists.",
                        "squares = [x**2 for x in range(1, 6)]\nprint(squares)  # [1, 4, 9, 16, 25]\n\ngrades = [45, 78, 90, 33, 85]\npassing = [g for g in grades if g >= 75]\nprint(passing)  # [78, 90, 85]",
                        List.of("lists", "intermediate"))
                )),
                "control", new Category("Control Flow", List.of(
                    new SyntaxEntry("py-if", "if / elif / else",
                        "if condition:\n    pass\nelif condition:\n    pass\nelse:\n    pass",
                        "Conditional execution. Python uses elif not else if.",
                        "Indentation defines the block — no curly braces.",
                        "grade = 88\nif grade >= 90:\n    print(\"A\")\nelif grade >= 75:\n    print(\"Passing\")\nelse:\n    print(\"Failed\")\n\nresult = \"Pass\" if grade >= 75 else \"Fail\"",
                        List.of("control", "beginner")),
                    new SyntaxEntry("py-for", "for loop",
                        "for item in iterable:\n    body",
                        "Iterates over any iterable: list, string, range, dict.",
                        "Use enumerate() for index + value. range() for count-based loops.",
                        "fruits = [\"mango\", \"banana\"]\nfor fruit in fruits:\n    print(fruit)\n\nfor i, f in enumerate(fruits):\n    print(f\"{i+1}. {f}\")\n\nfor i in range(0, 10, 2):\n    print(i)  # 0 2 4 6 8",
                        List.of("loops", "beginner"))
                ))
            )
        ));

        // ── Java ───────────────────────────────────────────────────────────────
        map.put("java", new Language("java", "Java", "#ED8B00", "JV",
            "Write once, run anywhere. Verbose but rock-solid.",
            Map.of(
                "basics", new Category("Basics", List.of(
                    new SyntaxEntry("java-hello", "Hello World / main()",
                        "public static void main(String[] args) { }",
                        "Every Java program starts from the main method inside a class.",
                        "Class name must match filename. Entry point of every program.",
                        "public class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, World!\");\n    }\n}",
                        List.of("basics", "beginner")),
                    new SyntaxEntry("java-output", "System.out",
                        "System.out.println(value);\nSystem.out.printf(\"format\", args);",
                        "Three ways to print. println adds newline, print does not.",
                        "Use printf for formatted output. %s=string %d=int %.2f=float.",
                        "System.out.println(\"Hello!\");\nSystem.out.print(\"No newline\");\nSystem.out.printf(\"Name: %s, GPA: %.2f%n\", \"Juan\", 1.75);",
                        List.of("output", "beginner")),
                    new SyntaxEntry("java-variables", "Variables & Types",
                        "dataType variableName = value;",
                        "Java is statically typed. Must declare type for every variable.",
                        "int=whole, double=decimal, String=text, boolean=true/false.",
                        "int age = 20;\ndouble gpa = 1.75;\nchar grade = 'A';\nboolean enrolled = true;\nString name = \"Juan\";\nfinal double PI = 3.14; // constant",
                        List.of("variables", "beginner")),
                    new SyntaxEntry("java-scanner", "Scanner (Input)",
                        "Scanner sc = new Scanner(System.in);\nString s = sc.nextLine();\nint n = sc.nextInt();",
                        "Read keyboard input using the Scanner class from java.util.",
                        "Import Scanner first. nextLine() for strings, nextInt() for ints.",
                        "import java.util.Scanner;\n\nScanner sc = new Scanner(System.in);\nSystem.out.print(\"Name: \");\nString name = sc.nextLine();\nSystem.out.print(\"Age: \");\nint age = sc.nextInt();\nSystem.out.printf(\"Hello, %s! Age: %d%n\", name, age);\nsc.close();",
                        List.of("input", "beginner"))
                )),
                "control", new Category("Control Flow", List.of(
                    new SyntaxEntry("java-if", "if / else if / else",
                        "if (condition) { } else if (condition) { } else { }",
                        "Conditional branching. Conditions must be in parentheses.",
                        "Use == for primitives, .equals() for String comparison.",
                        "int score = 88;\nif (score >= 90) {\n    System.out.println(\"A\");\n} else if (score >= 75) {\n    System.out.println(\"Passing\");\n} else {\n    System.out.println(\"Failed\");\n}",
                        List.of("control", "beginner")),
                    new SyntaxEntry("java-for", "for / for-each",
                        "for (int i = 0; i < n; i++) { }\nfor (Type item : array) { }",
                        "Standard for and enhanced for-each for arrays/collections.",
                        "Use for-each when you don't need the index — cleaner.",
                        "// Standard:\nfor (int i = 1; i <= 5; i++) {\n    System.out.println(i);\n}\n// For-each:\nint[] grades = {85, 90, 78};\nfor (int g : grades) {\n    System.out.println(g >= 75 ? \"Pass\" : \"Fail\");\n}",
                        List.of("loops", "beginner"))
                )),
                "oop", new Category("OOP Concepts", List.of(
                    new SyntaxEntry("java-class", "Class & Objects",
                        "public class Name {\n  // fields, constructor, methods\n}",
                        "Blueprint for objects. Java is fully object-oriented.",
                        "One public class per file. Filename must match class name.",
                        "public class Student {\n    private String name;\n    private double gpa;\n\n    public Student(String name) {\n        this.name = name;\n        this.gpa = 0.0;\n    }\n\n    public String getName() { return name; }\n    public void setGpa(double gpa) { this.gpa = gpa; }\n\n    public String toString() {\n        return name + \" (GPA: \" + gpa + \")\";\n    }\n}",
                        List.of("oop", "intermediate")),
                    new SyntaxEntry("java-inheritance", "Inheritance",
                        "public class Child extends Parent { }",
                        "A class inherits fields and methods from a parent class.",
                        "Use super() to call parent constructor. @Override for method overriding.",
                        "public class Person {\n    protected String name;\n    public Person(String name) { this.name = name; }\n    public void speak() { System.out.println(name + \" speaks.\"); }\n}\n\npublic class Student extends Person {\n    private String course;\n    public Student(String name, String course) {\n        super(name);\n        this.course = course;\n    }\n    @Override\n    public void speak() {\n        System.out.println(name + \" studies \" + course);\n    }\n}",
                        List.of("oop", "intermediate")),
                    new SyntaxEntry("java-interface", "Interface",
                        "public interface Name {\n  void method();\n}\nclass Impl implements Name { }",
                        "Contract that a class must fulfill. All methods are abstract by default.",
                        "Use to define shared behaviour across unrelated classes.",
                        "public interface Printable {\n    void print();\n    default void preview() {\n        System.out.println(\"Previewing...\");\n    }\n}\n\npublic class Report implements Printable {\n    @Override\n    public void print() {\n        System.out.println(\"Printing report!\");\n    }\n}",
                        List.of("oop", "intermediate"))
                )),
                "arrays", new Category("Arrays", List.of(
                    new SyntaxEntry("java-array", "Arrays",
                        "type[] name = new type[size];\ntype[] name = {val1, val2};",
                        "Fixed-size collection of elements of the same type.",
                        "Use .length for size. Use ArrayList for dynamic sizing.",
                        "int[] scores = {85, 90, 78, 92};\nSystem.out.println(scores[0]);     // 85\nSystem.out.println(scores.length); // 4\n\nfor (int score : scores) {\n    System.out.println(score >= 75 ? \"Pass\" : \"Fail\");\n}",
                        List.of("arrays", "beginner"))
                ))
            )
        ));

        // ── SQL ────────────────────────────────────────────────────────────────
        map.put("sql", new Language("sql", "SQL", "#336791", "SQL",
            "Talk to your database. Every IT student needs this.",
            Map.of(
                "queries", new Category("Queries (SELECT)", List.of(
                    new SyntaxEntry("sql-select", "SELECT",
                        "SELECT columns FROM table WHERE condition ORDER BY col;",
                        "Retrieve data from a table. The most used SQL statement.",
                        "Use * to select all, or name columns to be precise.",
                        "SELECT * FROM students;\n\nSELECT name, course, gpa\nFROM students\nWHERE gpa <= 1.75\nORDER BY gpa ASC;\n\nSELECT * FROM students\nWHERE name LIKE 'Juan%';",
                        List.of("queries", "beginner")),
                    new SyntaxEntry("sql-aggregate", "Aggregate Functions",
                        "COUNT() SUM() AVG() MIN() MAX()\nGROUP BY col\nHAVING condition",
                        "Calculate summary values across groups of rows.",
                        "GROUP BY groups rows. HAVING filters groups after grouping.",
                        "SELECT course, COUNT(*) AS total\nFROM students\nGROUP BY course;\n\nSELECT course, ROUND(AVG(gpa),2) AS avg_gpa\nFROM students\nGROUP BY course\nHAVING AVG(gpa) <= 1.75;",
                        List.of("aggregates", "intermediate")),
                    new SyntaxEntry("sql-join", "JOINs",
                        "SELECT ... FROM t1\nINNER JOIN t2 ON t1.id = t2.t1_id",
                        "Combine rows from multiple tables based on related columns.",
                        "INNER = only matching. LEFT = all left + matching right.",
                        "SELECT s.name, c.title\nFROM students s\nINNER JOIN enrollments e ON s.id = e.student_id\nINNER JOIN courses c ON e.course_id = c.id;",
                        List.of("joins", "intermediate"))
                )),
                "ddl", new Category("DDL — Structure", List.of(
                    new SyntaxEntry("sql-create", "CREATE TABLE",
                        "CREATE TABLE name (\n  column type constraints\n);",
                        "Define a new table in the database.",
                        "Plan schema carefully. Use constraints for data integrity.",
                        "CREATE TABLE students (\n    id         INT PRIMARY KEY AUTO_INCREMENT,\n    student_no VARCHAR(20) UNIQUE NOT NULL,\n    name       VARCHAR(100) NOT NULL,\n    gpa        DECIMAL(3,2) DEFAULT 0.00\n);",
                        List.of("ddl", "beginner")),
                    new SyntaxEntry("sql-alter", "ALTER TABLE",
                        "ALTER TABLE name ADD column type;\nALTER TABLE name DROP COLUMN name;",
                        "Modify an existing table structure.",
                        "Use to add/remove columns or add constraints after creation.",
                        "ALTER TABLE students ADD COLUMN year_level INT DEFAULT 1;\nALTER TABLE students DROP COLUMN year_level;\nALTER TABLE students MODIFY COLUMN gpa DECIMAL(4,2);",
                        List.of("ddl", "intermediate"))
                )),
                "dml", new Category("DML — Data Changes", List.of(
                    new SyntaxEntry("sql-insert", "INSERT",
                        "INSERT INTO table (cols) VALUES (vals);",
                        "Add new rows to a table.",
                        "Always specify column names — safer if schema changes.",
                        "INSERT INTO students (student_no, name, course, gpa)\nVALUES ('2021-001', 'Juan dela Cruz', 'BSIT', 1.75);\n\nINSERT INTO students (student_no, name)\nVALUES ('2021-002', 'Maria'), ('2021-003', 'Pedro');",
                        List.of("dml", "beginner")),
                    new SyntaxEntry("sql-update", "UPDATE",
                        "UPDATE table SET col = val WHERE condition;",
                        "Modify rows. ALWAYS use WHERE or all rows get updated!",
                        "Test with SELECT first if unsure of your WHERE clause.",
                        "UPDATE students\nSET gpa = 1.50\nWHERE student_no = '2021-001';\n\n-- ⚠ No WHERE = updates EVERYTHING:\n-- UPDATE students SET gpa = 5.0;",
                        List.of("dml", "beginner")),
                    new SyntaxEntry("sql-delete", "DELETE",
                        "DELETE FROM table WHERE condition;",
                        "Remove rows. Permanent! Always double-check WHERE.",
                        "No WHERE = deletes all rows. Consider soft deletes instead.",
                        "DELETE FROM students WHERE id = 5;\nDELETE FROM grades WHERE score < 60;\n\n-- ⚠ Deletes ALL rows:\n-- DELETE FROM students;",
                        List.of("dml", "beginner"))
                ))
            )
        ));

        // ── C++ ────────────────────────────────────────────────────────────────
        map.put("cpp", new Language("cpp", "C++", "#00599C", "C++",
            "Close to the metal. Fast, powerful, and unforgiving.",
            Map.of(
                "basics", new Category("Basics", List.of(
                    new SyntaxEntry("cpp-hello", "Hello World",
                        "#include <iostream>\nusing namespace std;\nint main() { return 0; }",
                        "Every C++ program starts here. #include imports libraries.",
                        "Return 0 from main() to signal successful execution.",
                        "#include <iostream>\nusing namespace std;\n\nint main() {\n    cout << \"Hello, World!\" << endl;\n    return 0;\n}",
                        List.of("basics", "beginner")),
                    new SyntaxEntry("cpp-io", "cin / cout",
                        "cout << value << endl;\ncin >> variable;",
                        "cout = output. cin = input. Use << and >> operators.",
                        "Chain values with <<. endl or \\n for new lines.",
                        "string name;\nint age;\ncout << \"Enter name: \";\ncin >> name;\ncout << \"Enter age: \";\ncin >> age;\ncout << \"Hello, \" << name << \"! Age: \" << age << endl;",
                        List.of("io", "beginner")),
                    new SyntaxEntry("cpp-variables", "Variables & Types",
                        "int x = 5;\ndouble gpa = 1.75;\nstring name = \"Juan\";",
                        "C++ is statically typed. Include <string> for string type.",
                        "Use const for constants. Common: int, double, char, bool, string.",
                        "int age = 20;\ndouble gpa = 1.75;\nchar grade = 'A';\nbool enrolled = true;\nstring name = \"Juan\";\nconst int MAX = 100;",
                        List.of("variables", "beginner"))
                )),
                "functions", new Category("Functions", List.of(
                    new SyntaxEntry("cpp-functions", "Functions",
                        "returnType name(params) {\n  return value;\n}",
                        "Must be declared before use or use a prototype declaration.",
                        "Use void if returning nothing. Declare above main() or use prototypes.",
                        "#include <iostream>\nusing namespace std;\n\ndouble average(int a, int b);\n\nint main() {\n    cout << average(85, 90) << endl; // 87.5\n    return 0;\n}\n\ndouble average(int a, int b) {\n    return (double)(a + b) / 2;\n}",
                        List.of("functions", "beginner"))
                )),
                "pointers", new Category("Pointers & Arrays", List.of(
                    new SyntaxEntry("cpp-pointers", "Pointers",
                        "type* ptr = &variable;\n*ptr  // dereference\n&var  // address-of",
                        "Variables storing memory addresses. Powerful but error-prone.",
                        "& gets the address. * dereferences (value at the address).",
                        "int num = 42;\nint* ptr = &num;\n\ncout << num;   // 42  (value)\ncout << &num;  // 0x7... (address)\ncout << *ptr;  // 42  (dereference)\n\n*ptr = 100;\ncout << num;   // 100 (changed!)",
                        List.of("pointers", "advanced")),
                    new SyntaxEntry("cpp-arrays", "Arrays",
                        "type name[size];\ntype name[] = {val1, val2};",
                        "Fixed-size collection of same-type elements. Index starts at 0.",
                        "Size must be known at compile time. Use vectors for dynamic sizing.",
                        "int scores[] = {85, 90, 78, 92};\ncout << scores[0]; // 85\n\nfor (int i = 0; i < 4; i++) {\n    cout << scores[i] << \" \";\n}\n\n// Range-based for (C++11):\nfor (int s : scores) { cout << s << \" \"; }",
                        List.of("arrays", "beginner"))
                ))
            )
        ));

        // ── Data Structures ────────────────────────────────────────────────────
        map.put("dsa", new Language("dsa", "Data Structures", "#9b59b6", "DS",
            "Stacks, queues, sorting — the stuff that gets asked in exams.",
            Map.of(
                "stacks", new Category("Stack", List.of(
                    new SyntaxEntry("ds-stack", "Stack (Python)",
                        "stack = []\nstack.append(item)  # push\nstack.pop()         # pop\nstack[-1]           # peek",
                        "LIFO: Last In, First Out. Think of a stack of plates.",
                        "Use for undo operations, expression parsing, backtracking.",
                        "stack = []\nstack.append(\"page1\")\nstack.append(\"page2\")\nstack.append(\"page3\")\nprint(stack[-1])  # peek: page3\nlast = stack.pop()\nprint(last)   # page3\nprint(stack)  # ['page1', 'page2']",
                        List.of("stack", "dsa", "intermediate"))
                )),
                "queues", new Category("Queue", List.of(
                    new SyntaxEntry("ds-queue", "Queue (Python)",
                        "from collections import deque\nq = deque()\nq.append(item)   # enqueue\nq.popleft()      # dequeue",
                        "FIFO: First In, First Out. Think of a line at the cashier.",
                        "Use deque — more efficient than list for queue operations.",
                        "from collections import deque\nqueue = deque()\nqueue.append(\"student1\")\nqueue.append(\"student2\")\nfirst = queue.popleft()\nprint(first)  # student1\nprint(queue[0])  # peek: student2",
                        List.of("queue", "dsa", "intermediate"))
                )),
                "sorting", new Category("Sorting Algorithms", List.of(
                    new SyntaxEntry("ds-bubble", "Bubble Sort",
                        "for i in range(n):\n  for j in range(n-i-1):\n    if arr[j] > arr[j+1]: swap",
                        "Repeatedly swap adjacent elements if out of order. O(n²).",
                        "Good for learning. Too slow for large datasets.",
                        "def bubble_sort(arr):\n    n = len(arr)\n    for i in range(n):\n        for j in range(n - i - 1):\n            if arr[j] > arr[j + 1]:\n                arr[j], arr[j+1] = arr[j+1], arr[j]\n    return arr\n\nprint(bubble_sort([64,34,25,12,22]))\n# [12, 22, 25, 34, 64]",
                        List.of("sorting", "dsa", "intermediate")),
                    new SyntaxEntry("ds-binary-search", "Binary Search",
                        "low, high = 0, len(arr)-1\nwhile low <= high:\n  mid = (low+high)//2",
                        "Find an item in a SORTED array. O(log n) — much faster than linear.",
                        "Array MUST be sorted first. Eliminates half search space each step.",
                        "def binary_search(arr, target):\n    low, high = 0, len(arr) - 1\n    while low <= high:\n        mid = (low + high) // 2\n        if arr[mid] == target:\n            return mid\n        elif arr[mid] < target:\n            low = mid + 1\n        else:\n            high = mid - 1\n    return -1\n\nprint(binary_search([11,22,34,64,90], 34)) # 2",
                        List.of("search", "dsa", "intermediate"))
                ))
            )
        ));

        // ── HTML & CSS ─────────────────────────────────────────────────────────
        map.put("html_css", new Language("html_css", "HTML & CSS", "#E34F26", "WEB",
            "Structure and style. The foundation of every website.",
            Map.of(
                "html", new Category("HTML Basics", List.of(
                    new SyntaxEntry("html-structure", "Document Structure",
                        "<!DOCTYPE html>\n<html>\n  <head></head>\n  <body></body>\n</html>",
                        "The skeleton of every webpage. DOCTYPE declares HTML5.",
                        "Put metadata in <head>. All visible content goes in <body>.",
                        "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n    <meta charset=\"UTF-8\">\n    <title>My Page</title>\n    <link rel=\"stylesheet\" href=\"style.css\">\n</head>\n<body>\n    <h1>Hello!</h1>\n    <script src=\"script.js\"></script>\n</body>\n</html>",
                        List.of("html", "beginner")),
                    new SyntaxEntry("html-tags", "Common Tags",
                        "<tag>content</tag>",
                        "Building blocks of HTML. Prefer semantic tags.",
                        "Use header, nav, main, section, footer over generic divs.",
                        "<h1>Title</h1>\n<p>Paragraph with <strong>bold</strong> and <em>italic</em>.</p>\n<a href=\"https://google.com\" target=\"_blank\">Google</a>\n<img src=\"photo.jpg\" alt=\"Description\">\n<ul><li>Item</li></ul>\n<ol><li>Ordered</li></ol>",
                        List.of("html", "beginner")),
                    new SyntaxEntry("html-forms", "Forms",
                        "<form action=\"\" method=\"\">\n  <input type=\"text\">\n  <button type=\"submit\">\n</form>",
                        "Collect user input. Action = destination. Method = GET or POST.",
                        "Use POST for sensitive data. GET for searches.",
                        "<form action=\"/submit\" method=\"POST\">\n    <label for=\"name\">Name:</label>\n    <input type=\"text\" id=\"name\" name=\"name\" required>\n    <select name=\"course\">\n        <option value=\"bsit\">BSIT</option>\n        <option value=\"bscs\">BSCS</option>\n    </select>\n    <button type=\"submit\">Submit</button>\n</form>",
                        List.of("html", "forms", "beginner"))
                )),
                "css", new Category("CSS Basics", List.of(
                    new SyntaxEntry("css-selectors", "Selectors",
                        "element { }\n.class { }\n#id { }",
                        "Target HTML elements to apply styles. Specificity matters.",
                        "Classes are reusable. IDs are unique. Avoid over-specific selectors.",
                        "p { color: black; }\n.highlight { background: yellow; }\n#header { font-size: 24px; }\nnav a { text-decoration: none; }\na:hover { color: red; }",
                        List.of("css", "beginner")),
                    new SyntaxEntry("css-flexbox", "Flexbox",
                        "display: flex;\njustify-content: ...;\nalign-items: ...;",
                        "One-dimensional layout. Perfect for navbars, centering, cards.",
                        "Apply to parent. Children become flex items automatically.",
                        ".navbar {\n    display: flex;\n    justify-content: space-between;\n    align-items: center;\n    gap: 16px;\n}\n\n.centered {\n    display: flex;\n    justify-content: center;\n    align-items: center;\n    height: 100vh;\n}",
                        List.of("css", "layout", "intermediate")),
                    new SyntaxEntry("css-grid", "CSS Grid",
                        "display: grid;\ngrid-template-columns: ...;\ngap: ...;",
                        "Two-dimensional layout system. Great for page layouts and grids.",
                        "Use grid for 2D (rows AND columns). Flexbox for 1D (row OR column).",
                        ".card-grid {\n    display: grid;\n    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));\n    gap: 24px;\n}\n\n.layout {\n    display: grid;\n    grid-template-columns: 240px 1fr;\n    min-height: 100vh;\n}",
                        List.of("css", "layout", "intermediate"))
                ))
            )
        ));

        return map;
    }
}
