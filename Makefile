JFLEX_PATH = 	lib/JFlex.jar
TURTLE_PATH = 	lib/cs34q.jar
CUP_PATH = 	lib/java_cup.jar
LEXER_PATH = 	src/fractal/syntax/FractalLexer.jflex
PARSER_PATH = 	src/fractal/syntax/FractalParser.cup
SYNTAX_DIR = 	src/fractal/syntax
jflex:
	@echo "Generating Lexer ........... (FractalLexer.java)"
	@java -cp $(JFLEX_PATH) JFlex.Main --nobak $(LEXER_PATH)
	@echo "Done."

cup:
	@echo "Generating Parser and Symbol file ........(FractalParser.java sym.java)"
	@java -cp $(CUP_PATH) java_cup.Main -parser FractalParser -destdir $(SYNTAX_DIR) < $(PARSER_PATH)
	@echo "Done."

compile:
	@echo "Compiling Java Files"
	@javac -cp ":$(TURTLE_PATH):$(CUP_PATH):$(JFLEX_PATH)" -Xlint:unchecked src/fractal/*/*.java
	@echo "Done."

run:
	@java -cp "src:$(TURTLE_PATH):$(CUP_PATH):$(JFLEX_PATH)" fractal.sys.Repl fractal.semantics.FractalEvaluator $(file)

test:
	@java -cp ":$(TURTLE_PATH):$(CUP_PATH):$(JFLEX_PATH)" fractal.sys.Repl

gosper:
	@make file=examples/gosper.fal run
