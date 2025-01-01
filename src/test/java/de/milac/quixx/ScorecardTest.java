package de.milac.quixx;

import de.milac.quixx.dice.DiceCup;
import de.milac.quixx.dice.PredictableNumberGenerator;
import de.milac.quixx.strategy.SimpleStrategy;
import org.junit.jupiter.api.Test;

import static de.milac.quixx.Color.*;
import static org.assertj.core.api.Assertions.assertThat;

class ScorecardTest {

	@Test
	void matchOnTurn() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());

		scorecard.matchOnTurn(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 1,2)
				.and(RED, 3).and(YELLOW, 4).and(GREEN, 5).and(BLUE, 6).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(0);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(2);
		assertThat(scorecard.getRow(RED).getCellOfValue(3).isChecked()).isTrue();
		assertThat(scorecard.getRow(RED).getCellOfValue(4).isChecked()).isTrue();
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(0);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(0);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(0);
	}

	@Test
	void matchOnTurnNoMatchWhite() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		scorecard.check(scorecard.getRow(RED).getCellOfValue(3));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(3));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(3));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(3));

		scorecard.matchOnTurn(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 1,2)
				.and(RED, 3).and(YELLOW, 4).and(GREEN, 5).and(BLUE, 6).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(0);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(2);
		assertThat(scorecard.getRow(RED).getCellOfValue(4).isChecked()).isTrue();
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(1);
	}

	@Test
	void matchOnTurnNoMatchColored() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		scorecard.check(scorecard.getRow(RED).getCellOfValue(7));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(7));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(4));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(4));

		scorecard.matchOnTurn(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 4,4)
				.and(RED, 1).and(YELLOW, 1).and(GREEN, 1).and(BLUE, 1).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(0);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(2);
		assertThat(scorecard.getRow(RED).getCellOfValue(8).isChecked()).isTrue();
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(1);
	}

	@Test
	void matchOnTurnNoMatch() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		scorecard.check(scorecard.getRow(RED).getCellOfValue(8));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(8));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(4));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(4));

		scorecard.matchOnTurn(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 4,4)
				.and(RED, 1).and(YELLOW, 1).and(GREEN, 1).and(BLUE, 1).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(1);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(1);
	}

	@Test
	void match() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());

		scorecard.match(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 1,2)
				.and(RED, 3).and(YELLOW, 4).and(GREEN, 5).and(BLUE, 6).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(0);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(RED).getCellOfValue(3).isChecked()).isTrue();
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(0);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(0);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(0);
	}

	@Test
	void matchNoMatch() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		scorecard.check(scorecard.getRow(RED).getCellOfValue(8));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(8));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(8));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(8));

		scorecard.match(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 4,4)
				.and(RED, 1).and(YELLOW, 1).and(GREEN, 1).and(BLUE, 1).build(),
			Color.allColored()));

		assertThat(scorecard.getNrOfMisses()).isEqualTo(0);
		assertThat(scorecard.getRow(RED).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(YELLOW).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(GREEN).nrOfChecked()).isEqualTo(1);
		assertThat(scorecard.getRow(BLUE).nrOfChecked()).isEqualTo(1);
	}

	@Test
	void getNrOfClosedRows() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		assertThat(scorecard.getNrOfClosedRows()).isEqualTo(0);
		assertThat(scorecard.getActiveColors()).isEqualTo(Color.allColored());

		scorecard.check(scorecard.getRow(RED).getCellOfValue(12));
		assertThat(scorecard.getNrOfClosedRows()).isEqualTo(1);
		assertThat(scorecard.getActiveColors()).isEqualTo(new Color[]{YELLOW, GREEN, BLUE});

		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(2));
		assertThat(scorecard.getNrOfClosedRows()).isEqualTo(2);
		assertThat(scorecard.getActiveColors()).isEqualTo(new Color[]{YELLOW, GREEN});
	}

	@Test
	void getScore() {
		Scorecard scorecard = new Scorecard(new SimpleStrategy());
		Score score = scorecard.getScore();
		System.out.println(score);

		assertThat(score.getMisses()).isEqualTo(0);
		assertThat(score.getTotal()).isEqualTo(0);
		assertThat(score.getRowScore(RED)).isEqualTo(0);
		assertThat(score.getRowScore(YELLOW)).isEqualTo(0);
		assertThat(score.getRowScore(GREEN)).isEqualTo(0);
		assertThat(score.getRowScore(BLUE)).isEqualTo(0);

		scorecard.check(scorecard.getRow(RED).getCellOfValue(11));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(4));
		scorecard.check(scorecard.getRow(YELLOW).getCellOfValue(10));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(12));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(8));
		scorecard.check(scorecard.getRow(GREEN).getCellOfValue(6));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(10));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(7));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(4));
		scorecard.check(scorecard.getRow(BLUE).getCellOfValue(2));
		score = scorecard.getScore();
		System.out.println(score);

		assertThat(score.getMisses()).isEqualTo(0);
		assertThat(score.getTotal()).isEqualTo(25);
		assertThat(score.getRowScore(RED)).isEqualTo(1);
		assertThat(score.getRowScore(YELLOW)).isEqualTo(3);
		assertThat(score.getRowScore(GREEN)).isEqualTo(6);
		assertThat(score.getRowScore(BLUE)).isEqualTo(15);

		scorecard.matchOnTurn(DiceCup.shake(
			PredictableNumberGenerator.of(Color.WHITE, 3,3)
				.and(RED, 1).and(YELLOW, 1).and(GREEN, 6).and(BLUE, 6).build(),
			Color.allColored()));
		score = scorecard.getScore();
		System.out.println(score);

		assertThat(score.getMisses()).isEqualTo(1);
		assertThat(score.getTotal()).isEqualTo(20);
		assertThat(score.getRowScore(RED)).isEqualTo(1);
		assertThat(score.getRowScore(YELLOW)).isEqualTo(3);
		assertThat(score.getRowScore(GREEN)).isEqualTo(6);
		assertThat(score.getRowScore(BLUE)).isEqualTo(15);
	}
}