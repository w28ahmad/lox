MAIN_CLASS = com.craftinginterpreters.lox.Lox


ARGS = test.lox

all: run

compile:
	mvn compile

run: compile
	mvn exec:java -Dexec.mainClass=$(MAIN_CLASS) -Dexec.args="$(ARGS)"

clean:
	mvn clean

.PHONY: all compile run clean

