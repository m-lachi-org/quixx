package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class AscendingNumbers implements RowLayout {
	@Override
	public List<Cell> fillCells(Color color) {
		List<Cell> cells = new ArrayList<>();
		IntStream.range(0, 11).forEach(i -> cells.add(new Cell(color, i, i + 2)));
		return cells;
	}
}
