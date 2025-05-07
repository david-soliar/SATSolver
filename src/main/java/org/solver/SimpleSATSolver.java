package org.solver;

import java.io.BufferedReader;


public class SimpleSATSolver {
    private final int vars;
    private final CircularBuffer[] watches;
    private final IntQueue prop_q;
    private static final byte NONE = 0, TRUE = 1, FALSE = -1;
    private final byte[] assigns;
    private final int[] tries;
    private final IntStack trail;
    private final IntStack trailLim;

    private final boolean[] savedPhase;
    private final double[] scores;
    private static final double DECAY_VALUE = 0.95;
    private static final int RESTART_VALUE = 8192;

    private final SATInfo info = new SATInfo();

    public SimpleSATSolver(BufferedReader source) {
        info.initTime = System.nanoTime();

        DimacsParser parser = new DimacsParser(source);
        this.vars = parser.vars;

        this.trail = new IntStack(vars + 1);
        this.trailLim = new IntStack(vars + 1);
        this.prop_q = new IntQueue(vars + 1);
        this.assigns = new byte[vars + 1];
        this.tries = new int[vars + 1];

        this.savedPhase = new boolean[vars + 1];
        this.scores = new double[vars + 1];

        watches = new CircularBuffer[2 * vars + 2];
        for (int i = 0; i < watches.length; i++) {
            watches[i] = new CircularBuffer(parser.frequency.getOrDefault(neg(i), 2) * 4);
        }

        for (int[] literals : parser.clauses) {
            if (literals.length == 1){
                enqueue(literals[0]);
                continue;
            }
            watches[neg(literals[0])].add(literals);
            watches[neg(literals[1])].add(literals);
        }

        info.initTime = System.nanoTime() - info.initTime;
    }

    public SATInfo solve() {
        info.solveTime = System.nanoTime();
        info.isSat = search();
        info.solveTime = System.nanoTime() - info.solveTime;
        info.depth = trailLim.size();
        info.assignment = assigns.clone();
        return info;
    }

    private int pickLiteral() {
        double maxScore = -1;
        int selectedVar = 1;

        for (int var = 1; var <= vars; var++) {
            double currentScore = scores[var];
            if (assigns[var] == NONE && currentScore > maxScore) {
                maxScore = scores[var];
                selectedVar = var;
            } else if (currentScore == maxScore && var < selectedVar) {
                selectedVar = var;
            }
            scores[var] *= DECAY_VALUE;
        }

        return selectedVar * 2 + (savedPhase[selectedVar] ? 0 : 1);
    }

    private boolean search() {
        assume(pickLiteral());

        while (true) {
            for (int i = 0; i < RESTART_VALUE; i++) {
                boolean conflict = propagate();
                if (conflict) {
                    boolean ok = backtrack();
                    if (!ok) return false;
                }
                else {
                    if (trail.size() == vars) {
                        return true;
                    } else {
                        assume(pickLiteral());
                    }
                }
            }
            for (int var = 1; var <= vars; var++) {
                scores[var] *= 1e-50;
            }
        }
    }

    private void assume(int p) {
        trailLim.push(trail.size());
        enqueue(p);
    }

    private boolean enqueue(int p) {
        byte val = value(p);
        if (val != NONE) {
            return val == TRUE;
        }
        int v = var(p);
        boolean sign = sgn(p);
        assigns[v] = sign ? TRUE : FALSE;
        savedPhase[v] = sign;
        tries[v] += 1;
        prop_q.add(p);
        trail.push(p);
        scores[v] += 1.0;
        return true;
    }

    private boolean propagate() {
        while (!prop_q.isEmpty()) {
            int p = prop_q.remove();
            CircularBuffer tmp = watches[p];

            int propagated = 0;
            int i = tmp.head;
            int stop = tmp.tail;
            int capacity = tmp.capacity;

            while (i != stop) {
                boolean ok = propagateClause(tmp.data[i], p);
                if (!ok) { // conflict
                    tmp.skip(propagated);
                    prop_q.clear();
                    return true;
                }
                i = (i + 1) & (capacity - 1);
                propagated++;
            }
            tmp.skip(propagated);
        }
        return false;
    }

    private boolean propagateClause(int[] clause, int p) {
        int watchLit = clause[1];
        if (watchLit != neg(p)) {
            clause[1] = clause[0];
            clause[0] = watchLit;
        }

        int cl0 = clause[0];
        byte valCl0 = value(cl0);
        if (valCl0 != TRUE) {
            for (int i = 2; i < clause.length; i++) {
                int lit = clause[i];
                if (value(lit) != FALSE) {
                    clause[i] = clause[1];
                    clause[1] = lit;
                    watches[neg(lit)].add(clause);
                    scores[var(lit)] += 1.0;
                    return true;
                }
            }

            if (valCl0 == NONE) {
                info.unitPropagationCount++;
                boolean res = enqueue(cl0);
                if (res) watches[p].add(clause);
                return res;
            }
        } else {
            watches[p].add(clause);
            return true;
        }
        return false;
    }

    private boolean undoOne() {
        int p = trail.pop();
        int v = var(p);
        assigns[v] = NONE;
        tries[v] = 0;
        scores[v] *= DECAY_VALUE;

        if (!trailLim.isEmpty() && trail.size() == trailLim.peek()) {
            trailLim.pop();
            return true;
        }
        return false;
    }

    private boolean backtrack() {
        while (true) {
            if (trailLim.isEmpty()) return false;

            while (trail.size() != trailLim.peek() + 1) {
                if (undoOne()) break;
                if (trail.isEmpty()) return false;
            }

            if (trail.isEmpty()) return false;

            int p = trail.peek();
            int v = var(p);

            if (tries[v] < 2) {
                assigns[v] = NONE;
                trail.pop();
                enqueue(neg(p));
                return true;
            } else {
                undoOne();
            }
        }
    }

    private byte value(int p) {
        byte val = assigns[p >> 1];
        if (val == NONE) return NONE;
        return ((p & 1) == 0 ? val : (byte)(-val));
    }

    private static int var(int p) {
        return p >> 1;
    }

    private static int neg(int p) {
        return p ^ 1;
    }

    private static boolean sgn(int p) {
        return (p & 1) == 0;
    }
}
