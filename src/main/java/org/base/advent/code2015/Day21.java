package org.base.advent.code2015;

import org.base.advent.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * <a href="https://adventofcode.com/2015/day/21">Day 21</a>
 */
public class Day21 implements Function<Void, Day21.OutfitCosts>, Helpers {

    private static final int BOSS_HP = 109;
    private static final int BOSS_DAMAGE = 8;
    private static final int BOSS_ARMOR = 2;

    private int lowestCost = Integer.MAX_VALUE;
    private Outfit bestOutfit;
    private int highestCost = Integer.MIN_VALUE;
    private Outfit worstOutfit;

    public record OutfitCosts(int lowest, int highest) {}

    @Override
    public OutfitCosts apply(Void unused) {
        for (final Item weapon : itemMap.get("weapons")) {
            for (final Item armor : itemMap.get("armor")) {
                // no rings
                evaluateOutfit(new Outfit(weapon, armor));

                final List<Item> rings = itemMap.get("rings");
                // one rings
                for (final Item lhRing : rings) {
                    evaluateOutfit(new Outfit(weapon, armor, lhRing));

                    // two rings
                    for (final Item rhRing : rings) {
                        if (rhRing.cost == lhRing.cost) continue;
                        evaluateOutfit(new Outfit(weapon, armor, lhRing, rhRing));
                    }
                }
            }
        }

        debug("Best outfit is %s with cost of %d", bestOutfit, lowestCost);
        debug("Worst outfit is %s with cost of %d", worstOutfit, highestCost);
        return new OutfitCosts(lowestCost, highestCost);
    }

    void evaluateOutfit(final Outfit outfit) {
        final int cost = outfit.getCost();
        if (isWinningOutfit(outfit)) {
            if (cost < lowestCost) {
                bestOutfit = outfit;
                lowestCost = cost;
            }
        }
        else {
            if (cost > highestCost) {
                worstOutfit = outfit;
                highestCost = cost;
            }
        }
    }

    /**
     *  For example, suppose you have 8 hit points, 5 damage, and 5 armor, and that
     *  the boss has 12 hit points, 7 damage, and 2 armor:

     The player deals 5-2 = 3 damage; the boss goes down to 9 hit points.
     The boss deals 7-5 = 2 damage; the player goes down to 6 hit points.
     The player deals 5-2 = 3 damage; the boss goes down to 6 hit points.
     The boss deals 7-5 = 2 damage; the player goes down to 4 hit points.
     The player deals 5-2 = 3 damage; the boss goes down to 3 hit points.
     The boss deals 7-5 = 2 damage; the player goes down to 2 hit points.
     The player deals 5-2 = 3 damage; the boss goes down to 0 hit points.
     In this scenario, the player wins! (Barely.)

     * @param outfit The {@link Outfit} to evaluate.
     * @return {@code true} if the {@code Outfit} is winning.
     */
    boolean isWinningOutfit(final Outfit outfit) {
        int myHp = 100, bossHp = BOSS_HP;
        final int myNetDmg = outfit.getDamage() - BOSS_ARMOR;
        final int bossNetDmg = BOSS_DAMAGE - outfit.getArmor();

        while (myHp > 0) {
            bossHp -= myNetDmg;
            if (bossHp <= 0)
                return true;

            myHp -= bossNetDmg;
        }

        return false;
    }

    /**
     *  Here is what the item shop is selling:

     Weapons:    Cost  Damage  Armor
     Dagger        8     4       0
     Shortsword   10     5       0
     Warhammer    25     6       0
     Longsword    40     7       0
     Greataxe     74     8       0

     Armor:      Cost  Damage  Armor
     Leather      13     0       1
     Chainmail    31     0       2
     Splintmail   53     0       3
     Bandedmail   75     0       4
     Platemail   102     0       5

     Rings:      Cost  Damage  Armor
     Damage +1    25     1       0
     Damage +2    50     2       0
     Damage +3   100     3       0
     Defense +1   20     0       1
     Defense +2   40     0       2
     Defense +3   80     0       3

     * the map of item lists
     */
    final Map<String, List<Item>> itemMap = Map.of(
            "weapons", List.of(
                    new Item("Dagger",      8,  4, 0),
                    new Item("Shortsword",  10, 5, 0),
                    new Item("Warhammer",   25, 6, 0),
                    new Item("Longsword",   40, 7, 0),
                    new Item("Greataxe",    74, 8, 0)),
            "armor", List.of(
                    new Item("Naked",          0, 0, 0),
                    new Item("Leather",       13, 0, 1),
                    new Item("Chainmail",     31, 0, 2),
                    new Item("Splintmail",    53, 0, 3),
                    new Item("Bandedmail",    75, 0, 4),
                    new Item("Platemail",    102, 0, 5)),
            "rings", List.of(
                    new Item("Damage +1",     25, 1, 0),
                    new Item("Damage +2",     50, 2, 0),
                    new Item("Damage +3",    100, 3, 0),
                    new Item("Defense +1",    20, 0, 1),
                    new Item("Defense +2",    40, 0, 2),
                    new Item("Defense +3",    80, 0, 3)));

    private static class Outfit {
        private final List<Item>items = new ArrayList<>();

        public Outfit(final Item w, final Item a, final Item... rs) {
            items.add(w);
            items.add(a);
            items.addAll(Arrays.asList(rs));
        }

        public int getArmor() {
            return items.stream().mapToInt(i -> i.armor).sum();
        }
        public int getCost() {
            return items.stream().mapToInt(i -> i.cost).sum();
        }
        public int getDamage() {
            return items.stream().mapToInt(i -> i.damage).sum();
        }

        @Override
        public String toString() {
            return items.toString();
        }
    }

    private static class Item {
        public final String name;
        public final int cost, damage, armor;
        public Item(final String n, final int c, final int d, final int a) {
            name = n;
            cost = c;
            damage = d;
            armor = a;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
