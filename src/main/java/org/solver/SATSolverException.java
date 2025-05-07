package org.solver;


public class SATSolverException extends RuntimeException {
    public SATSolverException(String message) {
        super(message);
    }

    public SATSolverException(String message, Throwable cause) {
        super(message, cause);
    }
}
