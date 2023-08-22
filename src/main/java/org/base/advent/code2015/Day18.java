package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.List;

/**
 * <a href="https://adventofcode.com/2015/day/18">Day 18</a>
 */
public class Day18 implements Solution<List<String>> {
    @Override
    public Object solvePart1(final List<String> input) {
        return totalLights(input, false);
    }

    @Override
    public Object solvePart2(final List<String> input) {
        return totalLights(input, true);
    }

    public record LightGrid(boolean[][] area) {
        public void breakCornerLights() {
            final int size = area.length;
            area[1][1] = true;
            area[1][size - 2] = true;
            area[size - 2][1] = true;
            area[size - 2][size - 2] = true;
        }

        public int countLights() {
            int on = 0;
            for (int i = 1; i <= area.length - 2; i++) {
                for (int j = 1; j <= area.length - 2; j++) {
                    if (area[i][j])
                        on += 1;
                }
            }

            return on;
        }

        public int countNeighbors(final int i, final int j) {
            int on = 0;
            on += area[i - 1][j - 1] ? 1 : 0;
            on += area[i][j - 1] ? 1 : 0;
            on += area[i + 1][j - 1] ? 1 : 0;
            on += area[i - 1][j] ? 1 : 0;
            on += area[i + 1][j] ? 1 : 0;
            on += area[i - 1][j + 1] ? 1 : 0;
            on += area[i][j + 1] ? 1 : 0;
            on += area[i + 1][j + 1] ? 1 : 0;
            return on;
        }

        public void displayGrid() {
            for (int i = 1; i <= area.length - 2; i++) {
                for (int j = 1; j <= area.length - 2; j++) {
                    System.out.print((area[i][j]) ? "#" : ".");
                }
                System.out.println();
            }

            System.out.println("\n-----\n");
        }
    }

    public int totalLights(final List<String> gridLines, final boolean breakCorners) {
        LightGrid lightGrid = loadGrid(gridLines);
        if (breakCorners)// turn on corners
            lightGrid.breakCornerLights();

        if (debug())
            lightGrid.displayGrid();
        
        for (int i = 0; i < 100; i++) {
            lightGrid = click(lightGrid, breakCorners);
            if (debug())
                lightGrid.displayGrid();
        }

        return lightGrid.countLights();
    }

    LightGrid click(final LightGrid currentGrid, final boolean hasBrokenCornerLights) {
        final int size = currentGrid.area.length;
        final boolean[][] next = new boolean[size][size];

        for (int i = 1; i <= size - 2; i++) {
            for (int j = 1; j <= size - 2; j++) {
                final boolean isOn = currentGrid.area[i][j];
                final int neighborsOn = currentGrid.countNeighbors(i, j);
                next[i][j] = (isOn) ? neighborsOn ==2 || neighborsOn ==3 : neighborsOn ==3;
            }
        }

        LightGrid grid = new LightGrid(next);
        if (hasBrokenCornerLights)
            grid.breakCornerLights();

        return grid;
    }

    protected LightGrid loadGrid(final List<String> lines) {
        final int size = lines.size();
        boolean[][] currentGrid = new boolean[size + 2][size + 2];
        for (int i = 0; i < size; i++) {
            final char[] bits = lines.get(i).toCharArray();
            for (int j = 0; j < size; j++) {
                currentGrid[i + 1][j + 1] = (bits[j] == '#');
            }
        }
        return new LightGrid(currentGrid);
    }
}
