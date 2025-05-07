package org.solver;


class IntQueue {
    private final int[] data;
    private int head = 0, tail = 0;
    private final int capacity;
    private final int mask;

    public IntQueue(int capacity) {
        this.capacity = roundToPowerOfTwo(capacity);
        this.mask = this.capacity - 1;
        this.data = new int[this.capacity];
    }

    private int roundToPowerOfTwo(int n) {
        if (n <= 0) throw new IllegalArgumentException("Capacity must be positive");
        return Integer.highestOneBit(n - 1) << 1;
    }

    public void add(int x) {
        data[tail] = x;
        tail = (tail + 1) & mask;
        if (tail == head) throw new RuntimeException("Buffer overflow");
    }

    public int remove() {
        if (isEmpty()) throw new IllegalStateException("Buffer underflow");
        int val = data[head];
        head = (head + 1) & mask;
        return val;
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public void clear() {
        head = tail = 0;
    }

    public int size() {
        return (tail - head + capacity) & mask;
    }
}
