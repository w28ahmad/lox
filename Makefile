MAIN_CLASS = com.craftinginterpreters.lox.Lox


ARGS = test.lox

all: run

compile:
	mvn compile

run: compile
	mvn exec:java -Dexec.mainClass=$(MAIN_CLASS) -Dexec.args="$(ARGS)"

ast:
	mvn exec:java -Dexec.mainClass=com.craftinginterpreters.lox.tools.GenerateAst -Dexec.args="src/main/java/com/craftinginterpreters/lox"

prettyPrint:
	mvn exec:java -Dexec.mainClass=com.craftinginterpreters.lox.AstPrinter


clean:
	mvn clean

.PHONY: all compile run clean

