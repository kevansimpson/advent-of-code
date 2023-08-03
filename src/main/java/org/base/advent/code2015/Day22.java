package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <a href="https://adventofcode.com/2015/day/22">Day 22</a>
 */
public class Day22 implements Solution<int[]> {
    private static final int BOSS_HP = 71;
    private static final int BOSS_DAMAGE = 10;

    @Override
    public int[] getInput(){
        return new int[] { 71, 10 };    // Boss HP, Dmg
    }

    @Override
    public Object solvePart1() {
        return regularDifficulty();
    }

    @Override
    public Object solvePart2() {
        return hardDifficulty();
    }

    public int regularDifficulty() {
        final PriorityQueue<Wizard> wizards = new PriorityQueue<>(
                (w1, w2) -> Integer.compare(w2.totalManaSpent, w1.totalManaSpent));
        wizards.add(new Wizard(50, 500, BOSS_HP));
        return fightBoss(wizards, false);
    }

    public int hardDifficulty() {
        final PriorityQueue<Wizard> wizards = new PriorityQueue<>(
                (w1, w2) -> Integer.compare(w2.totalManaSpent, w1.totalManaSpent));
        wizards.add(new Wizard(50, 500, BOSS_HP));
        return fightBoss(wizards, true);
    }

    /**
     * For example, suppose the player has 10 hit points and 250 mana, and that the boss has 13 hit points and 8 damage:
    <pre>
     -- Player turn --
     - Player has 10 hit points, 0 armor, 250 mana
     - Boss has 13 hit points
     Player casts Poison.

     -- Boss turn --
     - Player has 10 hit points, 0 armor, 77 mana
     - Boss has 13 hit points
     Poison deals 3 damage; its timer is now 5.
     Boss attacks for 8 damage.

     -- Player turn --
     - Player has 2 hit points, 0 armor, 77 mana
     - Boss has 10 hit points
     Poison deals 3 damage; its timer is now 4.
     Player casts Magic Missile, dealing 4 damage.

     -- Boss turn --
     - Player has 2 hit points, 0 armor, 24 mana
     - Boss has 3 hit points
     Poison deals 3 damage. This kills the boss, and the player wins.
     Now, suppose the same initial conditions, except that the boss has 14 hit points instead:

     -- Player turn --
     - Player has 10 hit points, 0 armor, 250 mana
     - Boss has 14 hit points
     Player casts Recharge.

     -- Boss turn --
     - Player has 10 hit points, 0 armor, 21 mana
     - Boss has 14 hit points
     Recharge provides 101 mana; its timer is now 4.
     Boss attacks for 8 damage!

     -- Player turn --
     - Player has 2 hit points, 0 armor, 122 mana
     - Boss has 14 hit points
     Recharge provides 101 mana; its timer is now 3.
     Player casts Shield, increasing armor by 7.

     -- Boss turn --
     - Player has 2 hit points, 7 armor, 110 mana
     - Boss has 14 hit points
     Shield's timer is now 5.
     Recharge provides 101 mana; its timer is now 2.
     Boss attacks for 8 - 7 = 1 damage!

     -- Player turn --
     - Player has 1 hit point, 7 armor, 211 mana
     - Boss has 14 hit points
     Shield's timer is now 4.
     Recharge provides 101 mana; its timer is now 1.
     Player casts Drain, dealing 2 damage, and healing 2 hit points.

     -- Boss turn --
     - Player has 3 hit points, 7 armor, 239 mana
     - Boss has 12 hit points
     Shield's timer is now 3.
     Recharge provides 101 mana; its timer is now 0.
     Recharge wears off.
     Boss attacks for 8 - 7 = 1 damage!

     -- Player turn --
     - Player has 2 hit points, 7 armor, 340 mana
     - Boss has 12 hit points
     Shield's timer is now 2.
     Player casts Poison.

     -- Boss turn --
     - Player has 2 hit points, 7 armor, 167 mana
     - Boss has 12 hit points
     Shield's timer is now 1.
     Poison deals 3 damage; its timer is now 5.
     Boss attacks for 8 - 7 = 1 damage!

     -- Player turn --
     - Player has 1 hit point, 7 armor, 167 mana
     - Boss has 9 hit points
     Shield's timer is now 0.
     Shield wears off, decreasing armor by 7.
     Poison deals 3 damage; its timer is now 4.
     Player casts Magic Missile, dealing 4 damage.

     -- Boss turn --
     - Player has 1 hit point, 0 armor, 114 mana
     - Boss has 2 hit points
     Poison deals 3 damage. This kills the boss, and the player wins.
     </pre>
     *
     */
    protected int fightBoss(final PriorityQueue<Wizard> wizards, final boolean hard) {
        final int[] lowestManaSpent = new int[] { Integer.MAX_VALUE };
        while (!wizards.isEmpty()) {
            final Wizard wiz = wizards.poll();
            if (hard) {
                wiz.hitPoints -= 1;
                if (wiz.hitPoints <= 0)
                    continue;
            }
            wiz.applyEffects();
            for (final Spell spell : Spell.values()) {
                if (wiz.canCast(spell)) {
                    final Wizard copy = new Wizard(wiz);
                    copy.cast(spell);
                    copy.applyEffects();

                    if (copy.bossHP <= 0) {
                        if (copy.totalManaSpent < lowestManaSpent[0]) {
                            lowestManaSpent[0] = copy.totalManaSpent;
                            wizards.removeIf(w -> w.totalManaSpent > lowestManaSpent[0]);
                        }
                    }
                    else {
                        copy.hitPoints -= Math.max(1, BOSS_DAMAGE - copy.armor);
                        if (copy.hitPoints > 0 && copy.mana > 0 && copy.totalManaSpent < lowestManaSpent[0])
                            wizards.add(copy);
                    }
                }
            }
        }

        return lowestManaSpent[0];
    }

