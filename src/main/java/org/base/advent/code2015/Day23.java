package org.base.advent.code2015;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.base.advent.Solution;

/**
 *
 */
public class Day23 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input23.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	private Map<String, Integer> registers = new HashMap<>();

	public void solvePuzzle1() throws Exception {
		registers.put("a", Integer.valueOf(0));
		registers.put("b", Integer.valueOf(0));
		List<String> instructions = getInput();
		followInstructions(instructions, 0);
		System.out.println("The value in register b is "+ registers.get("b"));
	}

	public void solvePuzzle2() throws Exception {
		registers.put("a", Integer.valueOf(1));
		registers.put("b", Integer.valueOf(0));
		List<String> instructions = getInput();
		followInstructions(instructions, 0);
		System.out.println("The value in register b if a=1 is "+ registers.get("b"));
	}

	void followInstructions(List<String> instructions, int index) {
		if (index < 0 || index >= instructions.size())
			return;

		String[] tkns = instructions.get(index).replace(",", "").split("\\s");
		switch (tkns[0]) {
			case "hlf":
				Integer hlf = registers.get(tkns[1]);
				registers.put(tkns[1], hlf / 2);
				followInstructions(instructions, index + 1);
				break;
			case "tpl":
				Integer tpl = registers.get(tkns[1]);
				registers.put(tkns[1], tpl * 3);
				followInstructions(instructions, index + 1);
				break;
			case "inc":
				Integer inc = registers.get(tkns[1]);
				registers.put(tkns[1], inc + 1);
				followInstructions(instructions, index + 1);
				break;
			case "jmp":
				followInstructions(instructions, index + Integer.parseInt(tkns[1]));
				break;
			case "jie":
				Integer jie = registers.get(tkns[1]);
				if ((jie.intValue() % 2) == 0)
					followInstructions(instructions, index + Integer.parseInt(tkns[2]));
				else
					followInstructions(instructions, index + 1);
				break;
			case "jio":
				Integer jio = registers.get(tkns[1]);
				if (jio.intValue() == 1)
					followInstructions(instructions, index + Integer.parseInt(tkns[2]));
				else
					followInstructions(instructions, index + 1);
				break;
		}
	}
}
