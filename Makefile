# Makefile for compiling and running a Java program

# Name of the Java source file without the .java extension
MAIN_CLASS = Main
SRC_FILE = $(MAIN_CLASS).java
OUT_DIR = out

# Default target
all: run

# Target to compile the Java source file
compile:
	mkdir -p $(OUT_DIR)
	javac -d $(OUT_DIR) $(SRC_FILE)

# Target to run the compiled Java class
run: compile
	java -cp $(OUT_DIR) $(MAIN_CLASS)

# Target to clean the output directory
clean:
	rm -rf $(OUT_DIR)

# Phony targets
.PHONY: all compile run clean

