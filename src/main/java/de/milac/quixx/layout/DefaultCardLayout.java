package de.milac.quixx.layout;

import de.milac.quixx.Color;
import de.milac.quixx.Row;

import java.util.LinkedHashMap;
import java.util.Map;

import static de.milac.quixx.Color.*;

public class DefaultCardLayout implements CardLayout {
	@Override
	public Map<Color, Row> fillRows() {
		Map<Color, Row> rows = new LinkedHashMap<>();
		rows.put(RED, new Row(RED));
		rows.put(YELLOW, new Row(YELLOW));
		rows.put(GREEN, new Row(GREEN, new DescendingNumbers()));
		rows.put(BLUE, new Row(BLUE, new DescendingNumbers()));
		return rows;
	}
}
