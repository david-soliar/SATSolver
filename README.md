
# SATSolver

A SAT solver implemented in Java using the DPLL algorithm with some optimizations.

The project supports:
- solving single CNF files or directories recursively
- HTML export of benchmark results


## Build

Requirements:
- Java 21+
- Maven 3.8+

Build project:
```
mvn clean package
```
This produces:
```
target/SATSolver-A.B.jar
```


## Run

### Solve a file
```
java -jar SATSolver-A.B.jar path/to/file.cnf
```

### Solve a folder (recursive)
```
java -jar SATSolver-A.B.jar path/to/folder
```
Processes all `.cnf` files recursively.

### Export benchmark results
```
java -jar SATSolver-A.B.jar <file|folder> --export
```
Output: ~/results.html


## Output Structure (CLI)

0. filename
1. `SAT` or `UNSAT`
2. List of true literals, sorted in ascending order by the variable number, or a newline if UNSAT
3. Time taken to initialize the solver
4. Time taken to solve
5. Number of unit propagations that happened during the entire run
6. Depth of the decision tree (number of decision variables)


## Output Structure (HTML)

[Example.](/example_results.html)


## Notes

- Only [DIMACS CNF](https://web.archive.org/web/20190325181937/https://www.satcompetition.org/2009/format-benchmarks2009.html) format is supported
- Time is measured in nanoseconds internally and displayed in milliseconds (remove `/ 1_000_000` in `SATInfo` to show nanoseconds)
- Around 5000 benchmark files are included in `benchmarks.zip`.
