package de.milac.quixx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CellTest {

	@Test
	void match() {
		Cell cell = new Cell(Color.RED, 1, 7);

		assertThat(cell.match(7)).isTrue();
		assertThat(cell.match(2)).isFalse();
	}

	@Test
	void check() {
		Cell cell = new Cell(Color.RED, 1, 7);
		assertThat(cell.isChecked()).isFalse();
		assertThat(cell.getState()).isEqualTo(Cell.State.ACTIVE);

		cell.check();

		assertThat(cell.isChecked()).isTrue();
		assertThat(cell.getState()).isEqualTo(Cell.State.CHECKED);
	}
}