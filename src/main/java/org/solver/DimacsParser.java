package org.solver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;


public class DimacsParser {
    public final ArrayList<int[]> clauses = new ArrayList<>(100);
    public final Map<Integer, Integer> frequency = new HashMap<>(100);
    public int vars;
    public int count;

    public DimacsParser(BufferedReader source) throws IOException {
        String line;
        int pVars = 0;
        int pClauses = 0;
        
        while ((line = source.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("c")) continue;

            if (line.startsWith("p cnf")) {
                String[] parts = line.split("\\s+");

                pVars = Integer.parseInt(parts[2]);
                pClauses = Integer.parseInt(parts[3]);
                if (pVars < 1 || pClauses < 1) {
                    throw new IllegalArgumentException("Invalid 'p cnf' line: " + line);
                }
                continue;
            }

            String[] parts = line.split("\\s+");
            int[] literals = new int[parts.length - 1];
            for (int i = 0; i < literals.length; i++) {
                int lit = Integer.parseInt(parts[i]);
                if (lit == 0) break;
                int encoded = 2 * Math.abs(lit) + (lit < 0 ? 1 : 0);
                literals[i] = encoded;
                frequency.put(encoded, frequency.getOrDefault(encoded, 0) + 1);
            }

            if (literals.length > 0) {
                clauses.add(literals);
            }
        }

        vars = pVars;
        count = pClauses;
    }
}
