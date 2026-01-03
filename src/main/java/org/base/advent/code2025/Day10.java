package org.base.advent.code2025;

import org.base.advent.ParallelSolution;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.ToLongFunction;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.Math.min;
import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
/**
 * <a href="https://adventofcode.com/2025/day/10">Day 10</a>
 */
public class Day10 extends ParallelSolution<List<String>> {
    record Machine(long target, List<List<Integer>> buttons, List<Integer> joltage) {
        boolean matches(int[] presses) {
            int[] counterValues = new int[joltage.size()];

            for (int i = 0; i < presses.length; i++) {
                int numPresses = presses[i];
                List<Integer> affectedCounters = buttons.get(i);

                for (int counter : affectedCounters) {
                    counterValues[counter] += numPresses;
                }
            }

            for (int i = 0; i < joltage.size(); i++) {
                if (counterValues[i] != joltage.get(i)) {
                    return false;
                }
            }

            return true;
        }
        int getMaxPossibleForButton(int buttonIndex, int[] currentPresses) {
            int max = 0;
            List<Integer> affectedCounters = buttons.get(buttonIndex);

            for (int counter : affectedCounters) {
                int currentValue = 0;
                for (int btn = 0; btn < buttonIndex; btn++) {
                    if (buttons.get(btn).contains(counter)) {
                        currentValue += currentPresses[btn];
                    }
                }

                int remaining = joltage.get(counter) - currentValue;
                max = Math.max(max, Math.max(0, remaining));
            }

            return max;
        }
    }

    static {
        Loader.loadNativeLibraries();
    }

    public Day10(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        return input.parallelStream().map(Day10::make).mapToInt(Day10::part1).sum();
    }

    @Override
    public Object solvePart2(List<String> input) {
        ToLongFunction<Machine> fxn = (input.size() == 3)
                ? Day10::part2a : Day10::part2; 
        return input.parallelStream().map(Day10::make).mapToLong(fxn).sum();
    }

    static int part1(Machine m) {
        PriorityQueue<List<Long>> queue = new PriorityQueue<>(Comparator.comparingInt(List::size));
        queue.add(List.of(0L));
        Map<Long, Integer> visited = new HashMap<>();
        int minSteps = Integer.MAX_VALUE;

        while (!queue.isEmpty()) {
            List<Long> path = queue.poll();
            int lastIx = path.size() - 1;
            long last = path.get(lastIx);
            if (last == m.target) {
                minSteps = min(minSteps, path.size());
                visited.put(last, path.size());
            }
            else if (!visited.containsKey(last) || visited.get(last) > path.size()) {
                visited.put(last, path.size());
                for (List<Integer> button : m.buttons) {
                    List<Long> newPath = new ArrayList<>(path);
                    newPath.add(flip(last, button));
                    queue.add(newPath);
                }
            }
        }

        return minSteps - 1;
    }

    static int part2(Machine m) {
        MPSolver solver = MPSolver.createSolver("SCIP");
        int numButtons = m.buttons.size();
        int numCounters = m.joltage.size();

        // Create variables: how many times to press each button
        MPVariable[] buttonPresses = new MPVariable[numButtons];
        for (int i = 0; i < numButtons; i++) {
            buttonPresses[i] = solver.makeIntVar(0, Double.POSITIVE_INFINITY, "button_" + i);
        }

        // Create constraints: one equation per counter
        for (int counter = 0; counter < numCounters; counter++) {
            MPConstraint constraint = solver.makeConstraint(
                    m.joltage.get(counter),  // must equal target
                    m.joltage.get(counter),
                    "counter_" + counter
            );

            // Add coefficients for buttons that affect this counter
            for (int btn = 0; btn < numButtons; btn++) {
                if (m.buttons.get(btn).contains(counter)) {
                    constraint.setCoefficient(buttonPresses[btn], 1);
                }
            }
        }

        // Objective: minimize total button presses
        MPObjective objective = solver.objective();
        for (int i = 0; i < numButtons; i++) {
            objective.setCoefficient(buttonPresses[i], 1);
        }
        objective.setMinimization();

        // Solve
        MPSolver.ResultStatus status = solver.solve();

        if (status == MPSolver.ResultStatus.OPTIMAL) {
            return (int) Math.round(objective.value());
        }

        return -1;
    }
    
    static int part2a(Machine m) {
        int[] best = new int[]{Integer.MAX_VALUE};
        int[] presses = new int[m.buttons.size()];
        dfs(presses, 0, 0, m, best);
        return best[0];
    }

    static void dfs(int[] presses, int buttonIndex, int totalSoFar,
                    Machine m, int[] best) {
        if (totalSoFar >= best[0])
            return;

        if (buttonIndex == m.buttons.size()) {
            if (m.matches(presses))
                best[0] = min(best[0], totalSoFar);
            return;
        }
        for (int i = 0; i <= m.getMaxPossibleForButton(buttonIndex, presses); i++) {
            presses[buttonIndex] = i;
            dfs(presses, buttonIndex + 1, totalSoFar + i, m, best);
        }
    }

    static long flip(long number, List<Integer> button) {
        long mask = 0L;
        for (int ix : button)
            mask |= (1L << ix);
        return number ^ mask;
    }

    static Machine make(String input) {
        String[] split = input.split(" ");
        String bin = new StringBuilder(split[0].substring(1))
                .reverse().toString()
                .replaceAll("]", "")
                .replaceAll("\\.", "0")
                .replaceAll("#", "1");
        List<List<Integer>> buttons = new ArrayList<>();
        for (String btn : Arrays.copyOfRange(split, 1, split.length - 1))
            buttons.add(split(btn));

        return new Machine(parseLong(bin, 2), buttons, split(split[split.length - 1]));
    }

    static List<Integer> split(String str) {
        List<Integer> list = new ArrayList<>();
        for (String s : str.substring(1, str.length() - 1).split(","))
            list.add(parseInt(s));
        return list;
    }
}

