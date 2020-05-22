PYTHON = python3
GROOVY = groovyc
GFLAGS = -d
RMFLAGS = -r
CPATH = -cp

default: Runner main

Runner: Runner.py
	$(PYTHON) Runner.py
main: main.groovy
	$(GROOVY) $(CPATH) lib/jpf.jar $(GFLAGS) bin/main main.groovy


clean:
	$(RM) $(RMFLAGS) bin/main
