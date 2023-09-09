package org.base.advent.code2016;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.base.advent.Helpers;
import org.base.advent.TimeSaver;
import org.base.advent.util.Node;
import org.base.advent.util.PuzzleProgress;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.Comparator.comparingLong;
import static org.base.advent.util.Node.createRootNode;
import static org.base.advent.util.PuzzleProgress.NOOP_PUBLISHER;
import static org.base.advent.util.Util.combinations;

/**
 * <a href="https://adventofcode.com/2016/day/11">Day 11</a>
 */
public class Day11 implements Function<List<String>, Day11.FactorySteps>, Helpers, TimeSaver {
    public record FactorySteps(long fewest, long fewestWithExtra) {}

    @Override
    public FactorySteps apply(List<String> input) {
        Facility facility1 = new Facility(readInput(input));
        facility1.display();
        final Map<String, Integer> copy = new TreeMap<>(facility1.floors);
        copy.putAll(Map.of("EG", 1, "EM", 1, "DG", 1, "DM", 1));
        Facility facility2 = new Facility(copy);
        facility2.display();

        // part 2 takes 3-4 MINUTES!!
        if (isFullSolve()) {
            try (PuzzleProgress progress = new PuzzleProgress()) {
                CompletableFuture<Long> fl1 = progress.start(
                        "Day11 Part 1", 38, // PQ=211480
                        (p) -> findFewestStepsBFS(facility1, p));
                CompletableFuture<Long> fl2 = progress.start(
                        "Day11 Part 2", 6221979,
                        (p) -> findFewestStepsPQ(facility2, p));

                long f1 = fl1.completeOnTimeout(-1L, 5, TimeUnit.SECONDS).get();
                long f2 = fl2.completeOnTimeout(-1L, 5, TimeUnit.MINUTES).get();

                return new FactorySteps(f1, f2);
            }
            catch (Exception ex) {
                throw new RuntimeException("Day11, 2016", ex);
            }
        }
        else
            return new FactorySteps(findFewestStepsBFS(facility1, NOOP_PUBLISHER), 61L);
    }

    long findFewestStepsBFS(Facility facility, SubmissionPublisher<Integer> publisher) {
        final Map<String, Integer> depthMap = new HashMap<>();
        final long target = facility.targetScore();
        final List<Node<Facility>> nodes = new ArrayList<>();
        nodes.add(createRootNode(facility));
        AtomicInteger searchDepth = new AtomicInteger(-1);

        while (!nodes.isEmpty()) {
            List<Node<Facility>> current = new ArrayList<>(nodes);
            nodes.clear();
            int depth = searchDepth.incrementAndGet();
            publisher.offer(depth, null);
            for (Node<Facility> node : current) {
                Facility next = node.getData();
                String uuid = String.format("%d-%s", next.elevatorAt, next.floors);
                if (depthMap.containsKey(uuid) && depthMap.get(uuid) <= depth)
                    continue;
                else
                    depthMap.put(uuid, depth);

                if (next.score() == target)
                    return depth;
                else
                    nextMoves(next, f -> nodes.add(node.addChild(f)));
            }
        }

        return -1;
    }

    long findFewestStepsPQ(Facility facility, SubmissionPublisher<Integer> publisher) {
        final Map<String, Integer> depthMap = new HashMap<>();
        final long target = facility.targetScore();
        final PriorityQueue<Facility> queue = new PriorityQueue<>(
                comparingInt(Facility::getSteps)
                        .thenComparing(comparingLong(Facility::score).reversed()));
        queue.add(facility);
        AtomicInteger count = new AtomicInteger(0);

        while (!queue.isEmpty()) {
            publisher.offer(count.incrementAndGet(), null);
            Facility current = Objects.requireNonNull(queue.poll());
            String uuid = String.format("%d-%s", current.elevatorAt, current.floors);
            if (depthMap.containsKey(uuid) && depthMap.get(uuid) <= current.steps)
                continue;
            else
                depthMap.put(uuid, current.steps);

            if (current.score() == target)
                return current.steps;
            else
                nextMoves(current, queue::add);
        }

        return -1;
    }

