package de.milac.quixx.dice;

import de.milac.quixx.Color;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DiceTest {

	@Test
	void getValue() {
		Map<Integer, Integer> count = new HashMap<>();
		for (int i = 0; i < 100; i++) {
			Dice dice = new Dice(Color.RED);
			count.put(dice.getValue(), count.getOrDefault(dice.getValue(), 0)+1);
			assertThat(dice.getValue()).isBetween(1, 6);
		}
		assertThat(count).hasSize(6);
		System.out.println(count);
	}
}