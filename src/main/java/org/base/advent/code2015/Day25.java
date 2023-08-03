package org.base.advent.code2015;

import org.base.advent.Solution;
import org.base.advent.util.Point;

/**
 * <a href="https://adventofcode.com/2015/day/25">Day 25</a>
 */
public class Day25 implements Solution<Long>{
    
    private static final int DIM = 6400;
    
    private final long[][] grid = new long[DIM][DIM];

    @Override
    public Long getInput(){
        return 20151125L;
    }

    @Override
    public Object solvePart1() {
        return calcMachineCode(getInput()); // gives wrong answer
    }

    @Override
    public Object solvePart2() {
        return givenUp(getInput());
    }

    // < 13431666
    public long calcMachineCode(final long input) {
        buildInitialGrid();
        int index = 1;
        long next = input;
        
        /*
         * [1,1]                             1
         * [1,2]    [2,1]                    2
         * [1,3]    [2,2]    [3,1]           3
         * [1,4]    [2,3]    [3,2]    [4,1]  4
         */
        int iterations = 0;
        outer:
        for (;; index++) {
            debug("index = ", index);
            for (int x = 1, y = index; x <= index; x++, y--) {
                ++iterations;
                debug("@ ", new Point(x, y));

                if (x == 2978 && y == 3083) {
                    grid[x][y] = next;
                    debug("Code is ", next, " @ index = ", index);
                    break outer;
                }
                if (grid[x][y] > 0L) {
                    if (grid[x][y] != next) {
                        debug("wrong @ ", new Point(x, y));
                        throw new RuntimeException(String.valueOf(next));
                    }
                }
                else
                    grid[x][y] = next;

                if (debug() && next == 2650453) {
                    debug("found @ ", new Point(x, y), " @ index = ", index);
                    debug("iterations = ", iterations);
                }
                next = next(next);
                debug("next = ", next);
            }
        }

        return next;
    }

    // plagiarized from answer found @ https://www.reddit.com/r/adventofcode/comments/3y5jco/day_25_solutions/
    public long givenUp(final long input) {
        final int row = 2978;
        final int col = 3083;
        long code = input;

        int iterations = 0;

        for (int i = 1; i <= col; i++) {
            iterations += i;
        }

        for (int i = 0; i < row - 1; i++) {
            iterations += col + i;
        }

        for (int i = 1; i < iterations; i++) {
            code = (code * 252533) % 33554393;
        }

        debug("iterations = ", iterations);
        return code;
    }

    /*
   |    1         2         3         4         5         6
---+---------+---------+---------+---------+---------+---------+
 1 | 20151125  18749137  17289845  30943339  10071777  33511524
 2 | 31916031  21629792  16929656   7726640  15514188   4041754
 3 | 16080970   8057251   1601130   7981243  11661866  16474243
 4 | 24592653  32451966  21345942   9380097  10600672  31527494
 5 |    77061  17552253  28094349   6899651   9250759  31663883
 6 | 33071741   6796745  25397450  24659492   1534922  27995004
    */
    protected void buildInitialGrid() {
        grid[1][1] = 20151125;    grid[2][1] = 18749137;    grid[3][1] = 17289845;    grid[4][1] = 30943339;    grid[5][1] = 10071777;    grid[6][1] = 33511524;
        grid[1][2] = 31916031;    grid[2][2] = 21629792;    grid[3][2] = 16929656;    grid[4][2] =  7726640;    grid[5][2] = 15514188;    grid[6][2] =  4041754;
        grid[1][3] = 16080970;    grid[2][3] =  8057251;    grid[3][3] =  1601130;    grid[4][3] =  7981243;    grid[5][3] = 11661866;    grid[6][3] = 16474243;
        grid[1][4] = 24592653;    grid[2][4] = 32451966;    grid[3][4] = 21345942;    grid[4][4] =  9380097;    grid[5][4] = 10600672;    grid[6][4] = 31527494;
        grid[1][5] =    77061;    grid[2][5] = 17552253;    grid[3][5] = 28094349;    grid[4][5] =  6899651;    grid[5][5] =  9250759;    grid[6][5] = 31663883;
        grid[1][6] = 33071741;    grid[2][6] =  6796745;    grid[3][6] = 25397450;    grid[4][6] = 24659492;    grid[5][6] =  1534922;    grid[6][6] = 27995004;
    }

    protected long next(final long start) {
        return (start * 252533) % 33554393;
    }
}