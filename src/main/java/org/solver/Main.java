package org.solver;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: solver <file|folder> [--export]");
            return;
        }

        try {
            boolean export = Arrays.stream(args)
                    .anyMatch(a -> a.equalsIgnoreCase("--export"));

            String target = Arrays.stream(args)
                    .filter(a -> !a.startsWith("-"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No input"));

            Path path = Paths.get(target);

            List<Path> cnfFiles = collectFiles(path);

            List<String> files = new ArrayList<>();
            List<SATInfo> results = new ArrayList<>();

            for (Path file : cnfFiles) {
                System.out.println(file);

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    SATInfo res = new SATSolver(reader).solve();

                    files.add(file.toString());
                    results.add(res);

                    if (!export) System.out.println(res);
                }
            }

            if (export) {
                HtmlExporter.export(files, results, Path.of(System.getProperty("user.home"), "results.html"));
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<Path> collectFiles(Path path) throws IOException {
        List<Path> cnfFiles = new ArrayList<>();

        if (Files.isDirectory(path)) {
            try (var stream = Files.walk(path)) {
                stream.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".cnf"))
                        .forEach(cnfFiles::add);
            }
        } else {
            cnfFiles.add(path);
        }

        return cnfFiles;
    }
}
