package org.solver;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class ExportResults {
    public static void main(String[] args) throws Exception {
        String description = "<p>" +
                "<b>Each benchmark test was run once per file. " +
                "The table below shows the time results from that single run. " +
                "In earlier tests, the variation between runs was usually no more than 5%, " +
                "so a single run is now considered sufficient for a general comparison.</b>" +
                "</p><br>\n";

        StringBuilder resultTest = new StringBuilder();
        resultTest.append("<table border='1'>\n<thead><tr>");
        resultTest.append("<th>Test Directory</th><th>Total Time (milliseconds)</th>");
        resultTest.append("</tr></thead>\n<tbody>\n");

        StringBuilder resultSingle = new StringBuilder();
        resultSingle.append("<table border='1'>\n<thead><tr>");
        resultSingle.append("<th>File</th><th>Init Time (milliseconds)</th><th>Solve Time(milliseconds)</th><th>Unit Count</th><th>Depth</th>");
        resultSingle.append("</tr></thead>\n<tbody>\n");

        System.out.println("The process is expected to take approximately 100 seconds to complete.");

        for (Constants.BenchmarkInfo benchmark : Constants.shared.TESTS) {

            System.out.println("Started testing: " + benchmark.directory());

            long totalTime = 0;
            for (String fileName : benchmark.files()) {
                String resourcePath = benchmark.directory() + fileName;
                try (InputStream in = ExportResults.class.getResourceAsStream(resourcePath)) {
                    if (in == null) {
                        System.err.println("Resource not found: " + resourcePath);
                        continue;
                    }
                    byte[] data = in.readAllBytes();

                    TestResult result = runTest(new ByteArrayInputStream(data), resourcePath);

                    if (result == null) {
                        System.out.println("Error");
                        return;
                    }

                    totalTime += result.time;
                    resultSingle.append(result.htmlRow).append("\n");
                }
            }

            resultTest.append("<tr>")
                    .append("<td>").append(benchmark.directory()).append("</td>")
                    .append("<td>").append(Math.ceilDiv(totalTime, 1_000_000)).append("</td>")
                    .append("</tr>\n");

            System.out.println("Finished testing: " + benchmark.directory());
        }

        resultTest.append("</tbody>\n</table>\n");
        resultSingle.append("</tbody>\n</table>\n");

        String finalHtml = description + resultTest + "<br>\n" + resultSingle;

        Path outputFile = Path.of("results.html");
        Files.writeString(outputFile, finalHtml);

        System.out.println("Export finished to: " + outputFile.toAbsolutePath());
    }

    static class TestResult {
        long time;
        String htmlRow;

        TestResult(long time, String htmlRow) {
            this.time = time;
            this.htmlRow = htmlRow;
        }
    }

    private static TestResult runTest(InputStream in, String filePath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            SimpleSATSolver solver = new SimpleSATSolver(reader);
            SATInfo res = solver.solve();

            long totalTime = res.initTime + res.solveTime;

            String htmlRow = "<tr>"
                    + "<td>" + filePath + "</td>"
                    + "<td>" + Math.ceilDiv(res.initTime, 1_000_000) + "</td>"
                    + "<td>" + Math.ceilDiv(res.solveTime, 1_000_000) + "</td>"
                    + "<td>" + res.unitPropagationCount + "</td>"
                    + "<td>" + res.depth + "</td>"
                    + "</tr>";

            return new TestResult(totalTime, htmlRow);

        } catch (Exception e) {
            System.err.println("Error processing resource: " + filePath);
            return null;
        }
    }
}
