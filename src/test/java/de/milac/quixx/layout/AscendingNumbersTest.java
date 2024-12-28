package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AscendingNumbersTest {

	@Test
	void fillCells() {
		AscendingNumbers layout = new AscendingNumbers();
		List<Cell> cells = layout.fillCells(Color.RED);

		assertThat(cells)
			.hasSize(11)
			.allMatch(c -> Color.RED.equals(c.getColor()))
			.flatMap(Cell::getValue).hasToString("[2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]")
		;
	}
}