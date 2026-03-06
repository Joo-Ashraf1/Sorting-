package com.example.sortingvisual.SortStartegy;

import java.util.Random;

public class ArrayGenerator {
    public enum ArrayType { RANDOM, SORTED, INVERSELY_SORTED }

    private static final Random RAND = new Random();

    public static int[] generate(int size, ArrayType type) {
        int[] array = new int[size];
        switch (type) {
            case SORTED:
                for (int i = 0; i < size; i++) array[i] = i + 1;
                break;
            case INVERSELY_SORTED:
                for (int i = 0; i < size; i++) array[i] = size - i;
                break;
            case RANDOM:
            default:
                for (int i = 0; i < size; i++) array[i] = RAND.nextInt(size * 10) + 1;
                break;
        }
        return array;
    }
    public static int[] copy(int[] source) {
        int[] dest = new int[source.length];
        System.arraycopy(source, 0, dest, 0, source.length);
        return dest;
    }
}
