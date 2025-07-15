This project is inspired by and adapted from the [Java implementation of the Lox interpreter](https://github.com/munificent/craftinginterpreters/tree/master/java/com/craftinginterpreters/lox) from the book *"Crafting Interpreters"* by Robert Nystrom. The provided framework was re-engineered to support the syntax and features of a subset of the GoLang language.

# GoLang Interpreter in Java

A multi-stage interpreter for a subset of the Go programming language, written entirely in Java. This project demonstrates a complete language processing pipeline, from lexical analysis to execution, including static analysis for variable resolution.

---

## Features

This interpreter supports a variety of core language features:

-   **Variable Declarations:** Handles both standard (`var x = 10`) and short-hand (`x := 10`) declarations, with support for explicit types (`var x int`).
-   **Arithmetic Operations:** Evaluates complex expressions with `+`, `-`, `*`, `/`, respecting the correct order of operations.
-   **Control Flow:** Full support for `if/else` conditional logic and `for` loops.
-   **Static Analysis:** Implements a Resolver to validate variable scopes and perform basic type-checking before execution, ensuring safer runtime behavior.
-   **Built-in Functions:** Includes support for standard I/O via `fmt.Println` and `fmt.Print`.

---

## Supported Syntax Example

The interpreter can successfully parse and execute code like the following:

```go
import "fmt"

var globalVar1 int = 10
var globalVar2 int = 5
var globalVar3 int = 15

func main() {
    if globalVar1 > globalVar2 {
        fmt.Println("globalVar1 is greater than globalVar2")
    } else {
        fmt.Println("globalVar2 is greater than or equal to globalVar1")
    }

    for i := 0; i < 5; i=i+1 {
        fmt.Println("Iteration ", i)
    }

    resultSum := globalVar1 + globalVar2
    var resultDiff = globalVar1 - globalVar2
    resultQuot := globalVar1 / globalVar2
    var resultProd int = globalVar1 * globalVar2
    var resultbodmas  =  globalVar1*globalVar2-globalVar3+globalVar1
    
    fmt.Println("Sum: ", resultSum)
    fmt.Println("Difference:  ", resultDiff)
    fmt.Println("Product: ", resultProd)
    fmt.Print("Quotient: ", resultQuot)
    fmt.Println("Hello, GoLang!")
    fmt.Println(" BODMAS: ", resultbodmas )
}
```

---

## How to Run

1.  **Compile the Java source files:**
    ```bash
    javac Golang/*.java
    ```
2.  **Run the interpreter with a source file:**
    ```bash
    java Golang.Main path/to/your/file.go
    ```
3.  **Run in interactive mode (REPL):**
    ```bash
    java Golang.Main
    ```

---

## Core Components

The interpreter is built with a classic multi-stage pipeline:

-   **`Scanner.java`:** The lexical analyzer, which takes raw source code and converts it into a stream of tokens.
-   **`Parser.java`:** A recursive-descent parser that takes the tokens and builds an Abstract Syntax Tree (AST) representing the code's structure.
-   **`Resolver.java`:** Performs a static analysis pass over the AST to resolve variable scopes, ensuring that all variables are declared and used correctly.
-   **`Interpreter.java`:** The execution engine, which traverses the AST to evaluate expressions and execute statements.

---

## Contributors

-   Anuj Sharma
-   Sai Madhav
-   Ashish Raj
-   Preet Bobde
-   Jyothiraditya
