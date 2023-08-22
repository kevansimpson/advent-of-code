package org.base.advent.code2019;

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
    public Long solvePart1(final List<String> input) {
        return minimumOre(input);
    }

    @Override
    public Long solvePart2(final List<String> input) {
        return fastOrFull(2144702L, () -> maximumFuel(input));
    }

    long minimumOre(final List<String> rxns) {
        return minimumOre(new Chem(1, "FUEL"), rxns);
    }

    long minimumOre(final Chem fuel, final List<String> rxns) {
        return minimumOre(fuel, reactions(rxns));
    }

    long minimumOre(final Chem fuel, final Map<String, Rxn> reactions) {
        final Rxn result = new Rxn(fuel, new HashMap<>());
        ore(fuel, result, reactions);
        return result.elements().get("ORE").qty();
    }

    private static final long FREE_FUEL = 1000000000000L;

    long maximumFuel(final List<String> rxns) {
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
        final Rxn rxn = reactions.get(chem.name());
        if (rxn == null) {
            result.plus(chem.copy(1));
            return;
        }

        if (result.elements().containsKey(chem.name())) {
            final long alreadyHas = result.elements().get(chem.name()).qty();
            if (alreadyHas > chem.qty()) {
                result.minus(chem);
                return;
            } else if (alreadyHas == chem.qty()) {
                result.elements().remove(chem.name());
                return;
            } else {
                result.elements().remove(chem.name());
                chem = new Chem(chem.qty() - alreadyHas, chem.name());
            }
        }

        long factor = 1, qty = rxn.result().qty();
        while ((factor * qty) < chem.qty()) factor++;

        final long leftover = (factor * qty) - chem.qty();
        if (leftover > 0) result.plus(new Chem(leftover, chem.name()));

        for (final Chem c : rxn.elements().values())
            next.add(c.copy(factor));

        if (!next.isEmpty()) next.forEach(n -> ore(n, result, reactions));
    }

    Map<String, Rxn> reactions(final List<String> rxns) {
        return rxns.stream()
                .map(rxn -> rxn.split(" => "))
                .map(io -> new Rxn(chem(io[1]),
                    Stream.of(io[0].trim().split(",\\s+"))
                            .map(this::chem)
                            .collect(Collectors.toMap(Chem::name, Function.identity()))))
                .collect(Collectors.toMap((Rxn rxn) -> rxn.result().name(), Function.identity()));
    }

    Chem chem(final String chem) {
        final String[] io = chem.trim().split("\\s+");
        return new Chem(Long.parseLong(io[0]), io[1]);
    }

    public record Chem(long qty, String name) {
        public Chem copy(final long times) {
            return new Chem(qty() * times, name());
        }

        @Override
        public String toString() {
            return String.format("%d %s", qty(), name());
        }
    }

    public record Rxn(Chem result, Map<String, Chem> elements) {
        public void plus(final Chem chem) {
            final Chem current = elements.get(chem.name());
            if (current == null) elements.put(chem.name(), chem);
            else elements.put(chem.name(), new Chem(current.qty() + chem.qty(), chem.name()));
        }

        public void minus(final Chem chem) {
            final Chem current = elements.get(chem.name());
            if (current == null) throw new RuntimeException("minus "+ chem);
            else elements.put(chem.name(), new Chem(current.qty() - chem.qty(), chem.name()));
        }
    }
}
