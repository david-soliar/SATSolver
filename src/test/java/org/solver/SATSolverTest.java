package org.solver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.*;
import java.util.stream.*;


public class SATSolverTest {

    private static final String SAT_DIR = "sat";
    private static final String UNSAT_DIR = "unsat";

    private SATInfo solve(Path file) throws Exception {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return new SATSolver(reader).solve();
        }
    }

    private Stream<Path> load(String dir) throws Exception {
        Path path = Paths.get(getClass().getClassLoader()
                .getResource(dir)
                .toURI());

        return Files.walk(path)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".cnf"));
    }

    private Stream<DynamicTest> tests(String dir, boolean expected) throws Exception {
        return load(dir).map(file ->
                DynamicTest.dynamicTest(
                        file.getFileName().toString(),
                        () -> assertEquals(expected, solve(file).isSat)
                )
        );
    }

    @TestFactory
    Stream<DynamicTest> satTests() throws Exception {
        return tests(SAT_DIR, true);
    }

    @TestFactory
    Stream<DynamicTest> unsatTests() throws Exception {
        return tests(UNSAT_DIR, false);
    }

    @Test
    void trivialSAT() throws Exception {
        String cnf = "p cnf 1 1\n1 0\n";

        SATInfo res = new SATSolver(
                new BufferedReader(new StringReader(cnf))
        ).solve();

        assertTrue(res.isSat);
    }
}