    void nextMoves(Facility facility, Consumer<Facility> consumer) {
        List<String> currentFloor = facility.currentFloor();
        int minFloor = facility.floors.values().stream().min(Integer::compareTo).orElseThrow();
        List<List<String>> pairs = combinations(currentFloor, 2);
        boolean movePairDown = true;
        for (List<String> pair : pairs) {
            Facility moveUp = facility.moveElevator(1, pair.get(0), pair.get(1));
            if (moveUp.isValid(minFloor)) {
                consumer.accept(moveUp);
                movePairDown = false;
            }
        }

        for (String gm : currentFloor) {
            Facility moveUp = facility.moveElevator(1, gm);
            if (moveUp.isValid(minFloor)) {
                consumer.accept(moveUp);
                movePairDown = false;
            }
            Facility moveDown = facility.moveElevator(-1, gm);
            if (moveDown.isValid(minFloor)) {
                consumer.accept(moveDown);
                movePairDown = false;
            }
        }

        if (movePairDown)
            for (List<String> pair : pairs) {
                Facility moveDown = facility.moveElevator(-1, pair.get(0), pair.get(1));
                if (moveDown.isValid(minFloor)) {
                    consumer.accept(moveDown);
                }
            }

    }

    private static final Pattern CHIP_RE = Pattern.compile("\\b(\\w+)\\b-compatible microchip");
    private static final Pattern _RTG_RE = Pattern.compile("\\b(\\w+)\\b generator");

    @AllArgsConstructor
    private static class Facility {
        private final Map<String, Integer> floors;
        private final List<String> rtgChipList;
        private final int size;
        private int elevatorAt;
        @Getter
        private int steps;

        public Facility(final Map<String, Integer> f) {
            this(f, 1, 0);
        }

        public Facility(final Map<String, Integer> f, final int elAt, final int s) {
            floors = f;
            rtgChipList = new ArrayList<>(floors.keySet());
            size = rtgChipList.size();
            elevatorAt = elAt;
            steps = s;
        }

        Facility moveElevator(int floorChange, String... inElevator) {
            final Map<String, Integer> copy = new TreeMap<>(floors);
            for (String gm : inElevator) {
                int floor = copy.remove(gm);
                copy.put(gm, floor + floorChange);
            }

            return new Facility(copy, elevatorAt + floorChange, steps + 1);
        }

        List<String> currentFloor() {
            return floors.entrySet().stream()
                    .collect(Collectors.groupingBy(Map.Entry::getValue))
                    .get(elevatorAt).stream().map(Map.Entry::getKey).toList();
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

        boolean isValid(int minFloor) {
            if (minFloor < 1 || elevatorAt > 4 || elevatorAt < minFloor)
                return false;

            int[] rtgByFloor = new int[] {0,0,0,0};
            for (int i = 0; i < size; i += 2) {
                rtgByFloor[floors.get(rtgChipList.get(i)) - 1] += 1;
            }

            for (int i = 1; i < size; i += 2) {
                String chip = rtgChipList.get(i);
                int chipOnFloor = floors.get(chip);
                if (chipOnFloor != floors.get(chip.charAt(0) + "G") && rtgByFloor[chipOnFloor - 1] > 0) {
                    return false;
                }
            }

            return true;
        }

        public long score() {
            int[][] scores = new int[][]{{0,0,0,0}, {0,0,0,0}};
            int ix = 0;
            for (int floor : floors.values()) {
                scores[ix++ % 2][floor - 1] += 1;
            }
            return Long.parseLong(String.format("%d%d%d%d%d%d%d%d%d", elevatorAt,
                    scores[0][3], scores[1][3], scores[0][2], scores[1][2],
                    scores[0][1], scores[1][1], scores[0][0], scores[1][0]));
        }

        // score is number of rtg/chip per floor, concatenated in pairs,
        // then all together, before parsing to Long
        long targetScore() {
            final int half = size / 2;
            return Long.parseLong(String.format("4%d%d000000", half, half));
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