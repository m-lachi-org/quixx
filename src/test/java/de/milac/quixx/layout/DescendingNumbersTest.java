package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DescendingNumbersTest {

	@Test
	void fillCells() {
		DescendingNumbers layout = new DescendingNumbers();
		List<Cell> cells = layout.fillCells(Color.RED);

		assertThat(cells)
			.hasSize(11)
			.allMatch(c -> Color.RED.equals(c.getColor()))
			.flatMap(Cell::getValue).hasToString("[12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2]")
		;
	}
}