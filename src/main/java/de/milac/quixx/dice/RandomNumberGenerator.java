package de.milac.quixx.dice;

import de.milac.quixx.Color;

import java.util.random.RandomGenerator;

public class RandomNumberGenerator implements NumberGenerator {
	private final int origin;
	private final int bound;

	public RandomNumberGenerator(int origin, int bound) {
		this.origin = origin;
		this.bound = bound;
	}

	@Override
	public int nextNumber(Color color) {
		return RandomGenerator.getDefault().nextInt(origin, bound);
	}
}
