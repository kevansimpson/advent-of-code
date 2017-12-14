package org.base.advent.code2017;

import java.util.LinkedHashMap;
import java.util.Map;
import org.base.advent.Point;
import org.base.advent.Solution;


/**
 * <h2>Part 1</h2>
 * You come across an experimental new kind of memory stored on an infinite two-dimensional grid.
 *
 * Each square on the grid is allocated in a spiral pattern starting at a location marked 1 and then
 * counting up while spiraling outward. For example, the first few squares are allocated like this:
 *
 * 17  16  15  14  13
 * 18   5   4   3  12
 * 19   6   1   2  11
 * 20   7   8   9  10
 * 21  22  23---> ...
 *
 * While this is very space-efficient (no squares are skipped), requested data must be carried back
 * to square 1 (the location of the only access port for this memory system) by programs that can
 * only move up, down, left, or right. They always take the shortest path: the Manhattan Distance
 * between the location of the data and square 1.
 *
 * For example:
 *
 * Data from square 1 is carried 0 steps, since it's at the access port.
 * Data from square 12 is carried 3 steps, such as: down, left, left.
 * Data from square 23 is carried only 2 steps: up twice.
 * Data from square 1024 must be carried 31 steps.
 *
 * How many steps are required to carry the data from the square identified in your puzzle input all the way to the access port?
 *
 * <h2>Part 2</h2>
 * As a stress test on the system, the programs here clear the grid and then store the value 1 in square 1.
 * Then, in the same allocation order as shown above, they store the sum of the values in all adjacent squares,
 * including diagonals.
 *
 * So, the first few squares' values are chosen as follows:
 *
 * Square 1 starts with the value 1.
 * Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
 * Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
 * Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4.
 * Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
 *
 * Once a square is written, its value does not change. Therefore, the first few squares would receive the following values:
 *
 * 147  142  133  122   59
 * 304    5    4    2   57
 * 330   10    1    1   54
 * 351   11   23   25   26
 * 362  747  806--->   ...
 *
 * What is the first value written that is larger than your puzzle input?
 */
public class Day03 implements Solution<Integer> {

	@Override
	public Integer getInput() {
		return Integer.parseInt("265149");
	}

	@Override
	public Object solvePart1() {
		return distanceFromOrigin(getInput());
	}

	@Override
	public Object solvePart2() {
		return firstLargerValue(getInput());
	}

	public int distanceFromOrigin(int location) {
		return toPoint(location).getManhattanDistance();
	}

	public long firstLargerValue(int input) {
		Map<Point, Long> vmap = new LinkedHashMap<>();
		vmap.put(Point.ORIGIN, 1L);

		long firstLarger = -1;
		int index = 2;

		while (firstLarger <= input) {
			Point point = toPoint(index);
			firstLarger = spiralSum(point, vmap);
			++index;
		}

		return firstLarger;
	}

	public long spiralSum(Point point) {
		return spiralSum(point, new LinkedHashMap<>());
	}

	public long spiralSum(Point point, Map<Point, Long> vmap) {
		vmap.put(Point.ORIGIN, 1L);

		if (vmap.containsKey(point))
			return vmap.get(point);

		long sum = -1;
		int index = 2;
		Point target = null;

		while (!point.equals(target)) {
			target = toPoint(index);
			if (!vmap.containsKey(target)) {
				sum = target.surrounding().stream().mapToLong(pt -> vmap.getOrDefault(pt, 0L)).sum();
				vmap.put(target, sum);
			}
			++index;
		}

		return sum;
	}

	public Point toPoint(int location) {
		if (location == 1)
			return Point.ORIGIN;

		// lower-right corner of each spiral loop is n^2, where n is the loop index + 1 and increments by 2 for each loop
		int loop = 0;
		int index = 1 + loop * 2;
		int corner = (int) Math.pow(index, 2);
		while (location > corner) {
			++loop;
			index = 1 + loop * 2;
			corner = (int) Math.pow(index, 2);
		}

		// check bottom side
		if (location > (corner - index)) {
			Point target = new Point(loop - (corner - location), -loop);
			return target;
		}
		else {
			corner = corner - index + 1;
			// check left side
			if (location > (corner - index)) {
				Point target = new Point(-loop, -loop + (corner - location));
				return target;
			}
			else {
				corner = corner - index + 1;
				// check top side
				if (location > (corner - index)) {
					Point target = new Point(-loop + (corner - location), loop);
					return target;
				}
				else {
					corner = corner - index + 1;
					// check right side
					if (location > (corner - index)) {
						Point target = new Point(loop, loop - (corner - location));
						return target;
					}
				}
			}
		}

		return Point.ORIGIN;
	}
}
