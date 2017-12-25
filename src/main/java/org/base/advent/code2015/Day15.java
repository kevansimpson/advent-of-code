package org.base.advent.code2015;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.base.advent.Solution;


/**
 *
 */
public class Day15 implements Solution<List<String>> {

	@Override
	public List<String> getInput() throws IOException {
		return readLines("/2015/input15.txt");
	}

	@Override
	public Object solvePart1() throws Exception {
		return null;
	}

	@Override
	public Object solvePart2() throws Exception {
		return null;
	}


	private Map<String, Ingredient> ingredientMap;
	private Set<String> ingredients = new HashSet<>();
	private int highestScore = Integer.MIN_VALUE;

	public void solvePuzzle1() throws Exception {
		List<String> cookbook = getInput();
		this.ingredientMap = buildIngredientMap(cookbook);
		Recipe recipe = new Recipe();
		List<Ingredient> ingredientList = new ArrayList<>(this.ingredientMap.values());
		buildAllRecipes(ingredientList, 0, recipe, 100, -1);
		System.out.println("The highest cookie score is "+ this.highestScore);
	}

	public void solvePuzzle2() throws Exception {
		this.highestScore = Integer.MIN_VALUE;
		Recipe recipe = new Recipe();
		List<Ingredient> ingredientList = new ArrayList<>(this.ingredientMap.values());
		buildAllRecipes(ingredientList, 0, recipe, 100, 500);
		System.out.println("The highest score for the cookie with 500 calories is "+ this.highestScore);
	}

	void buildAllRecipes(List<Ingredient> ingredientList, int ingredientIndex, 
							Recipe recipe, int total, int calorieRequirement) {
//		System.out.println(String.format("Ingredient index = %d, total = %d", ingredientIndex, total));
		if (ingredientList.size() <= ingredientIndex) {	// recipe has all ingredients
//			System.out.println("Has all ingredients: "+ recipe.ingredientMap);
			if (recipe.sumTeaspoons() == 100) {			// recipe has =100 teaspoons
				if (calorieRequirement <= 0 ||
						(calorieRequirement > 0 && recipe.caloricCount(this.ingredientMap) == calorieRequirement)) {
					int score = recipe.calculateScore(this.ingredientMap);
					if (score > this.highestScore) {
						this.highestScore = score;
//						System.out.println(String.format("New high score: %d\nFrom Recipe: %s", score, recipe.ingredientMap));
					}
				}
			}

			return;
		}

		for (int i = 0; i <= total; i++) {
			Ingredient current = ingredientList.get(ingredientIndex);
			recipe.setTeaspoons(current.name, i);
			buildAllRecipes(ingredientList, ingredientIndex + 1, recipe, total - i, calorieRequirement);
		}
	}

	void buildAllPaths(List<String> available, List<String> permutation) {
		if (available.size() == 0) {
			System.out.println(permutation);
//			distanceMap.put(permutation, null);//calculateDistance(permutation));
			return;
		}

		for (int i = 0; i < available.size(); i++) {
			String loc = available.get(i);
			List<String> remaining = new ArrayList<>();
			remaining.addAll(available.subList(0, i));
			remaining.addAll(available.subList(i + 1, available.size()));
			
			List<String> newPerm = new ArrayList<>(permutation);
			newPerm.add(loc);
			buildAllPaths(remaining, newPerm);
		}
	}

	String key(String loc1, String loc2) {
		return "JUMP-"+ Arrays.asList(loc1, loc2).toString();
	}

	private static Pattern parser = Pattern.compile(
			"([^:]+):\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+),\\s[a-z]+\\s([-\\d]+)", Pattern.DOTALL);
	Map<String, Ingredient> buildIngredientMap(List<String> cookbook) {
		Map<String, Ingredient> ingredientMap = new HashMap<>();
		for (String ingredient : cookbook) {
			Matcher matcher = parser.matcher(ingredient);
			if (matcher.matches()) {
				String name = matcher.group(1);
				this.ingredients.add(name);
				ingredientMap.put(name, new Ingredient(name, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)),
						Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)), Integer.parseInt(matcher.group(6))));
			}
			else {
				System.err.println("No match: "+ ingredient);
			}
		}

		return ingredientMap;
	}

	void debug(Map<String, Ingredient> map) {
		for (String key : map.keySet()) {
			System.out.println(key +" ==> "+ map.get(key));
		}
	}
	void debugL(Map<List<String>, Integer> map) {
		for (List<String> key : map.keySet()) {
			System.out.println(key.toString() +" ==> "+ map.get(key));
		}
	}

	private enum Trait {
		capacity, durability, flavor, texture, calories;
	}

	private static class Recipe {
		private Map<String, Integer> ingredientMap = new HashMap<>();// # of tspn/ingredient

		public int caloricCount(Map<String, Ingredient> cookbook) {
			int count = 0;
			for (String iname : cookbook.keySet()) {
				Ingredient ingredient = cookbook.get(iname);
				count += (ingredientMap.get(iname)) * ingredient.getValue(Trait.calories);
			}
			if (count <= 0)
				return 0;

			return count;
		}

		public void setTeaspoons(String ingredient, int teaspoons) {
			ingredientMap.put(ingredient, teaspoons);
		}

		public int sumTeaspoons() {
			return ingredientMap.values().stream().mapToInt(Integer::intValue).sum();
		}

		public int calculateScore(Map<String, Ingredient> cookbook) {
			int score = 1;
			for (Trait trait : Trait.values()) {
				if (trait == Trait.calories) continue;

				int value = 0;
				for (String iname : cookbook.keySet()) {
					Ingredient ingredient = cookbook.get(iname);
//					System.out.println(ingredient.name +" = "+ ingredientMap.get(iname) +
//							" * "+ ingredient.getValue(trait));
					value += (ingredientMap.get(iname)) * ingredient.getValue(trait);
				}

				if (value <= 0)
					return 0;

				score *= value;
			}
			
			return score;
		}
	}

	private static class Ingredient {
		public final String name;
		public final Map<Trait, Integer> traits = new HashMap<>();
		public Ingredient(String nm, int cap, int d, int f, int t, int cal) {
			name = nm;
			traits.put(Trait.capacity, cap);
			traits.put(Trait.durability, d);
			traits.put(Trait.flavor, f);
			traits.put(Trait.texture, t);
			traits.put(Trait.calories, cal);
		}

		public int getValue(Trait trait) {
			return traits.get(trait);
		}

		@Override
		public boolean equals(Object obj) {
			return (obj instanceof Ingredient) && obj.hashCode() == this.hashCode();
		}

		@Override
		public int hashCode() {
			return toString().hashCode() * 7;
		}

		@Override
		public String toString() {
			return name + traits.toString();
		}
	}
}
