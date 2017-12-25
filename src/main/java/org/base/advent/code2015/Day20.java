package org.base.advent.code2015;


/**
 *
 */
public class Day20 {

	private static int MAX = 1000000;
	private static int TARGET = 34000000;

	public void solvePuzzle1() throws Exception {
		int[] houses = new int[MAX];

		for (int elf = 1; elf < MAX; elf++) {
			for (int visited = elf; visited < MAX; visited += elf) {
				houses[visited] += elf * 10;
			}
		}

		System.out.println("House # "+ findTargetHouse(houses));
	}

	public void solvePuzzle2() throws Exception {
		int[] houses = new int[MAX];

		for (int elf = 1; elf < MAX; elf++) {
			int count = 0;
			for (int visited = elf; visited < MAX; visited += elf) {
				houses[visited] += elf * 11;
				count += 1;
				if (count >= 50)
					break;
			}
		}

		System.out.println("House # "+ findTargetHouse(houses));
	}

	int findTargetHouse(int[] houses) {
		int answer = 0;
		for (int i = 0; i < MAX; i++) {
			if (houses[i] >= TARGET) {
				answer = i;
				break;
			}
		}

		return answer;
	}
}
