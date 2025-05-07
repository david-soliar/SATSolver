import org.junit.jupiter.api.Test;
import org.solver.Constants;
import org.solver.ExportResults;
import org.solver.SATInfo;
import org.solver.SimpleSATSolver;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;


public class SimpleSATSolverTest {

    @Test
    public void test20varsSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(0), true);
    }

    @Test
    public void test50varsSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(1), true);
    }

    @Test
    public void test50varsUNSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(2), false);
    }

    @Test
    public void test100varsSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(3), true);
    }

    @Test
    public void test100varsUNSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(4), false);
    }

    @Test
    public void test200varsSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(5), true);
    }

    @Test
    public void test200varsUNSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(6), false);
    }

    @Test
    public void testColoring30SAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(7), true);
    }

    @Test
    public void testColoring100SAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(8), true);
    }

    @Test
    public void testColoring200SAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(9), true);
    }

    @Test
    public void testPigeonHoleUNSAT() throws Exception {
        testBenchmarks(Constants.shared.TESTS.get(10), false);
    }

    private void testBenchmarks(Constants.BenchmarkInfo benchmarkInfo, boolean result) throws Exception {
        for (String fileName : benchmarkInfo.files()) {
            String resourcePath = benchmarkInfo.directory() + fileName;
            try (InputStream in = ExportResults.class.getResourceAsStream(resourcePath)) {
                if (in == null) {
                    throw new FileNotFoundException(resourcePath);
                }
                SimpleSATSolver solver = new SimpleSATSolver(new BufferedReader(new InputStreamReader(in)));
                SATInfo res = solver.solve();
                assertEquals(res.isSat, result);
            }
        }
    }
}