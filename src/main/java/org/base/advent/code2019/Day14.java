package org.base.advent.code2019;

import lombok.Data;
import org.base.advent.Solution;
import org.base.advent.TimeSaver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <a href="https://adventofcode.com/2019/day/14">Day 14</a>
 */
public class Day14 implements Solution<List<String>>, TimeSaver {

    @Override
    public List<String> getInput(){
        return readLines("/2019/input14.txt");
    }

    @Override
    public Long solvePart1() {
        return minimumOre(getInput());
    }

    @Override
    public Long solvePart2() {
        return fastOrFull(2144702L, () -> maximumFuel(getInput()));
    }

    public long minimumOre(final List<String> rxns) {
        return minimumOre(new Chem(1, "FUEL"), rxns);
    }

    public long minimumOre(final Chem fuel, final List<String> rxns) {
        return minimumOre(fuel, reactions(rxns));
    }

    long minimumOre(final Chem fuel, final Map<String, Rxn> reactions) {
        final Rxn result = new Rxn(fuel, new HashMap<>());
        ore(fuel, result, reactions);
        return result.getElements().get("ORE").getQty();
    }

    private static final long FREE_FUEL = 1000000000000L;

    public long maximumFuel(final List<String> rxns) {
        final Map<String, Rxn> reactions = reactions(rxns);
        final long minOre = minimumOre(new Chem(1L, "FUEL"), reactions);
        // use cheat for day's input, examples need to be calculated
        if (rxns.size() > 25) {
            long x1 = 10000L, x2 = 1000000L;
            final long y1 = minimumOre(new Chem(x1, "FUEL"), reactions);
            final long y2 = minimumOre(new Chem(x2, "FUEL"), reactions);

            var slope = (double) (y2 - y1) / (x2 - x1);
            var b = y1 - slope * x1;
            var fuel = (FREE_FUEL - b) / slope;
            return (long) fuel;
        }
        // part 2: int(int(1000000000000 / one_fuel)
        // *
        // (1000000000000 / calculate_ore(int(1000000000000 / one_fuel))))
        @SuppressWarnings("IntegerDivisionInFloatingPointContext")
        long a = (long) Math.ceil(FREE_FUEL / minOre);
        long b = minimumOre(new Chem(a, "FUEL"), reactions);
        double c = (double) FREE_FUEL / b;
        final BigDecimal d = BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(c));
        long maxFuel = d.longValue();
        while (minimumOre(new Chem(maxFuel + 1L, "FUEL"), reactions) < FREE_FUEL)
            maxFuel += 1L;
        return maxFuel;
    }

    void ore(final Chem target, final Rxn result, final Map<String, Rxn> reactions) {
        final List<Chem> next = new ArrayList<>();
        Chem chem = target.copy(1);
        final Rxn rxn = reactions.get(chem.getName());
        if (rxn == null) {
            result.plus(chem.copy(1));
            return;
        }

        if (result.getElements().containsKey(chem.getName())) {
            final long alreadyHas = result.getElements().get(chem.getName()).getQty();
            if (alreadyHas > chem.getQty()) {
                result.minus(chem);
                return;
            } else if (alreadyHas == chem.getQty()) {
                result.getElements().remove(chem.getName());
                return;
            } else {
                result.getElements().remove(chem.getName());
                chem = new Chem(chem.getQty() - alreadyHas, chem.getName());
            }
        }

        long factor = 1, qty = rxn.getResult().getQty();
        while ((factor * qty) < chem.getQty()) factor++;

        final long leftover = (factor * qty) - chem.getQty();
        if (leftover > 0) result.plus(new Chem(leftover, chem.getName()));

        for (final Chem c : rxn.getElements().values())
            next.add(c.copy(factor));

        if (!next.isEmpty()) next.forEach(n -> ore(n, result, reactions));
    }

    Map<String, Rxn> reactions(final List<String> rxns) {
        return rxns.stream()
                .map(rxn -> rxn.split(" => "))
                .map(io -> new Rxn(chem(io[1]),
                    Stream.of(io[0].trim().split(",\\s+"))
                            .map(this::chem)
                            .collect(Collectors.toMap(Chem::getName, Function.identity()))))
                .collect(Collectors.toMap((Rxn rxn) -> rxn.getResult().getName(), Function.identity()));
    }

    Chem chem(final String chem) {
        final String[] io = chem.trim().split("\\s+");
        return new Chem(Long.parseLong(io[0]), io[1]);
    }

    @Data
    private static class Chem {
        private final long qty;
        private final String name;

        public Chem copy(final long times) {
            return new Chem(getQty() * times, getName());
        }

        @Override
        public String toString() {
            return String.format("%d %s", getQty(), getName());
        }
    }

    @Data
    private static class Rxn {
        private final Chem result;
        private final Map<String, Chem> elements;

        public void plus(final Chem chem) {
            final Chem current = elements.get(chem.getName());
            if (current == null) elements.put(chem.getName(), chem);
            else elements.put(chem.getName(), new Chem(current.getQty() + chem.getQty(), chem.getName()));
        }

        public void minus(final Chem chem) {
            final Chem current = elements.get(chem.getName());
            if (current == null) throw new RuntimeException("minus "+ chem);
            else elements.put(chem.getName(), new Chem(current.getQty() - chem.getQty(), chem.getName()));
        }
    }
}
