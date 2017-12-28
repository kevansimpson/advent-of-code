package org.base.advent.code2015;

import org.base.advent.Solution;

import java.io.IOException;
import java.util.*;

/**
 * <h2>Part 1</h2>
 * Little Henry Case got a new video game for Christmas. It's an RPG, and he's stuck on a boss. He needs to know what
 * equipment to buy at the shop. He hands you the controller.
 *
 * In this game, the player (you) and the enemy (the boss) take turns attacking. The player always goes first. Each
 * attack reduces the opponent's hit points by at least 1. The first character at or below 0 hit points loses.
 *
 * Damage dealt by an attacker each turn is equal to the attacker's damage score minus the defender's armor score.
 * An attacker always does at least 1 damage. So, if the attacker has a damage score of 8, and the defender has an
 * armor score of 3, the defender loses 5 hit points. If the defender had an armor score of 300, the defender would
 * still lose 1 hit point.
 *
 * Your damage score and armor score both start at zero. They can be increased by buying items in exchange for gold.
 * You start with no items and have as much gold as you need. Your total damage or armor is equal to the sum of those
 * stats from all of your items. You have 100 hit points.
 *
 * @see #buildItems()
 *
 * You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one. You can
 * buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one of each item,
 * so you can't buy, for example, two rings of Damage +3.
 *
 * You have 100 hit points. The boss's actual stats are in your puzzle input. What is the least amount of gold you
 * can spend and still win the fight?
 *
 * <h2>Part 2</h2>
 * Turns out the shopkeeper is working with the boss, and can persuade you to buy whatever items he wants. The other
 * rules still apply, and he still only has one of each item.
 */
public class Day21 implements Solution<Void> {

    private static final int BOSS_HP = 109;
    private static final int BOSS_DAMAGE = 8;
    private static final int BOSS_ARMOR = 2;

    private int lowestCost = Integer.MAX_VALUE;
    private Outfit bestOutfit;
    private int highestCost = Integer.MIN_VALUE;
    private Outfit worstOutfit;

    @Override
    public Void getInput() throws IOException {
        return null;
    }

    @Override
    public Object solvePart1() throws Exception {
        determineOutfits();
        return lowestCost;
    }

    @Override
    public Object solvePart2() throws Exception {
        if (worstOutfit == null)
            determineOutfits();
        return highestCost;
    }

    public void determineOutfits() {
        final Map<String, List<Item>> itemMap = buildItems();
        for (final Item weapon : itemMap.get("weapons")) {
            for (final Item armor : itemMap.get("armor")) {
                // no rings
                evaluateOutfit(new Outfit(weapon, armor));

                final List<Item> rings = itemMap.get("rings");
                final List<Item> leftHand = new ArrayList<>(rings);
                // one rings
                for (final Item lhRing : leftHand) {
                    evaluateOutfit(new Outfit(weapon, armor, lhRing));

                    // two rings
                    final List<Item> rightHand = new ArrayList<>(rings);
                    for (final Item rhRing : rightHand) {
                        if (rhRing.cost == lhRing.cost) continue;
                        evaluateOutfit(new Outfit(weapon, armor, lhRing, rhRing));
                    }
                }
            }
        }

        debug("Best outfit is %s with cost of %d", bestOutfit, lowestCost);
        debug("Worst outfit is %s with cost of %d", worstOutfit, highestCost);
    }

    protected void evaluateOutfit(final Outfit outfit) {
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
    protected boolean isWinningOutfit(final Outfit outfit) {
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

     * @return map of item lists
     */
    protected Map<String, List<Item>> buildItems() {
        final Map<String, List<Item>> itemMap = new HashMap<>();
        final List<Item> weapons = new ArrayList<>();
        weapons.add(new Item("Dagger",      8,  4, 0));
        weapons.add(new Item("Shortsword",  10, 5, 0));
        weapons.add(new Item("Warhammer",   25, 6, 0));
        weapons.add(new Item("Longsword",   40, 7, 0));
        weapons.add(new Item("Greataxe",    74, 8, 0));
        itemMap.put("weapons", weapons);
        final List<Item> armor = new ArrayList<>();
        armor.add(new Item("Naked",          0, 0, 0));
        armor.add(new Item("Leather",       13, 0, 1));
        armor.add(new Item("Chainmail",     31, 0, 2));
        armor.add(new Item("Splintmail",    53, 0, 3));
        armor.add(new Item("Bandedmail",    75, 0, 4));
        armor.add(new Item("Platemail",    102, 0, 5));
        itemMap.put("armor", armor);
        final List<Item> rings = new ArrayList<>();
        rings.add(new Item("Damage +1",     25, 1, 0));
        rings.add(new Item("Damage +2",     50, 2, 0));
        rings.add(new Item("Damage +3",    100, 3, 0));
        rings.add(new Item("Defense +1",    20, 0, 1));
        rings.add(new Item("Defense +2",    40, 0, 2));
        rings.add(new Item("Defense +3",    80, 0, 3));
        itemMap.put("rings", rings);

        return itemMap;
    }

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
