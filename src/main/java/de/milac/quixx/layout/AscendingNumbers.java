package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;

public class AscendingNumbers implements RowLayout {
	@Override
	public Cell[] fillCells(Color color) {
		Cell[] cells = new Cell[11];
		for (int i = 0; i < cells.length; i++) {
			cells[i] = new Cell(color, i, i+2);
		}
		return cells;
	}
}
