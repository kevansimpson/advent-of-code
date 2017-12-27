package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.List;

/**
 * <h2>Part 1</h2>
 * After the million lights incident, the fire code has gotten stricter: now, at most ten thousand lights are allowed.
 * You arrange them in a 100x100 grid.
 *
 * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration. With so few
 * lights, he says, you'll have to resort to animation.
 *
 * Start by setting your lights to the included initial configuration (your puzzle input). A # means "on", and
 * a . means "off".
 *
 * Then, animate your grid in steps, where each step decides the next configuration based on the current one. Each
 * light's next state (either on or off) depends on its current state and the current states of the eight lights
 * adjacent to it (including diagonals). Lights on the edge of the grid might have fewer than eight neighbors; the
 * missing ones always count as "off".
 *
 * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8, and the light
 * marked B, which is on an edge, only has the neighbors marked 1 through 5:
 *
 * 1B5...
 * 234...
 * ......
 * ..123.
 * ..8A4.
 * ..765.
 *
 * The state a light should have next is based on its current state (on or off) plus the number of neighbors that are on:
 *  - A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
 *  - A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
 *
 * All of the lights update simultaneously; they all consider the same current state before moving to the next.
 *
 * In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?
 *
 * <h2>Part 2</h2>
 * You flip the instructions over; Santa goes on to point out that this is all just an implementation of Conway's
 * Game of Life. At least, it was, until you notice that something's wrong with the grid of lights you bought:
 * four lights, one in each corner, are stuck on and can't be turned off. The example above will actually run like this:
 * ...
 * After 5 steps, this example now has 17 lights on.
 *
 * In your grid of 100x100 lights, given your initial configuration, but with the four corners always in the on state,
 * how many lights are on after 100 steps?
 *
 */
public class Day18 implements Solution<List<String>> {

    private boolean[][] currentGrid;

    @Override
    public List<String> getInput() throws IOException {
        return readLines("/2015/input18.txt");
    }

    @Override
    public Object solvePart1() throws Exception {
        return totalLights(getInput(), false);
    }

    @Override
    public Object solvePart2() throws Exception {
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
            System.out.println("");
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
