package de.milac.quixx.event;

import de.milac.quixx.Color;

public class RowClosedEvent implements Event {
	private final Color color;

	public static RowClosedEvent of(Color color) {
		return new RowClosedEvent(color);
	}

	private RowClosedEvent(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return String.format("%s[%s]", this.getClass().getSimpleName(), color);
	}
}
