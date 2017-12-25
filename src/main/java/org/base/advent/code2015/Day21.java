package org.base.advent.code2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class Day21 {
	private static final int BOSS_HP = 109;
	private static final int BOSS_DAMAGE = 8;
	private static final int BOSS_ARMOR = 2;

	private int lowestCost = Integer.MAX_VALUE;
	private Outfit bestOutfit = null;
	private int highestCost = Integer.MIN_VALUE;
	private Outfit worstOutfit = null;

	public void solvePuzzle1() throws Exception {
		Map<String, List<Item>> itemMap = buildItems();
		for (Item weapon : itemMap.get("weapons")) {
			for (Item armor : itemMap.get("armor")) {
				// no rings
				evaluateOutfit(new Outfit(weapon, armor));

				List<Item> rings = itemMap.get("rings");
				List<Item> leftHand = new ArrayList<>(rings);
				// one rings
				for (Item lhRing : leftHand) {
					evaluateOutfit(new Outfit(weapon, armor, lhRing));

					// two rings
					List<Item> rightHand = new ArrayList<>(rings);
					for (Item rhRing : rightHand) {
						if (rhRing.cost == lhRing.cost) continue;
						evaluateOutfit(new Outfit(weapon, armor, lhRing, rhRing));
					}
				}
			}
		}

		System.out.println(String.format("Best outfit is %s with cost of %d", bestOutfit, lowestCost));
	}

	public void solvePuzzle2() throws Exception {
		System.out.println(String.format("Worst outfit is %s with cost of %d", worstOutfit, highestCost));
	}

	void evaluateOutfit(Outfit outfit) {
		int cost = outfit.getCost();
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

	boolean isWinningOutfit(Outfit outfit) {
		int myHp = 100, bossHp = BOSS_HP;
		int myNetDmg = outfit.getDamage() - BOSS_ARMOR;
		int bossNetDmg = BOSS_DAMAGE - outfit.getArmor();

		while (myHp > 0 && bossHp > 0) {
			bossHp -= myNetDmg;
			if (bossHp <= 0)
				return true;

			myHp -= bossNetDmg;
		}

		return false;
	}

	Map<String, List<Item>> buildItems() {
		Map<String, List<Item>> itemMap = new HashMap<>();
		List<Item> weapons = new ArrayList<>();
		weapons.add(new Item("Dagger",		8,  4, 0));
		weapons.add(new Item("Shortsword",	10, 5, 0));
		weapons.add(new Item("Warhammer",	25, 6, 0));
		weapons.add(new Item("Longsword",	40, 7, 0));
		weapons.add(new Item("Greataxe",	74, 8, 0));
		itemMap.put("weapons", weapons);
		List<Item> armor = new ArrayList<>();
		armor.add(new Item("Naked",			 0, 0, 0));
		armor.add(new Item("Leather",		13, 0, 1));
		armor.add(new Item("Chainmail",		31, 0, 2));
		armor.add(new Item("Splintmail",	53, 0, 3));
		armor.add(new Item("Bandedmail",	75, 0, 4));
		armor.add(new Item("Platemail",	   102, 0, 5));
		itemMap.put("armor", armor);
		List<Item> rings = new ArrayList<>();
		rings.add(new Item("Damage +1",		25, 1, 0));
		rings.add(new Item("Damage +2",		50, 2, 0));
		rings.add(new Item("Damage +3",	   100, 3, 0));
		rings.add(new Item("Defense +1",	20, 0, 1));
		rings.add(new Item("Defense +2",	40, 0, 2));
		rings.add(new Item("Defense +3",	80, 0, 3));
		itemMap.put("rings", rings);

		return itemMap;
	}

	private static class Outfit {
		private final List<Item>items = new ArrayList<>();

		public Outfit(Item w, Item a, Item... rs) {
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
		public Item(String n, int c, int d, int a) {
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
