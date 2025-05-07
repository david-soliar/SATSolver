
# SATSolver

A SAT solver implemented in Java using the DPLL algorithm with some optimizations.
This project can be run via the CLI and includes a few tests to validate its functionality and correctness.

> **Note:** Around 5,000 benchmark files are bundled with the project. As a result, installation or cloning may take a few minutes.

## Installation

### Install Java and Maven (if needed)

Make sure Java (16+) and Maven are installed on your system.

- [Java Download](https://www.oracle.com/java/technologies/downloads)
- [Maven Download](https://maven.apache.org/download.cgi)

### Navigate to the Directory Containing `pom.xml`

Navigate to the project folder where the `pom.xml` file is located:

```
cd SATSolver/
```

### Creating and running

To build the project and generate the `.jar` file, use:

```
mvn clean
mvn package
```

Once the `.jar` file is built, you can run the solver with:

```
java -jar path/to/solver/SATSolver-1.0.jar path/to/input.cnf
```

For example:

```
java -jar target/SATSolver-1.0.jar benchmarks/20vars/sat/0.cnf
java -jar target/SATSolver-1.0.jar --export
```

### Arguments

`filepath`, to output results in the command line

`--export`, to generate `results.html`, which contains benchmarked results of the solver from the `benchmarks` folder

**Note:** Only one of `filepath` or `--export` can be used at the same time.

### Output Structure

The output is structured as follows:

1. `SAT` or `UNSAT`
2. List of true literals, sorted in ascending order by the variable number, or a newline if UNSAT
3. Time taken to initialize the solver
4. Time taken to solve
5. Number of unit propagations that happened during the entire run
6. Depth of the decision tree (number of decision variables)

## Other info

**Project supports the [DIMACS CNF](https://people.sc.fsu.edu/~jburkardt/data/cnf/cnf.html) format only**

- The `benchmarks` are located in `src/main/resources/benchmarks`
- The tests are located in `src/test/java/SimpleSATSolverTest.java`
- In `src/main/java/org/solver`:
  - `CircularBuffer`, `IntQueue`, and `IntStack` contain data structures for better performance
  - `DimacsParser` parses `.cnf` files (streams)
  - `SATInfo` contains information printed after the solver finishes
  - `SimpleSATSolver` contains the solver logic
  - `ExportResults` exports benchmarks

## Time Measurement

- Time is measured in nanoseconds, but the output displays the times in milliseconds.
- If you need the time in nanoseconds, remove the division by `1_000_000` in `SATInfo` to output time in nanoseconds.
