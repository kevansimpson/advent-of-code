package org.base.advent.code2022;

import lombok.experimental.Delegate;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.TimeSaver;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.floorDiv;
import static org.base.advent.util.Util.combinations;
import static org.base.advent.util.Text.extractInt;

/**
 * <a href="https://adventofcode.com/2022/day/16">Day 16</a>
 */
public class Day16 implements Function<List<String>, Day16.ValvePath>, TimeSaver {
    public record ValvePath(int mostEfficient, int withElephant) {}

    private static final String START = "AA";
    private final ExecutorService pool = fastOrFull(null, () -> Executors.newFixedThreadPool(10));

    @Override
    public ValvePath apply(final List<String> input) {
        final ValveMap valveMap = new ValveMap(input);
        int mostEfficient = mostEfficientPath(valveMap.important, "AA", 30);
        int withElephant = fastOrFull(2594, () -> mostElephantPath(valveMap.important));
        return new ValvePath(mostEfficient, withElephant);
    }

    int mostElephantPath(final Map<String, Valve> valveMap) {
        Valve startNode = valveMap.get(START);
        Map<String, Valve> unopened = new HashMap<>(valveMap);
        unopened.remove(START);
        int count = floorDiv(unopened.size(), 2);
        int mod = unopened.size() % 2;
        List<List<Valve>> allPaths = combinations(new ArrayList<>(unopened.values()), count + mod);
        List<Future<Integer>> tasks = allPaths.stream().map(path -> pool.submit(() -> {
            List<List<Valve>> temp = new ArrayList<>(allPaths);
            for (Valve valve : path) {
                List<Valve> list = temp.stream()
                        .filter(it -> it.get(0).name.equals(valve.name)).findFirst().orElse(null);
                int index = list == null ? -1 : temp.indexOf(list);
                if (index > -1)
                    temp.remove(index);
            }

            final Map<String, Valve> a =
                    path.stream().collect(Collectors.toMap(Valve::name, Function.identity()));
            a.put(START, startNode);

            final Map<String, Valve> b =
                    temp.stream().flatMap(List::stream)
                            .filter(it -> !a.containsKey(it.name))
                            .collect(Collectors.toMap(Valve::name, Function.identity(), (one, two) -> one));
            b.put(START, startNode);
            return mostEfficientPath(a, START, 26) + mostEfficientPath(b, START, 26);
        })).toList();

        try {
            final TreeSet<Integer> set = new TreeSet<>();
            for (Future<Integer> task : tasks)
                set.add(task.get());
            return set.isEmpty() ? 1138 : set.last();
        }
        catch (Exception ex) {
            return -1;
        }
    }

    public int mostEfficientPath(Map<String, Valve> valveMap, String start, int duration) {
        ArrayDeque<Path> queue = new ArrayDeque<>();
        Path best = new Path(valveMap.get(start));
        queue.addFirst(best);
        while (!queue.isEmpty()) {
            Path current = queue.removeFirst();
            current.visited.add(current.valve.name);
            if (current.flowInfo.totalFlow > best.flowInfo.totalFlow)
                best = current;

            for (Valve edge : valveMap.values()) {
                if (current.visited.contains(edge.name) ||
                        current.valve.name.equals(edge.name) ||
                        current.flowInfo.steps + current.valve.edges.get(edge.name) > duration)
                    continue;

                FlowInfo calculated = calculateFlow(current, edge, duration,
                        new FlowInfo(current.flowInfo.steps + current.valve.edges.get(edge.name)));
                if (calculated.totalFlow < best.flowInfo.totalFlow &&
                    calculated.steps >= best.flowInfo.steps)
                    continue;

                queue.addLast(new Path(edge, new ArrayList<>(current.visited), calculated));
            }
        }

        return best.flowInfo.totalFlow;
    }

    FlowInfo calculateFlow(Path current, Valve edge, int duration, FlowInfo flowInfo) {
        int newFlow = (current.flowInfo.flowRate * current.valve.edges.get(edge.name)) + current.flowInfo.flow;
        int newRate = current.flowInfo.flowRate + edge.flowRate;
        return new FlowInfo(
                flowInfo.steps, newRate, newFlow, (newRate * (duration - flowInfo.steps)) + newFlow);
    }

    private static class ValveMap implements Map<String, Valve> {
        @Delegate()
        private final Map<String, Valve> data;
        private final Map<String, Valve> important;

        public ValveMap(List<String> caves) {
            this(caves.stream().map(ValveMap::scanCave).collect(Collectors.toMap(Valve::name, Function.identity())));
        }

        public ValveMap(Map<String, Valve> map) {
            data = map;
            data.values().forEach(this::shortestPaths);
            important = data.values().stream()
                    .filter(it -> it.flowRate > 0 || "AA".equals(it.name))
                    .collect(Collectors.toMap(Valve::name, Function.identity()));
        }

        void shortestPaths(Valve start) {
            for (Valve valve : data.values()) {
                if (valve.name.equals(start.name) || valve.flowRate == 0)
                    continue;

                Set<String> visited = new HashSet<>();
                ArrayDeque<String> queue = new ArrayDeque<>();
                queue.addLast(start.name);
                ArrayDeque<Pair<String, Valve>> path = new ArrayDeque<>();
                while (!queue.isEmpty()) {
                    String currentName = queue.removeFirst();
                    Valve current = data.get(currentName);
                    if (current.tunnels.contains(valve.name)) {
                        start.edges.put(valve.name, tracePaths(path, current, valve));
                        break;
                    }

                    for (String tunnel : current.tunnels) {
                        if (visited.contains(tunnel))
                            continue;
                        path.addLast(Pair.of(tunnel, current));
                        queue.addLast(tunnel);
                    }
                    visited.add(currentName);
                }
            }
        }

        private int tracePaths(ArrayDeque<Pair<String, Valve>> path, Valve current, Valve end) {
            List<Valve> list = new ArrayList<>(Arrays.asList(end, current));
            Valve temp = current;

            while (path.size() > 0) {
                Pair<String, Valve> pair = path.removeLast();
                if (pair.getLeft().equals(temp.name)) {
                    list.add(pair.getRight());
                    temp = pair.getRight();
                }
            }
            return list.size();
        }

        private static Valve scanCave(String report) {
            String[] str = report.split(" ");
            return new Valve(str[1], extractInt(str[4])[0],
                    Stream.of(ArrayUtils.subarray(str, 9, str.length))
                            .map(it -> it.replaceAll(",", "")).toList(),
                    new HashMap<>());
        }
    }

    public record Valve(String name, int flowRate, List<String> tunnels, Map<String, Integer> edges) {}

    public record Path(Valve valve, List<String> visited, FlowInfo flowInfo) {
        public Path(Valve v) {
            this(v, new ArrayList<>(), new FlowInfo());
        }
    }

    public record FlowInfo(int steps, int flowRate, int flow, int totalFlow) {
        public FlowInfo() {
            this(0, 0, 0, 0);
        }

        public FlowInfo(int steps) {
            this(steps, 0, 0, 0);
        }
    }
}
