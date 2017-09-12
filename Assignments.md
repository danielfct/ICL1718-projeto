# Interpretation and compilation of Programming Languages 2017-18

## Lab Assignments

This is the list of lab assignments for the course. We first have the complete list of assignmetns and then the details of each one. This list and details will be updated every week with new adjustments and details.

### Planning

>**Set 11-15** - Parsing of an expression language.

>**Set 18-22** - Interpretation of an expression language.

>**Set 25-29** - Extending the language with boolean values. Typing an expression language.

>**Project assignment: announcement**

>**Out 2-6**  - Compiling arithmetic expressions to the JVM.

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

#### Goal 1

Extend the grammar with the top-level non-terminal `bexp`, to have the following extra operations, still following the standard priorities between operators

~~~
bexp ::= bool | cmp | exp '&&' exp | exp '||' exp | '!' exp 

cmp ::= exp '<' exp | exp '>' exp | exp '==' exp | exp '!=' exp | ...

exp ::= exp '+' exp | exp '-' exp | exp '*' exp | exp '/' exp | '-' exp | '(' exp ')' | num

bool ::= true | false

num ::= ['1'-'9']\['0'-'9'\]*
~~~

#### Goal 2

Produce new tests that cover significant cases of the new grammar.

#### Goal 3

Add the possibility of using identifiers

#### Conclusion 

Consider the unit test under function `testsLabClass01` which tests the minimum requirements for completing the assignment.