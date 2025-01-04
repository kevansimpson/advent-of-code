package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.base.advent.Solution;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2024/day/24">Day 24</a>
 * Shout out to <a href="https://github.com/JoanaBLate/advent-of-code-js/blob/main/2024/day24-solve2.js">JoanaBLate</a>
 * for knowing how to solve Part 2 without
 * <a href="https://en.wikipedia.org/wiki/Adder_(electronics)#Ripple-carry_adder">Adders</a>
 */
@AllArgsConstructor
public class Day24 implements Solution<List<String>> {
    final int totalSwaps;

    @Override
    public Object solvePart1(List<String> input) {
        return scan(input).resolveAll("z");
    }

    @Override
    public Object solvePart2(List<String> input) {
        LiveSystem system = scan(input);
        if (totalSwaps == 8) {
            Gate[] gateLevels = system.firstLevelGates();
            Set<String> swaps = new TreeSet<>();
            while (true) {
                system.identifySwappedWires(gateLevels, swaps);
                if (swaps.size() == totalSwaps) break;
                gateLevels = system.nextLevelGates(gateLevels, swaps);
            }
            return String.join(",", swaps);
        }
        else {
            // brute force for example
            long x = system.resolveAll("x");
            system.reset();
            long y = system.resolveAll("y");
            system.reset();
            long target = x & y;

            List<String> combo = new ArrayList<>();
            for (int i = 0; i < totalSwaps; i++)
                combo.add("");
            List<String> allGateKeys = new ArrayList<>(system.gatesByOutput.keySet());
            return solveFor(target, combo, 0, 0, allGateKeys, system);
        }
    }


    String solveFor(long target, List<String> combo, int index, int depth,
                    List<String> allGateKeys, LiveSystem system) {
        if (depth > totalSwaps)
            return null;
        if (depth == totalSwaps) {
            String[] array = combo.toArray(new String[0]);
            if (target == system.swap(array)) {
                Arrays.sort(array);
                return String.join(",", array);
            }
            system.reset();
            return null;
        }

        String answer;
        for (int i = 0; i < allGateKeys.size(); i++) {
            String nextGate = allGateKeys.get(i);
            if (!combo.contains(nextGate)) {
                combo.set(index, nextGate);
                answer = solveFor(target, combo, index + 1, depth + 1, allGateKeys, system);
                if (answer != null)
                    return answer;
            }
        }
        return null;
    }

