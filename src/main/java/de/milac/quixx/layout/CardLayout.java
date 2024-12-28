package de.milac.quixx.layout;

import de.milac.quixx.Color;
import de.milac.quixx.Row;

import java.util.Map;

public interface CardLayout {
	Map<Color, Row> fillRows();
}
