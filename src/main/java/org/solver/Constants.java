package org.solver;

import java.util.ArrayList;
import java.util.List;


public class Constants {
    public record BenchmarkInfo(String directory, List<String> files) { }
    public static Constants shared = new Constants();

    public final ArrayList<BenchmarkInfo> TESTS = new ArrayList<>();

    public Constants() {
        List<String> files99 = new ArrayList<>(99);
        List<String> files100 = new ArrayList<>(100);
        List<String> files1000 = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) { files1000.add(i + ".cnf"); }
        for (int i = 0; i < 100; i++) { files100.add(i + ".cnf"); }
        for (int i = 0; i < 99; i++) { files99.add(i + ".cnf"); }

        List<String> vert30 = new ArrayList<>(100);
        List<String> vert100 = new ArrayList<>(100);
        List<String> vert200 = new ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            vert30.add("flat30-" + (i + 1) + ".cnf");
            vert100.add("flat100-" + (i + 1) + ".cnf");
            vert200.add("flat200-" + (i + 1) + ".cnf");
        }

        List<String> pigeon = new ArrayList<>(5);
        for (int i = 6; i < 11; i++) { pigeon.add("hole" + i + ".cnf"); }

        TESTS.add(new BenchmarkInfo("/benchmarks/20vars/sat/", files1000));

        TESTS.add(new BenchmarkInfo("/benchmarks/50vars/sat/", files1000));
        TESTS.add(new BenchmarkInfo("/benchmarks/50vars/unsat/", files1000));

        TESTS.add(new BenchmarkInfo("/benchmarks/100vars/sat/", files1000));
        TESTS.add(new BenchmarkInfo("/benchmarks/100vars/unsat/", files1000));

        TESTS.add(new BenchmarkInfo("/benchmarks/200vars/sat/", files100));
        TESTS.add(new BenchmarkInfo("/benchmarks/200vars/unsat/", files99));

        TESTS.add(new BenchmarkInfo("/benchmarks/coloring/30vertices/sat/", vert30));
        TESTS.add(new BenchmarkInfo("/benchmarks/coloring/100vertices/sat/", vert100));
        TESTS.add(new BenchmarkInfo("/benchmarks/coloring/200vertices/sat/", vert200));

        TESTS.add(new BenchmarkInfo("/benchmarks/pigeon-hole/unsat/", pigeon));
    }
}
