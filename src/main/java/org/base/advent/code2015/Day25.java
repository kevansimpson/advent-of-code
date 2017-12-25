package org.base.advent.code2015;

import org.apache.commons.lang3.StringUtils;
import org.base.advent.Point;

/**
 *
 */
public class Day25 {
	
	private static final int DIM = 6400;
	
	private long[][] grid = new long[DIM][DIM];
	
	// < 13431666
	public void solvePuzzle1() throws Exception {
		buildInitialGrid();
//		printGrid(20);
		// Enter the code at row 2978, column 3083.
		long next = 20151125;
		int index = 1;

		/*
		 * [1,1]							1
		 * [1,2]	[2,1]					2
		 * [1,3]	[2,2]	[3,1]			3
		 * [1,4]	[2,3]	[3,2]	[4,1]	4
		 */
		int iterations = 0;
		outer:
		for (;; index++) {
			debug("index = ", index);
			for (int x = 1, y = index; x <= index; x++, y--) {
				int row = x;
				int col = y;
				++iterations;
				debug("@ ", new Point(row, col));

				if (row == 2978 && col == 3083) {
					grid[row][col] = next;
					info("Code is ", next, " @ index = ", index);
//					printGrid(20, 2970, 3075);
					break outer;
				}
				if (grid[row][col] > 0l) {
					if (grid[row][col] != next) {
						debug("wrong @ ", new Point(row, col));
						throw new RuntimeException(String.valueOf(next));
					}
				}
				else
					grid[row][col] = next;
//				if (index > 8) break outer;	// temp

				if (next == 2650453) {
					info("found @ ", new Point(row, col), " @ index = ", index);
					info("iterations = ", iterations);
				}
				next = next(next);
				debug("next = ", next);
			}
		}
	}

	// plagiarized from answer found @ https://www.reddit.com/r/adventofcode/comments/3y5jco/day_25_solutions/
	public void solvePuzzle2() throws Exception {
		final int row = 2978;
		final int col = 3083;
		long code = 20151125;

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

		info("iterations = ", iterations);
		System.out.println("The code to enter is "+ code);
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
	void buildInitialGrid() {
		grid[1][1] = 20151125;	grid[2][1] = 18749137;	grid[3][1] = 17289845;	grid[4][1] = 30943339;	grid[5][1] = 10071777;	grid[6][1] = 33511524;
		grid[1][2] = 31916031;	grid[2][2] = 21629792;	grid[3][2] = 16929656;	grid[4][2] =  7726640;	grid[5][2] = 15514188;	grid[6][2] =  4041754;
		grid[1][3] = 16080970;	grid[2][3] =  8057251;	grid[3][3] =  1601130;	grid[4][3] =  7981243;	grid[5][3] = 11661866;	grid[6][3] = 16474243;
		grid[1][4] = 24592653;	grid[2][4] = 32451966;	grid[3][4] = 21345942;	grid[4][4] =  9380097;	grid[5][4] = 10600672;	grid[6][4] = 31527494;
		grid[1][5] =    77061;	grid[2][5] = 17552253;	grid[3][5] = 28094349;	grid[4][5] =  6899651;	grid[5][5] =  9250759;	grid[6][5] = 31663883;
		grid[1][6] = 33071741;	grid[2][6] =  6796745;	grid[3][6] = 25397450;	grid[4][6] = 24659492;	grid[5][6] =  1534922;	grid[6][6] = 27995004;
	}

	void printGrid(int depth) {
		printGrid(depth, 1, 1);
	}

	void printGrid(int depth, int startX, int startY) {
		for (int y = startY; y < depth + startY; y++) {
			for (int x = startX; x < depth + startX; x++) {
				System.out.print(String.format("%10d\t", grid[x][y]));
			}
			System.out.println("");
		}
	}
	long next(long start) {
		return (start * 252533) % 33554393;
	}

	void debug(Object... args) {
//		System.out.println(StringUtils.join(args));
	}
	void info(Object... args) {
		System.out.println(StringUtils.join(args));
	}

}