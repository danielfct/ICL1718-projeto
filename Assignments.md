# Interpretation and compilation of Programming Languages 2017-18

## Lab Assignments

This is the list of lab assignments for the course. We first have the complete list of assignmetns and then the details of each one. This list and details will be updated every week with new adjustments and details.

### Planning

>**Set 11-15** - Parsing of an expression language.

>**Set 18-22** - Interpretation of an expression language. Dynamic typing.

>**Set 25-29** - Static typing of an expression language.

>**Out 2-6**  - Compiling arithmetic expressions to the JVM.

>**Project assignment: announcement**

>**Out 9-13** - Type-directed compilation. Compiling boolean expressions to the JVM.

>**Out 16-20** - Implementing an interpreter with binding and scope.

>**Project assignment: 1st phase** (exact date awaiting approval)

>**Out 23-27** - Implementing a compiler with binding and scope.

>**Out 30 - Nov 3** - Implementing imperative Languages: Parsing and interpretation.

>**Nov 6-10**  - Functional Languages: semantics and interpretation. Static vs Dynamic Name resolution.

>**Nov 13-17** - Functional Languages: compilation

>**Nov 20-24** - Records and Objects: typing and type-directed compilation

>**Nov 27-30** - Records and Objects: recursive types

>**Dec 4-8**   - Evaluation strategies (Lazy languages)

>**Project assignment: submission** (exact date awaiting approval)

>**Dec 11-15** - Project Presentation and Discussion



## Lab 3 - Static typing of expressions. Compiling to the JVM

This Lab is focused on the implementation of a type checker for the expression language, and in the first compiler to the JVM. This lab starts with the definition of two semantic functions. The first semantic function will associate a type to all expressions of the language, the second one will have as result a sequence of JVM instructions that should have the same behaviour as the interpreter for the same expression.

### Starter code for typing

Commit, sync, and pull the new code to your local repository. The typing function has one of three possible outcomes, `int`, `bool`, and type `none` which can be represented by throwing an exception. Follow the same approach taken to implement values, and implement a sum type for types. Consider the interface `IType` and the new method in interface `ASTNode` named `typecheck`.

### Starter code for compiling

Commit, sync, and pull the new code to your local repository. uncomment the compile method in ASTNode.

### Goal 1

To implement the typechecker and intersect the execution of every ill-typed program.

### Goal 2

Make some more tests for your typechecker for both positive and negative cases

### Goal 3

To implement the compiler for the language

### Goal 4

Test your compiler... make sure that you are running the compiled code.



## Lab 2 - Interpretation of an expression. Dynamic Typing.

This lab assignment is focused on the implementation of an interpreter for the expression language developed in Lab 1. We will define an abstract representation for our programs (expressions) and will define an interpretation function on all objects.

### Git tip

In order to proceed we first need to commit the work done so far. To do that use the following command

```
git commit -a -m "Lab 1"
```

This means that you will commit all tracked files (option `-a`) and use a message "Lab 1" (option `-m`). You can also use the GUI provided by Atlassian's application [`SourceTree`](https://www.sourcetreeapp.com/).

The next action is to synchronize your repository (`Sync` button in the bitbucket website), and pull the modifications through the command

```
git pull
```

### Starter code

Consider the modifications introduced in file `Parser.jj` and by the classes defined in package `ast`. You must now modify the `Console` class to use the `Parser` class instead the `SimpleParser` class. Notice that the function `Start` (and the other non-terminal functions) now returns an object representing the expression. The object is of type `ASTNode` and implemented by the classes added in package `ast`.

### Semantic Functions

Study the `ASTNode` interface and the functions it declares. An evaluation function is provided to define the structural operational semantics of the language. The testing infrastructure was also extended to test the language semantics.

### Part 1

Extend the evaluation function to all operators over boolean values introduced in Lab 1. In order to return boolean values, you should change the return value of function `eval` to an interface `IValue` implemented by two classes `IntValue` and `BoolValue`.

### Part 2

Detect erroneous situation and abort the evaluation if they occur.

### Part 3

Produce enough tests to ensure a correct implementation.

### Conclusion

Consider the unit test under function `testsLabClass02` which tests the minimum requirements for completing the assignment.




### Lab 1 - Parsing of an Expression Language using JavaCC

Please fork and clone this repository and build a project using Eclipse or IntelliJ with the sources pointing to the folder `labs`. Start a branch called `work` and use it as your own workspace. In this way the updates done in the master repository can be easily controlled. Create a project from the existing sources in folder `labs`

#### Sample Code

You have as starter code a working `Console` class in package `main`, a set of classes that will be used to abstractly represent a program, and a JUnit tester class whose final success will be used as goal. Inspect the code in file `SimpleParser.jj` which implements the starting point for a context free grammar for an expression language. We follow the usual priority for the arithmetic operators.

~~~
exp ::= exp '+' exp | exp '-' exp | exp '*' exp | exp '/' exp |  '(' exp ')' | num

num ::= ['1'-'9']\['0'-'9'\]*
~~~

Notice the way that the `javacc` grammar is written. Recall the explanation from the lecture to justify it. 

The existing code contains compilation errors due to missing classes. We must use the `javacc` tool to build the parser. Run the command in the command line console:

```
javacc SimpleParser.jj
```

or use the plugin for the chosen IDE. Notice the extra classes that were produced.

Test the given grammar, using the provided `JUNit` classes, and write more tests for it.

#### Part 1

Extend the grammar with the top-level non-terminal `bexp`, to have the following extra operations, still following the standard priorities between operators

~~~
bexp ::= bool | cmp | exp '&&' exp | exp '||' exp | '!' exp 

cmp ::= exp '<' exp | exp '>' exp | exp '==' exp | exp '!=' exp | ...

exp ::= exp '+' exp | exp '-' exp | exp '*' exp | exp '/' exp | '-' exp | '(' exp ')' | num

bool ::= true | false

num ::= ['1'-'9']\['0'-'9'\]*
~~~

#### Part 2

Produce new tests that cover significant cases of the new grammar.

#### Part 3

Add the possibility of using identifiers

#### Conclusion 

Consider the unit test under function `testsLabClass01` which tests the minimum requirements for completing the assignment.