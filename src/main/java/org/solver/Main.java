package org.solver;

import java.io.*;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 1 && args[0].equalsIgnoreCase("--export")) {
            ExportResults.main(new String[0]);
            return;
        }

        if (args.length != 1) {
            System.out.println("Usage: solver <input.cnf>  OR  solver --export");
            return;
        }

        String filename = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            SimpleSATSolver solver = new SimpleSATSolver(reader);
            SATInfo res = solver.solve();
            System.out.println(res);
        }
    }
}
