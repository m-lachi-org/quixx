package de.milac.quixx.dice;

import de.milac.quixx.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.milac.quixx.Color.*;

public class DiceCup {
	private final List<Dice> dice = new ArrayList<>();

	DiceCup(Color... colors) {
		this(Dice.DEFAULT_NUMBER_GENERATOR, colors);
	}

	DiceCup(NumberGenerator generator, Color... colors) {
		dice.add(new Dice(WHITE, generator));
		dice.add(new Dice(WHITE, generator));
		for (Color color : colors) {
			if (!WHITE.equals(color)) {
				dice.add(new Dice(color, generator));
			}
		}
	}

	public static DiceCup shake(Color... colors) {
		return new DiceCup(colors);
	}

	public static DiceCup shake(NumberGenerator generator, Color... colors) {
		return new DiceCup(generator, colors);
	}

	public int[] sumOf(Color color) {
		List<Dice> whiteDice = dice.stream().filter(d -> d.getColor().equals(WHITE)).toList();
		if (WHITE.equals(color)) {
			return new int[] { whiteDice.stream().map(Dice::getValue).reduce(0, Integer::sum) };
		} else {
			Dice coloredDice = dice.stream().filter(d -> d.getColor().equals(color)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException(String.format("No dice of color %s available in cup: %s", color, this)));
			return new int[] {
				Stream.of(whiteDice.get(0), coloredDice).map(Dice::getValue).reduce(0, Integer::sum),
				Stream.of(whiteDice.get(1), coloredDice).map(Dice::getValue).reduce(0, Integer::sum)
			};
		}
	}

	@Override
	public String toString() {
		return dice.toString();
	}

	public boolean hasColor(Color color) {
		return dice.stream().anyMatch(d -> color.equals(d.getColor()));
	}

	public int nrOfDice() {
		return dice.size();
	}
}
