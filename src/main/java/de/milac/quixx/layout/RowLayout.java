package de.milac.quixx.layout;

import de.milac.quixx.Cell;
import de.milac.quixx.Color;

import java.util.List;

public interface RowLayout {
	List<Cell> fillCells(Color color);
}
