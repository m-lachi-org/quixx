package de.milac.quixx;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static de.milac.quixx.Color.*;

public class DiceCup {
	private final List<Dice> dices = new ArrayList<>();

	DiceCup(Color... colors) {
		dices.add(new Dice(WHITE));
		dices.add(new Dice(WHITE));
		for (Color color : colors) {
			dices.add(new Dice(color));
		}
	}

	public static DiceCup shake(Color... colors) {
		return new DiceCup(colors);
	}

	public int[] sumOf(Color color) {
		List<Dice> whiteDices = dices.stream().filter(d -> d.getColor().equals(WHITE)).toList();
		if (WHITE.equals(color)) {
			return new int[] { whiteDices.stream().map(Dice::getValue).reduce(0, Integer::sum) };
		} else {
			Dice coloredDice = dices.stream().filter(d -> d.getColor().equals(color)).findFirst().orElseThrow();
			return new int[] {
				Stream.of(whiteDices.get(0), coloredDice).map(Dice::getValue).reduce(0, Integer::sum),
				Stream.of(whiteDices.get(1), coloredDice).map(Dice::getValue).reduce(0, Integer::sum)
			};
		}
	}

	@Override
	public String toString() {
		return dices.toString();
	}

	public boolean hasColor(Color color) {
		return dices.stream().anyMatch(d -> color.equals(d.getColor()));
	}
}
