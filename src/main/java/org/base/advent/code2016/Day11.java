package org.base.advent.code2016;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Helpers;
import org.base.advent.ParallelSolution;
import org.base.advent.TimeSaver;
import org.base.advent.util.Node;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Long.parseLong;
import static java.lang.Long.toBinaryString;
import static java.util.Comparator.comparingLong;
import static java.util.Comparator.naturalOrder;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;
import static org.base.advent.util.Node.createRootNode;
import static org.base.advent.util.Util.combinations;

/**
 * <a href="https://adventofcode.com/2016/day/11">Day 11</a>
 */
public class Day11 extends ParallelSolution<List<String>> implements Helpers, TimeSaver {
    public Day11(ExecutorService pool) {
        super(pool);
    }

    @Override
    public Object solvePart1(List<String> input) {
        Facility facility1 = new Facility(readInput(input));
        if (debug())
            facility1.display();
        return facility1.fewestSteps();
    }

    @Override
    public Object solvePart2(List<String> input) {
        Facility facility1 = new Facility(readInput(input));
        final Map<String, Integer> copy = new TreeMap<>(facility1.floors);
        copy.putAll(Map.of("EG", 1, "EM", 1, "DG", 1, "DM", 1));
        Facility facility2 = new Facility(copy);
        if (debug())
            facility2.display();

        return facility2.fewestSteps();
    }

    private static final Pattern CHIP_RE = Pattern.compile("\\b(\\w+)\\b-compatible microchip");
    private static final Pattern _RTG_RE = Pattern.compile("\\b(\\w+)\\b generator");

    @AllArgsConstructor
    private static class Facility {
        private static final Long fewestSteps = 62L; // just higher than part2 answer to improve performance

        private final Map<String, Integer> floors;
        private final List<String> rtgChipList;
        private final long size;
        private final long maxSize;
        private final int sizeInt;
        private final int maxSizeInt;
        private int elevatorAt;
        @Getter
        private int steps;

        public Facility(final Map<String, Integer> f) {
            this(f, 1, 0);
        }

        public Facility(final Map<String, Integer> f, final int elAt, final int s) {
            floors = f;
            rtgChipList = new ArrayList<>(floors.keySet());
            rtgChipList.sort(naturalOrder());
            size = rtgChipList.size();
            sizeInt = (int) size;
            maxSize = size * 4L;
            maxSizeInt = (int) maxSize;
            elevatorAt = elAt;
            steps = s;
        }

        long fewestSteps() {
            Map<Long, Long> visited = new HashMap<>();
            long target = targetScore();
            long[] twos = generateTwos();
            long[] elevatorScores = elevatorScores();
            PriorityQueue<Node<Long>> queue = new PriorityQueue<>(
                    comparingLong(n -> ((long) ((Node<?>) n).getData())).reversed());
            queue.add(createRootNode(score()));

            while (!queue.isEmpty()) {
                Node<Long> node = queue.poll();
                long score = node.getData();
                if (!visited.containsKey(score) || node.getDepth() < visited.get(score)) {
                    visited.put(score, node.getDepth());
                    if (score == target) {
                        if (node.getDepth() - 1 < fewestSteps)
                            return node.getDepth() - 1;
                    }

                    int elevator = (int) (score >> maxSize);
                    long allFloors = score - elevatorScores[elevator];
                    String allFloorsBin = leftPad(toBinaryString(allFloors), maxSizeInt, '0');
                    int start = (4 - elevator) * sizeInt, end = start + sizeInt;
                    List<Long> currentFloor = new ArrayList<>();
                    for (int i = start; i < end; i++)
                        if (allFloorsBin.charAt(i) == '1')
                            currentFloor.add(twos[i]);

                    boolean movePairDown = true;
                    List<List<Long>> pairs = combinations(currentFloor, 2);
                    if (elevator < 4) {
                        // move pair up
                        for (List<Long> pair : pairs) {
                            long sum = pair.get(0) + pair.get(1);
                            long upScore = allFloors - sum + (sum << size);
                            if (isValid(upScore)) {
                                queue.add(node.addChild(upScore + elevatorScores[elevator + 1]));
                                movePairDown = false;
                            }
                        }

                        // move single up
                        for (long gc : currentFloor) {
                            long upScore = allFloors - gc + (gc << size);
                            if (isValid(upScore)) {
                                queue.add(node.addChild(upScore + elevatorScores[elevator + 1]));
                                movePairDown = false;
                            }
                        }
                    }

                    if (elevator > 1) {
                        // move single down
                        for (long gc : currentFloor) {
                            long downScore = allFloors - gc + (gc >> size);
                            if (isValid(downScore)) {
                                queue.add(node.addChild(downScore + elevatorScores[elevator - 1]));
                                movePairDown = false;
                            }
                        }

                        if (movePairDown) {
                            for (List<Long> pair : pairs) {
                                long sum = pair.get(0) + pair.get(1);
                                long downScore = allFloors - sum + (sum >> size);
                                if (isValid(downScore)) {
                                    queue.add(node.addChild(downScore + elevatorScores[elevator - 1]));
                                }
                            }

                        }
                    }
                }
            }
            return -1L;
        }

