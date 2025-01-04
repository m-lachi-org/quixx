package de.milac.quixx;

import org.junit.jupiter.api.Test;

import java.util.List;

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
		Row row1 = new Row(RED);
		Row row2 = new Row(RED);
		Row row3 = new Row(RED);

		row1.check(row1.getCellOfValue(7));
		row1.check(row1.getCellOfValue(8));
		row1.check(row1.getCellOfValue(9));
		row1.check(row1.getCellOfValue(10));
		row1.check(row1.getCellOfValue(11));
		row1.check(row1.getCellOfValue(12));
		MatchResult match1 = row1.findMatch(2);

		assertThat(match1.isEmpty()).isTrue();
		assertThat(match1.getColor()).isEqualTo(RED);
		assertThat(match1.getDistanceToFirstActive()).isNegative();

		// should find match in same round
		row2.check(row2.getCellOfValue(7));
		row2.check(row2.getCellOfValue(8));
		row2.check(row2.getCellOfValue(9));
		row2.check(row2.getCellOfValue(10));
		row2.check(row2.getCellOfValue(11));
		MatchResult match2 = row2.findMatch(12);

		assertThat(match2.isEmpty()).isFalse();
		assertThat(match2.getColor()).isEqualTo(RED);
		assertThat(match2.getDistanceToFirstActive()).isEqualTo(0);
		row2.check(match2.getMatch().orElseThrow());
		assertThat(row2.nrOfChecked()).isEqualTo(7);

		// should find match in same round
		new Round(new Player("A"), List.of(new Player("B")));
		MatchResult match3 = row3.findMatch(2);

		assertThat(match3.isEmpty()).isTrue();
		assertThat(match3.getColor()).isEqualTo(RED);
		assertThat(match3.getDistanceToFirstActive()).isNegative();
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

		rowPlayer1.check(rowPlayer1.getCellOfValue(7));
		rowPlayer1.check(rowPlayer1.getCellOfValue(8));
		rowPlayer1.check(rowPlayer1.getCellOfValue(9));
		rowPlayer1.check(rowPlayer1.getCellOfValue(10));
		rowPlayer1.check(rowPlayer1.getCellOfValue(11));
		rowPlayer1.check(rowPlayer1.getCellOfValue(12));

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

		row.check(row.getCellOfValue(8));
		row.check(row.getCellOfValue(9));
		row.check(row.getCellOfValue(10));
		row.check(row.getCellOfValue(11));
		row.check(row.getCellOfValue(12));
		assertThat(row.nrOfChecked()).isEqualTo(7);
	}

	@Test
	void check() {
		Row row = new Row(RED);
		assertThat(row.getCellAt(0).isChecked()).isFalse();

		row.check(row.getCellAt(0));
		assertThat(row.getCellAt(0).isChecked()).isTrue();

		row.check(row.getCellAt(6));
		row.check(row.getCellAt(7));
		row.check(row.getCellAt(8));
		row.check(row.getCellAt(9));
		row.check(row.getCellAt(10));
		assertThat(row.getCellAt(10).isChecked()).isTrue();
		assertThat(row.getCellAt(11).isChecked()).isTrue();
		assertThat(row.isClosed()).isTrue();
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

		row.check(row.getCellAt(6));
		row.check(row.getCellAt(7));
		row.check(row.getCellAt(8));
		row.check(row.getCellAt(9));
		row.getCellAt(10).check();

		row.shiftOrClose(10);
		assertThat(row.isActive()).isFalse();
		assertThat(row.getFirstActive()).isEqualTo(12);
		assertThat(rowOtherPlayer.isActive()).isFalse();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(0);

		row.shiftOrClose(11);
		assertThat(row.isActive()).isFalse();
		assertThat(row.getFirstActive()).isEqualTo(12);
		assertThat(rowOtherPlayer.isActive()).isFalse();
		assertThat(rowOtherPlayer.getFirstActive()).isEqualTo(0);
	}

	@Test
	void fillCellsDefaultAscending() {
		Row row = new Row(RED);
		assertThat(row.getCellAt(0).getValue()).isEqualTo(2);
		assertThat(row.getCellAt(10).getValue()).isEqualTo(12);
		assertThat(row.getCellAt(11).getValue()).isEqualTo(0);
	}

	@Test
	void canCheck() {
		Row row = new Row(RED);
		// all cells can be checked except the last one
		assertThat(row.canCheck(row.getCellAt(0)).isPassed()).isTrue();
		assertThat(row.canCheck(row.getCellAt(9)).isPassed()).isTrue();
		assertThat(row.canCheck(row.getCellAt(10)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(11)).isPassed()).isFalse();

		// after checking the first cell, only the following cells can be checked, except the last one
		row.check(row.getCellAt(0));
		assertThat(row.canCheck(row.getCellAt(0)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(9)).isPassed()).isTrue();
		assertThat(row.canCheck(row.getCellAt(10)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(11)).isPassed()).isFalse();

		// after checking the previous to last cell, nothing can be checked
		row.check(row.getCellAt(9));
		assertThat(row.canCheck(row.getCellAt(0)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(9)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(10)).isPassed()).isFalse();
		assertThat(row.canCheck(row.getCellAt(11)).isPassed()).isFalse();

		// having 4 cells checked, the last one can also be checked
		Row row2 = new Row(RED);
		row2.check(row2.getCellAt(2));
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isFalse();
		row2.check(row2.getCellAt(3));
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isFalse();
		row2.check(row2.getCellAt(4));
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isFalse();
		row2.check(row2.getCellAt(5));
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isFalse();
		row2.check(row2.getCellAt(6));
		assertThat(row2.canCheck(row2.getCellAt(0)).isPassed()).isFalse();
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isTrue();
		assertThat(row2.canCheck(row2.getCellAt(11)).isPassed()).isFalse();

		row2.check(row2.getCellAt(10));
		assertThat(row2.canCheck(row2.getCellAt(0)).isPassed()).isFalse();
		assertThat(row2.canCheck(row2.getCellAt(10)).isPassed()).isFalse();
		assertThat(row2.getCellAt(11).isChecked()).isTrue();
	}
}