    private static class Wizard {
        public int hitPoints, mana, armor, totalManaSpent;
        public Map<Spell, Integer> activeEffects = new HashMap<>();
        public int bossHP;
        
        public Wizard(final int hp, final int m, final int b) {
            hitPoints = hp;
            mana = m;
            bossHP = b;
        }
        public Wizard(final Wizard copy) {
            hitPoints = copy.hitPoints;
            mana = copy.mana;
            armor = copy.armor;
            totalManaSpent = copy.totalManaSpent;
            activeEffects = new HashMap<>(copy.activeEffects);
            bossHP = copy.bossHP;
        }

        public int getDuration(final Spell spell) {
            return activeEffects.computeIfAbsent(spell, k -> 0);
        }

        @SuppressWarnings("incomplete-switch")
        public void applyEffects() {
            if (!activeEffects.containsKey(Spell.Shield) || activeEffects.get(Spell.Shield) == 0)
                armor = 0;

            for (final Spell spell : activeEffects.keySet()) {
                final int duration = getDuration(spell);
                if (duration > 0) {
                    activeEffects.put(spell, duration - 1);
                    switch (spell) {
                        case Shield:
                            armor = 7;
                            break;
                        case Poison:
                            bossHP -= 3;
                            break;
                        case Recharge:
                            mana += 101;
                            break;
                    }
                }
                        
            }
        }

        public boolean canCast(final Spell spell) {
            return mana >= spell.manaCost && (spell.ordinal() < 2 || getDuration(spell) == 0);
        }

        public void cast(final Spell spell) {
            mana -= spell.manaCost;
            totalManaSpent += spell.manaCost;

            switch (spell) {
                case MagicMissile:
                    bossHP -= 4;
                    break;
                case Drain:
                    bossHP -= 2;
                    hitPoints += 2;
                    break;
                default:
                    activeEffects.put(spell, spell.effectLasts);
                    break;
            }
        }
    }

    public enum Spell {
        MagicMissile(53, 0),    // 4 dmg
        Drain(73, 0),            // 2 dmg + 2 HP
        Shield(113, 6),            // +7 armor / turn
        Poison(173, 6),            // 3 dmg / turn
        Recharge(229, 5);        // +101 mana / turn
        
        public final int manaCost, effectLasts;

        Spell(final int mc, final int el) {
            manaCost = mc;
            effectLasts = el;
        }
    }
}
