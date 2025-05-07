package org.solver;


class IntStack {
    private final int[] data;
    private int top = 0;

    public IntStack(int size) {
        data = new int[size];
    }

    public void push(int x) {
        data[top++] = x;
    }

    public int pop() {
        return data[--top];
    }

    public int peek() {
        return data[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public int size() {
        return top;
    }

}
