package de.milac.quixx;

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
