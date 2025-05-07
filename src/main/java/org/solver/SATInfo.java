package org.solver;

public class SATInfo {
    public boolean isSat;
    public byte[] assignment;
    public long initTime;
    public long solveTime;
    public int unitPropagationCount;
    public int depth;
    private static final byte NONE = 0, TRUE = 1, FALSE = -1;

    public SATInfo() {}

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isSat ? "SAT" : "UNSAT").append("\n");

        if (isSat && assignment != null) {
            for (int i = 1; i < assignment.length; i++) {
                sb.append(i * assignment[i]).append(", ");
            } sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("\n");

        sb.append(initTime/1_000_000).append("\n");
        sb.append(solveTime/1_000_000).append("\n");
        sb.append(unitPropagationCount).append("\n");
        sb.append(depth);

        return sb.toString();
    }
}