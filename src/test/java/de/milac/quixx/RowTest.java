package de.milac.quixx;

import org.junit.jupiter.api.Test;

import static de.milac.quixx.Color.RED;
import static org.assertj.core.api.Assertions.assertThat;

class RowTest {

	@Test
	void findMatchNoCellForSum() {
		Row row = new Row(RED);
		MatchResult match = row.findMatch(1);

		assertThat(match.isEmpty()).isTrue();
		assertThat(match.getColor()).isEqualTo(RED);
		assertThat(match.getDistanceToFirstActive()).isNegative();
	}

	@Test
	void findMatchFound() {
		Row row = new Row(RED);
		MatchResult match = row.findMatch(2);

		assertThat(match.isEmpty()).isFalse();
		assertThat(match.getColor()).isEqualTo(RED);
		assertThat(match.getDistanceToFirstActive()).isEqualTo(0);
		Cell cell = match.getMatch().orElseThrow();
		assertThat(cell.getValue()).isEqualTo(2);
	}

	@Test
	void findMatchInactive() {
		Row row = new Row(RED);
		row.shiftOrClose(11);
		MatchResult match = row.findMatch(2);

		assertThat(match.isEmpty()).isTrue();
		assertThat(match.getColor()).isEqualTo(RED);
		assertThat(match.getDistanceToFirstActive()).isNegative();
	}

	@Test
	void findMatchAlreadyChecked() {
		Row row = new Row(RED);
		row.check(row.getCellOfValue(2));

		MatchResult match = row.findMatch(2);

		assertThat(match.isEmpty()).isTrue();
		assertThat(match.getColor()).isEqualTo(RED);
		assertThat(match.getDistanceToFirstActive()).isNegative();
	}

	@Test
	void findMatchTwoSumsBothUnchecked() {
		Row row = new Row(RED);
		MatchResult match = row.findMatch(2, 5);

		assertThat(match.isEmpty()).isFalse();
		Cell cell = match.getMatch().orElseThrow();
		assertThat(cell.getValue()).isEqualTo(2);
	}

	@Test
	void findMatchTwoSumsFirstChecked() {
		Row row = new Row(RED);
		row.check(row.getCellOfValue(2));

		MatchResult match = row.findMatch(2, 5);

		assertThat(match.isEmpty()).isFalse();
		Cell cell = match.getMatch().orElseThrow();
		assertThat(cell.getValue()).isEqualTo(5);
	}

	@Test
	void findMatchTwoSumsSecondChecked() {
		Row row = new Row(RED);
		row.check(row.getCellOfValue(5));

		MatchResult match = row.findMatch(2, 5);

		assertThat(match.isEmpty()).isTrue();
		assertThat(match.getColor()).isEqualTo(RED);
		assertThat(match.getDistanceToFirstActive()).isNegative();
	}

	@Test
	void handleEvent() {
		Row rowPlayer1 = new Row(RED);
		assertThat(rowPlayer1.isClosed()).isFalse();
		assertThat(rowPlayer1.isActive()).isTrue();
		Row rowPlayer2 = new Row(RED);
		assertThat(rowPlayer2.isClosed()).isFalse();

		rowPlayer1.shiftOrClose(11);

		assertThat(rowPlayer1.isClosed()).isTrue();
		assertThat(rowPlayer1.isActive()).isFalse();
		assertThat(rowPlayer2.isClosed()).isTrue();
	}

	@Test
	void nrOfChecked() {
		Row row = new Row(RED);
		assertThat(row.nrOfChecked()).isEqualTo(0);

		row.check(row.getCellOfValue(2));
		assertThat(row.nrOfChecked()).isEqualTo(1);

		row.check(row.getCellOfValue(12));
		assertThat(row.nrOfChecked()).isEqualTo(3);
	}

	@Test
	void check() {
		Row row = new Row(RED);
		assertThat(row.getCellAt(0).isChecked()).isFalse();

		row.check(row.getCellAt(0));
		assertThat(row.getCellAt(0).isChecked()).isTrue();

		row.check(row.getCellAt(10));
		assertThat(row.getCellAt(10).isChecked()).isTrue();
		assertThat(row.getCellAt(11).isChecked()).isTrue();
	}

	@Test
	void shiftOrClose() {
		Row row = new Row(RED);
		Row rowOtherPlayer = new Row(RED);
		assertThat(row.isActive()).isTrue();
		assertThat(row.getFirstActive()).isEqualTo(0);
		assertThat(rowOtherPlayer.isActive()).isTrue();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(0);

		row.shiftOrClose(0);
		assertThat(row.isActive()).isTrue();
		assertThat(row.getFirstActive()).isEqualTo(1);
		assertThat(rowOtherPlayer.isActive()).isTrue();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(0);

		row.shiftOrClose(10);
		assertThat(row.isActive()).isFalse();
		assertThat(row.getFirstActive()).isEqualTo(-1);
		assertThat(rowOtherPlayer.isActive()).isFalse();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(-1);

		row.shiftOrClose(11);
		assertThat(row.isActive()).isFalse();
		assertThat(row.getFirstActive()).isEqualTo(-1);
		assertThat(rowOtherPlayer.isActive()).isFalse();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(-1);
	}

	@Test
	void fillCellsDefaultAscending() {
		Row row = new Row(RED);
		assertThat(row.getCellAt(0).getValue()).isEqualTo(2);
		assertThat(row.getCellAt(10).getValue()).isEqualTo(12);
		assertThat(row.getCellAt(11).getValue()).isEqualTo(0);
	}
}