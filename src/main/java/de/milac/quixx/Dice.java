package de.milac.quixx;

import java.util.random.RandomGenerator;

public class Dice {
	private final Color color;
	private final int value = RandomGenerator.getDefault().nextInt(1, 7);

	public Dice(Color color) {
		this.color = color;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("[%s-%d]", color, value);
	}

	public Color getColor() {
		return color;
	}
}
