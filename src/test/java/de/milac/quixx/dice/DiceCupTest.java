package de.milac.quixx.dice;

import de.milac.quixx.Color;
import org.junit.jupiter.api.Test;

import static de.milac.quixx.Color.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiceCupTest {

	@Test
	void shakeNoColors() {
		DiceCup diceCup = DiceCup.shake();

		assertThat(diceCup.nrOfDice()).isEqualTo(2);
		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isFalse();
		assertThat(diceCup.hasColor(Color.YELLOW)).isFalse();
		assertThat(diceCup.hasColor(Color.GREEN)).isFalse();
		assertThat(diceCup.hasColor(Color.BLUE)).isFalse();
	}

	@Test
	void shakeWithWhite() {
		DiceCup diceCup = DiceCup.shake(WHITE);

		assertThat(diceCup.nrOfDice()).isEqualTo(2);
		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isFalse();
		assertThat(diceCup.hasColor(Color.YELLOW)).isFalse();
		assertThat(diceCup.hasColor(Color.GREEN)).isFalse();
		assertThat(diceCup.hasColor(Color.BLUE)).isFalse();
	}

	@Test
	void shakeWithColor() {
		DiceCup diceCup = DiceCup.shake(RED);

		assertThat(diceCup.nrOfDice()).isEqualTo(3);
		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isTrue();
		assertThat(diceCup.hasColor(Color.YELLOW)).isFalse();
		assertThat(diceCup.hasColor(Color.GREEN)).isFalse();
		assertThat(diceCup.hasColor(Color.BLUE)).isFalse();
	}

	@Test
	void sumOf() {
		DiceCup diceCup = DiceCup.shake(
			PredictableNumberGenerator.of(WHITE, 1,2).and(RED, 5).and(YELLOW, 3).build(),
			RED, YELLOW);

		assertThat(diceCup.sumOf(WHITE)).contains(3);
		assertThat(diceCup.sumOf(RED)).contains(6,7);
		assertThat(diceCup.sumOf(YELLOW)).contains(4,5);
		assertThatThrownBy(() -> diceCup.sumOf(GREEN)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> diceCup.sumOf(BLUE)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void hasColorNoColors() {
		DiceCup diceCup = DiceCup.shake();

		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isFalse();
		assertThat(diceCup.hasColor(YELLOW)).isFalse();
		assertThat(diceCup.hasColor(GREEN)).isFalse();
		assertThat(diceCup.hasColor(BLUE)).isFalse();
	}

	@Test
	void hasColorWithWhite() {
		DiceCup diceCup = DiceCup.shake(WHITE);

		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isFalse();
		assertThat(diceCup.hasColor(YELLOW)).isFalse();
		assertThat(diceCup.hasColor(GREEN)).isFalse();
		assertThat(diceCup.hasColor(BLUE)).isFalse();
	}

	@Test
	void hasColorWithColor() {
		DiceCup diceCup = DiceCup.shake(RED, BLUE);

		assertThat(diceCup.hasColor(WHITE)).isTrue();
		assertThat(diceCup.hasColor(RED)).isTrue();
		assertThat(diceCup.hasColor(YELLOW)).isFalse();
		assertThat(diceCup.hasColor(GREEN)).isFalse();
		assertThat(diceCup.hasColor(BLUE)).isTrue();
	}

	@Test
	void nrOfDiceNoColors() {
		DiceCup diceCup = DiceCup.shake();

		assertThat(diceCup.nrOfDice()).isEqualTo(2);
	}

	@Test
	void nrOfDiceWithWhite() {
		DiceCup diceCup = DiceCup.shake(WHITE);

		assertThat(diceCup.nrOfDice()).isEqualTo(2);
	}

	@Test
	void nrOfDiceWithColor() {
		DiceCup diceCup = DiceCup.shake(RED, YELLOW);

		assertThat(diceCup.nrOfDice()).isEqualTo(4);
	}
}