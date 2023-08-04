package org.base.advent.code2015;

import lombok.Getter;
import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/18">Day 18</a>
 */
public class Day18 implements Solution<List<String>> {
    private boolean[][] currentGrid;

    @Getter
    private final List<String> input = readLines("/2015/input18.txt");

    @Override
    public Object solvePart1() {
        return totalLights(getInput(), false);
    }

    @Override
    public Object solvePart2() {
        return totalLights(getInput(), true);
    }

    public int totalLights(final List<String> gridLines, final boolean breakCorners) {
        loadGrid(gridLines);
        if (breakCorners)// turn on corners
            breakCornerLights();

        if (debug())
            displayGrid(currentGrid);
        
        for (int i = 0; i < 100; i++) {
            click(breakCorners);
            if (debug())
                displayGrid(currentGrid);
        }

        return countLights(currentGrid);
    }

    protected void click(final boolean hasBrokenCornerLights) {
        final int size = currentGrid.length;
        final boolean[][] next = new boolean[size][size];

        for (int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 2; j++) {
                final boolean isOn = currentGrid[i][j];
                final int neighborsOn = countNeighbors(i, j);
                next[i][j] = (isOn) ? neighborsOn ==2 || neighborsOn ==3 : neighborsOn ==3;
            }
        }

        synchronized (this) {
            currentGrid = next;
            if (hasBrokenCornerLights)
                breakCornerLights();
        }
    }

    protected void breakCornerLights() {
        final int size = currentGrid.length;
        currentGrid[1][1] = true;
        currentGrid[1][size - 2] = true;
        currentGrid[size - 2][1] = true;
        currentGrid[size - 2][size - 2] = true;
    }

    protected int countNeighbors(final int i, final int j) {
        int on = 0;
        on += this.currentGrid[i - 1][j - 1] ? 1 : 0;
        on += this.currentGrid[i][j - 1] ? 1 : 0;
        on += this.currentGrid[i + 1][j - 1] ? 1 : 0;
        on += this.currentGrid[i - 1][j] ? 1 : 0;
        on += this.currentGrid[i + 1][j] ? 1 : 0;
        on += this.currentGrid[i - 1][j + 1] ? 1 : 0;
        on += this.currentGrid[i][j + 1] ? 1 : 0;
        on += this.currentGrid[i + 1][j + 1] ? 1 : 0;
        return on;
    }

    @SuppressWarnings("UseOfSystemOutOrSystemErr")
    protected void displayGrid(final boolean[][] grid) {
        for (int i = 1; i <= grid.length - 2; i++) {
            for (int j = 1; j <= grid.length - 2; j++) {
                System.out.print((grid[i][j]) ? "#" : ".");
            }
            System.out.println();
        }

        System.out.println("\n-----\n");
    }

    protected void loadGrid(final List<String> lines) {
        final int size = lines.size();
        currentGrid = new boolean[size + 2][size + 2];
        for (int i = 0; i < size; i++) {
            final char[] bits = lines.get(i).toCharArray();
            for (int j = 0; j < size; j++) {
                this.currentGrid[i + 1][j + 1] = (bits[j] == '#');
            }
        }
    }

    protected int countLights(final boolean[][] grid) {
        int on = 0;
        for (int i = 1; i <= grid.length - 2; i++) {
            for (int j = 1; j <= grid.length - 2; j++) {
                on += (grid[i][j]) ? 1 : 0;
            }
        }

        return on;
    }
}
