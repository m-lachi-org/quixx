package de.milac.quixx.dice;

import de.milac.quixx.Color;

public class Dice {
	private final Color color;
	private static final NumberGenerator DEFAULT_NUMBER_GENERATOR = new RandomNumberGenerator(1,7);
	private final int value;

	Dice(Color color) {
		this(color, DEFAULT_NUMBER_GENERATOR);
	}

	Dice(Color color, NumberGenerator generator) {
		this.color = color;
		this.value = generator.nextNumber(color);
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s (%d)", color.dyedName(), value);
	}

	public Color getColor() {
		return color;
	}
}
