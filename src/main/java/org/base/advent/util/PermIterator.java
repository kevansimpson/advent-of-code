package org.base.advent.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Iterator of permutations and reduces results.
 * @param <T> the permutation item type
 */
@Slf4j
public class PermIterator<T> extends Yielder<List<T>> {

    @Getter private final T[] items;
    private final int size;
    private final int[] indexes;

    public PermIterator(T[] permItems) {
        items = permItems;
        size = permItems.length;
        indexes = new int[size];
        for (int i = 0; i < size; i++) indexes[i] = i;
    }

    protected void run() throws InterruptedException {
        generatePermutations(size, indexes);
    }

    void generatePermutations(int n, int[] array) throws InterruptedException {
        if (n == 1) {
            final List<T> list = new ArrayList<>(array.length);
            for (int i : array) list.add(getItems()[i]);

            this.yield(list);
            return;
        }
        for (int i = 0; i < n; i++) {
            generatePermutations(n - 1, array);
            int tmp;
            if ((n & 1) == 0) {
                tmp = array[i];
                array[i]   = array[n-1];
            } else {
                tmp = array[0];
                array[0]   = array[n-1];
            }
            array[n-1] = tmp;
        }
    }
}
