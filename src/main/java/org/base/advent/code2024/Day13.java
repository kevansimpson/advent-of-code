package org.base.advent.code2024;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.base.advent.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.base.advent.util.Text.extractLong;
import static org.base.advent.util.Util.safeGet;
import static org.base.advent.util.Util.splitByBlankLine;

/**
 * <a href="https://adventofcode.com/2024/day/13">Day 13</a>
 */
@AllArgsConstructor
public class Day13 implements Function<List<String>, Pair<Long, Long>> {
    private final ExecutorService pool;

    @Override
    public Pair<Long, Long> apply(List<String> input) {
        List<Machine> machines = installMachines(input);
        long resultNear = 0L, resultFar = 0L;
        List<CompletableFuture<Long>> plays = new ArrayList<>();
        List<CompletableFuture<Long>> longPlays = new ArrayList<>();
        for (Machine m : machines)
            plays.add(supplyAsync(() -> playCramersRule(m), pool));
        for (CompletableFuture<Long> future : plays)
            resultNear += safeGet(future);

        for (Machine m : machines)
            longPlays.add(supplyAsync(() -> playCramersRule(m.further()), pool));
        for (CompletableFuture<Long> future : longPlays)
            resultFar += safeGet(future);

        return Pair.of(resultNear, resultFar);
    }

    /**
     * <a href="https://www.youtube.com/watch?v=KOUjAzDyeZY">Cramer's Rule</a>
     * <ul>
     *     <li><pre>Determinant   (D)  =      (a.y * b.x) - (b.y * a.x)</pre></li>
     *     <li><pre>Determinant A (Da) = (target.y * b.x) - (b.y * target.x)</pre></li>
     *     <li><pre>Determinant B (Db) = (a.y * target.x) - (target.y * a.x)</pre></li>
     *     <li><pre>a = Da / D where Da % D == 0</pre></li>
     *     <li><pre>b = Db / D where Db % D == 0</pre></li>
     * </ul>
     */
    private long playCramersRule(Machine machine) {
        long determinant = (machine.btnA.y * machine.btnB.x) - (machine.btnB.y * machine.btnA.x);
        long detA = (machine.target.y * machine.btnB.x) - (machine.btnB.y * machine.target.x);
        long detB = (machine.btnA.y * machine.target.x) - (machine.target.y * machine.btnA.x);
        if ((detA % determinant) == 0L && (detB % determinant) == 0L) {
            long a = detA / determinant;
            long b = detB / determinant;
            return 3L * a + b;
        }
        return 0L;
    }

    private List<Machine> installMachines(List<String> input) {
        List<Machine> machines = new ArrayList<>();
        List<List<String>> instructions = splitByBlankLine(input);
        for (List<String> instr : instructions) {
            long[] a = extractLong(instr.get(0));
            long[] b = extractLong(instr.get(1));
            long[] t = extractLong(instr.get(2));
            machines.add(new Machine(Point.of(a[0], a[1]), Point.of(b[0], b[1]), Point.of(t[0], t[1])));
        }

        return machines;
    }

    record Machine(Point btnA, Point btnB, Point target) {
        public Machine further() {
            return new Machine(btnA, btnB, Point.of(target.x + 10000000000000L, target.y + 10000000000000L));
        }
    }
}