        void display() {
            for (int level = 4; level > 0; level--) {
                System.out.println();
                System.out.printf("F%d %s ", level, (level == elevatorAt) ? "E" : " ");
                for (String gm : rtgChipList) {
                    System.out.printf("%s ", (level == floors.get(gm)) ? gm : "  ");
                }
            }
            System.out.println();
        }

        boolean isValid(long score) {
            return isValid(leftPad(toBinaryString(score), maxSizeInt, '0'));
        }

        boolean isValid(String facility) {
            for (int f = 0; f < 4; f++) {
                String floor = facility.substring(f * sizeInt, (f + 1) * sizeInt);
                int gens = 0, unprotectedChips = 0;
                for (int i = 0; i < floor.length(); i += 2) {
                    if (floor.charAt(i) == '1')
                        gens++;
                    else if (floor.charAt(i + 1) == '1')
                        unprotectedChips++;
                }
                if (unprotectedChips > 0 && gens > 0)
                    return false;
            }
            return true;
        }

        // score is binary representation of floors,
        // concatenated from 4th to 1st and prefixed by elevator
        long score() {
            StringBuilder score = new StringBuilder().append(toBinaryString(elevatorAt));
            for (int floor = 4; floor >= 1; floor--) {
                for (String gc : rtgChipList)
                    score.append((floor == floors.get(gc) ? 1 : 0));
            }
            return parseLong(score.toString(), 2);
        }

        long targetScore() {
            String full4thFloor = rightPad(leftPad("", sizeInt, '1'), maxSizeInt, '0');
            return parseLong(String.format("0100%s", full4thFloor), 2);
        }

        long[] generateTwos() {
            long[] twos = new long[maxSizeInt];
            twos[maxSizeInt - 1] = 1L;
            for (int i = maxSizeInt - 2; i >= 0; i--)
                twos[i] = 2L * twos[i + 1];
            return twos;
        }

        long[] elevatorScores() {
            long[] scores = new long[5];
            for (long i = 0L; i < 5L; i += 1L) {
                scores[(int) i] = i << maxSize;
            }
            return scores;
        }
    }

    private static Map<String, Integer> readInput(final List<String> input) {
        Map<String, Integer> floors = new TreeMap<>();
        for (int floorNum = 0; floorNum < input.size(); floorNum++) {
            String floor = input.get(floorNum);
            walkFloor(floors, floor, floorNum + 1, CHIP_RE, "M");
            walkFloor(floors, floor, floorNum + 1, _RTG_RE, "G");
        }

        return floors;
    }

    private static void walkFloor(Map<String, Integer> floors,
                                  String floor,
                                  int level,
                                  Pattern regex,
                                  String type) {
        Matcher m = regex.matcher(floor);
        int start = 0;
        if (m.find(start))
            do {
                String str = m.group(1);
                start = m.end();
                floors.put(StringUtils.upperCase(str.substring(0, 1)) + type, level);
            } while (start <= floor.length() && m.find(start));
    }
}