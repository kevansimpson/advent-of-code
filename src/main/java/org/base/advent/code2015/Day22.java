package org.base.advent.code2015;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 */
public class Day22 {
	private static final int BOSS_HP = 71;
	private static final int BOSS_DAMAGE = 10;

	int lowestManaSpent = Integer.MAX_VALUE;

	public void solvePuzzle1() throws Exception {
		PriorityQueue<Wizard> wizards = new PriorityQueue<Wizard>(
				(w1, w2) -> Integer.compare(w2.totalManaSpent, w1.totalManaSpent));
		wizards.add(new Wizard(50, 500, BOSS_HP));
		fightBoss(wizards, false);
		// > 767
		System.out.println("Least amount of mana spent to win is "+ lowestManaSpent);
	}

	public void solvePuzzle2() throws Exception {
		lowestManaSpent = Integer.MAX_VALUE;
		PriorityQueue<Wizard> wizards = new PriorityQueue<Wizard>(
				(w1, w2) -> Integer.compare(w2.totalManaSpent, w1.totalManaSpent));
		wizards.add(new Wizard(50, 500, BOSS_HP));
		fightBoss(wizards, true);
		System.out.println("Least amount of mana spent to win on hard difficulty is "+ lowestManaSpent);
	}

	void fightBoss(PriorityQueue<Wizard> wizards, boolean hard) {
		while (wizards.size() > 0) {
			Wizard wiz = wizards.poll();
			if (hard) {
				wiz.hitPoints -= 1;
				if (wiz.hitPoints <= 0)
					continue;
			}
			wiz.applyEffects();
			for (Spell spell : Spell.values()) {
				if (wiz.canCast(spell)) {
					Wizard copy = new Wizard(wiz);
					copy.cast(spell);
					copy.applyEffects();

					if (copy.bossHP <= 0) {
						if (copy.totalManaSpent < lowestManaSpent) {
							lowestManaSpent = copy.totalManaSpent;
							wizards.removeIf(w -> w.totalManaSpent > lowestManaSpent);
						}
					}
					else {
						copy.hitPoints -= Math.max(1, BOSS_DAMAGE - copy.armor);
						if (copy.hitPoints > 0 && copy.mana > 0 && copy.totalManaSpent < lowestManaSpent)
							wizards.add(copy);
					}
				}
			}
		}
	}

	private static class Wizard {
		public int hitPoints, mana = 0, armor = 0, totalManaSpent = 0;
		public Map<Spell, Integer> activeEffects = new HashMap<>();
		public int bossHP;
		
		public Wizard(int hp, int m, int b) {
			hitPoints = hp;
			mana = m;
			bossHP = b;
		}
		public Wizard(Wizard copy) {
			hitPoints = copy.hitPoints;
			mana = copy.mana;
			armor = copy.armor;
			totalManaSpent = copy.totalManaSpent;
			activeEffects = new HashMap<>(copy.activeEffects);
			bossHP = copy.bossHP;
		}

		public int getDuration(Spell spell) {
			Integer dur = activeEffects.get(spell);
			if (dur == null)
				activeEffects.put(spell, (dur = Integer.valueOf(0)));
			
			return dur.intValue();
		}

		@SuppressWarnings("incomplete-switch")
		public void applyEffects() {
			if (!activeEffects.containsKey(Spell.Shield))
				armor = 0;
			else if (activeEffects.get(Spell.Shield).intValue() == 0)
				armor = 0;

			for (Spell spell : activeEffects.keySet()) {
				int duration = getDuration(spell);
				if (duration > 0) {
					activeEffects.put(spell, Integer.valueOf(duration - 1));
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

		public boolean canCast(Spell spell) {
			return mana >= spell.manaCost && (spell.ordinal() < 2 || getDuration(spell) == 0);
		}

		public void cast(Spell spell) {
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
					activeEffects.put(spell, Integer.valueOf(spell.effectLasts));
					break;
			}
		}
	}

	public enum Spell {
		MagicMissile(53, 0),	// 4 dmg
		Drain(73, 0),			// 2 dmg + 2 HP
		Shield(113, 6),			// +7 armor / turn
		Poison(173, 6),			// 3 dmg / turn
		Recharge(229, 5);		// +101 mana / turn
		
		public final int manaCost, effectLasts;

		Spell(int mc, int el) {
			manaCost = mc;
			effectLasts = el;
		}
	}
}
