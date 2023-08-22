package org.base.advent.code2015;

import org.base.advent.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * <a href="https://adventofcode.com/2015/day/22">Day 22</a>
 */
public class Day22 implements Solution<Day22.Boss> {
    public record Boss(int hitPoints, int damage) {}

    @Override
    public Object solvePart1(Boss boss) {
        return fightBoss(boss, 0);
    }

    @Override
    public Object solvePart2(Boss boss) {
        return fightBoss(boss,1);
    }

    int fightBoss(final Boss boss, final int extraDamage) {
        final PriorityQueue<Wizard> wizards = new PriorityQueue<>(
                (w1, w2) -> Integer.compare(w2.totalManaSpent, w1.totalManaSpent));
        wizards.add(new Wizard(50, 500, boss.hitPoints));

        final int[] lowestManaSpent = new int[] { Integer.MAX_VALUE };
        while (!wizards.isEmpty()) {
            final Wizard wiz = wizards.poll();
            wiz.hitPoints -= extraDamage;
            if (wiz.hitPoints <= 0)
                continue;
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
                        copy.hitPoints -= Math.max(1, boss.damage - copy.armor);
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

        public void applyEffects() {
            if (!activeEffects.containsKey(Spell.Shield) || activeEffects.get(Spell.Shield) == 0)
                armor = 0;

            for (final Spell spell : activeEffects.keySet()) {
                final int duration = getDuration(spell);
                if (duration > 0) {
                    activeEffects.put(spell, duration - 1);
                    switch (spell) {
                        case Shield -> armor = 7;
                        case Poison -> bossHP -= 3;
                        case Recharge -> mana += 101;
                        default -> {}
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
                case MagicMissile -> bossHP -= 4;
                case Drain -> {
                    bossHP -= 2;
                    hitPoints += 2;
                }
                default -> activeEffects.put(spell, spell.effectLasts);
            }
        }
    }

    public enum Spell {
        MagicMissile(53, 0),    // 4 dmg
        Drain(73, 0),           // 2 dmg + 2 HP
        Shield(113, 6),         // +7 armor / turn
        Poison(173, 6),         // 3 dmg / turn
        Recharge(229, 5);       // +101 mana / turn
        
        public final int manaCost, effectLasts;

        Spell(final int mc, final int el) {
            manaCost = mc;
            effectLasts = el;
        }
    }
}
