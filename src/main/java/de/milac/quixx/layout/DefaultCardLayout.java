package de.milac.quixx.layout;

import de.milac.quixx.Color;
import de.milac.quixx.Row;

import java.util.Map;

import static de.milac.quixx.Color.*;

public class DefaultCardLayout implements CardLayout {
	@Override
	public Map<Color, Row> fillRows() {
		return Map.of(
			RED, new Row(RED),
			YELLOW, new Row(YELLOW),
			GREEN, new Row(GREEN),
			BLUE, new Row(BLUE)
		);
	}
}
