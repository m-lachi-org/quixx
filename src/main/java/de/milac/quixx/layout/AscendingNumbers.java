package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;

import java.util.ArrayList;
import java.util.List;

public class AscendingNumbers implements RowLayout {
	@Override
	public List<Cell> fillCells(Color color) {
		List<Cell> cells = new ArrayList<>();
		int value = 2;
		for (int i = 0; i < 11; i++) {
			cells.add(new Cell(color, i, value++));
		}
		return cells;
	}
}