    record LiveSystem(Map<String, Long> initialWires,
                      Map<String, Gate> gatesByOutput,
                      Map<String, List<Gate>> gatesByInput,
                      Map<String, Long> resolved) {

        long resolveAll(String prefix) {
            StringBuilder output = new StringBuilder();
            for (int c = wireCount(prefix) - 1; c >= 0; c--)
                output.append(resolve(String.format("%s%02d", prefix, c)));

            return parseLong(output.toString(), 2);
        }

        long resolve(String output) {
            if (!resolved.containsKey(output)) {
                Gate gate = gatesByOutput.get(output);
                long left = resolve(gate.left);
                long right = resolve(gate.right);
                switch (gate.op) {
                    case "AND" -> resolved.put(output, left & right);
                    case "OR" -> resolved.put(output, left | right);
                    case "XOR" -> resolved.put(output, left ^ right);
                }
            }

            return resolved.get(output);
        }

        Gate[] nextLevelGates(Gate[] current, Set<String> swaps) {
            List<Gate> next = new ArrayList<>();
            for (Gate level : current) {
                String wire = level.output;
                if (!wire.startsWith("z") && !swaps.contains(wire)) {
                    gatesByInput.get(wire).forEach(g -> {
                        g.type = level.type +" "+ level.op;
                        g.nextOps = findNextOps(g.output);
                        next.add(g);
                    });
                }
            }

            return next.toArray(new Gate[0]);
        }

        void identifySwappedWires(Gate[] gateLevels, Set<String> swaps) {
            Map<String, List<String>> outputById = new HashMap<>();
            for (Gate level : gateLevels) {
                String id = level.id();
                if (!isOutlier(level, id)) {
                    if (!outputById.containsKey(id))
                        outputById.put(id, new ArrayList<>());
                    outputById.get(id).add(level.output);
                }
            }

            for (List<String> outputs : outputById.values())
                if (outputs.size() <= 8)
                    swaps.addAll(outputs);
        }

        boolean isOutlier(Gate gate, String id) {
            return switch (id) {
                case "XOR~~XY~~ZZZ", "AND~~XY~~AND-XOR" -> gate.left.endsWith("00");
                case "OR~~XY AND~~ZZZ" -> gate.output.startsWith("z") &&
                                parseInt(gate.output.substring(1)) == initialWires().size() / 2;
                default -> false;
            };
        }

        Gate[] firstLevelGates() {
            Gate[] firstLevel = new Gate[initialWires.size()];
            for (String out : gatesByOutput.keySet()) {
                Gate g = gatesByOutput.get(out);
                if (g.left.startsWith("x") || g.left.startsWith("y")) {
                    g.type = "XY";
                    g.nextOps = findNextOps(out);
                    int index = parseInt(g.left.substring(1)) * 2 + (g.op.equals("AND") ? 0 : 1);
                    firstLevel[index] = g;
                }
            }

            return firstLevel;
        }

        String findNextOps(String outWire) {
            return (outWire.startsWith("z")) ? "ZZZ" :
                    gatesByInput.get(outWire).stream()
                            .map(Gate::getOp).sorted().collect(Collectors.joining("-"));
        }


        long swap(String... toSwap) {
            Gate[] originalGates = new Gate[toSwap.length];
            for (int i = 0; i < toSwap.length; i++)
                originalGates[i] = gatesByOutput.get(toSwap[i]);

            try {
                for (int i = 0; i < toSwap.length; i += 2) {
                    gatesByOutput.put(toSwap[i], originalGates[i + 1]);
                    gatesByOutput.put(toSwap[i + 1], originalGates[i]);
                }
                return resolveAll("z");
            }
            finally {
                for (int i = 0; i < toSwap.length; i++)
                    gatesByOutput.put(toSwap[i], originalGates[i]);
            }
        }

        void reset() {
            resolved.clear();
            resolved.putAll(initialWires);
        }

        int wireCount(String prefix) {
            int count = 0;
            for (String out : gatesByOutput.keySet())
                if (out.startsWith(prefix))
                    count++;
            if (count == 0)
                for (String out : resolved.keySet())
                    if (out.startsWith(prefix))
                        count++;
            return count;
        }
    }

    LiveSystem scan(List<String> input) {
        List<List<String>> data = splitByBlankLine(input);
        Map<String, Long> initialWires = new HashMap<>();
        for (String wire : data.get(0)) {
            String[] vals = wire.split(": ");
            initialWires.put(vals[0], parseLong(vals[1]));
        }

        Map<String, Gate> gatesByOutput = new TreeMap<>();
        Map<String, List<Gate>> gatesByInput = new HashMap<>();
        for (String conn : data.get(1)) {
            String[] wires = conn.split(" ");
            Gate g = new Gate(wires[0], wires[1], wires[2], wires[4], "", "");
            gatesByOutput.put(wires[4], g);

            // all wires used in 2 gates have 1 AND + 1 XOR
            // all wires used in 1 gate are all OR, none of which are XY
            if (!gatesByInput.containsKey(g.left))
                gatesByInput.put(g.left, new ArrayList<>());
            gatesByInput.get(g.left).add(g);
            if (!gatesByInput.containsKey(g.right))
                gatesByInput.put(g.right, new ArrayList<>());
            gatesByInput.get(g.right).add(g);
        }

        return new LiveSystem(
                initialWires, gatesByOutput, gatesByInput, new HashMap<>(initialWires));
    }

    @Getter
    @AllArgsConstructor
    private static class Gate {
        String left, op, right, output, type, nextOps;

        public String id() {
            return String.format("%s~~%s~~%s", op, type, nextOps);
        }
    }
}

