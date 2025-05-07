package org.solver;


class CircularBuffer {
    public final int[][] data;
    public int head = 0, tail = 0;
    public final int capacity;
    public final int mask;

    public CircularBuffer(int capacity) {
        this.capacity = roundToPowerOfTwo(capacity);
        this.mask = this.capacity - 1;
        data = new int[this.capacity][];
    }

    private int roundToPowerOfTwo(int n) {
        if (n <= 0) throw new IllegalArgumentException("Capacity must be positive");
        return Integer.highestOneBit(n - 1) << 1;
    }

    public int size() {
        return (tail - head + capacity) & mask;
    }

    public void add(int[] x) {
        data[tail] = x;
        tail = (tail + 1) & mask;
        if (tail == head) throw new RuntimeException("Buffer overflow");
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public void skip(int n) {
        head = (head + n) & mask;
    }
}
