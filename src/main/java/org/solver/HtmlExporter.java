package org.solver;

import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;


public class HtmlExporter {

    public static void export(List<String> files, List<SATInfo> results, Path output) throws IOException {

        if (files.size() != results.size()) {
            throw new IllegalArgumentException("Files and results size mismatch");
        }

        StringBuilder html = new StringBuilder();

        html.append("""
        <html>
        <body>
            <h2>SAT Results</h2>

            <table border="1">
            <thead>
            <tr>
                <th>File</th>
                <th>Status</th>
                <th>Init (ms)</th>
                <th>Solve (ms)</th>
                <th>Unit Propagation</th>
                <th>Depth</th>
            </tr>
            </thead>
            <tbody>
        """);

        for (int i = 0; i < files.size(); i++) {
            SATInfo r = results.get(i);

            html.append("\t\t<tr>")
                .append("<td>").append(files.get(i)).append("</td>")
                .append("<td>").append(r.isSat ? "SAT" : "UNSAT").append("</td>")
                .append("<td>").append(r.initTime / 1_000_000).append("</td>")
                .append("<td>").append(r.solveTime / 1_000_000).append("</td>")
                .append("<td>").append(r.unitPropagationCount).append("</td>")
                .append("<td>").append(r.depth).append("</td>")
                .append("</tr>\n");
        }

        html.append("""
            </tbody>
            </table>
        </body>
        </html>
        """);

        Files.writeString(output, html.toString());
    }
}
