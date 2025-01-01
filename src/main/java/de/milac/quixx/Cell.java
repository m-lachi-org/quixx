package de.milac.quixx;

import static de.milac.quixx.Cell.State.*;

public class Cell {
	private final int value;
	private final Color color;
	private final int pos;
	private State state = ACTIVE;

	public enum State {
		ACTIVE(" "), CHECKED("X");

		private final String symbol;

		State(String symbol) {
			this.symbol = symbol;
		}

		public String getSymbol() {
			return symbol;
		}

	}
	public Cell(Color color, int pos, int value) {
		this.color = color;
		this.pos = pos;
		this.value = value;
	}

	public boolean match(int value) {
		return this.value == value;
	}

	void check() {
		state = CHECKED;
		System.out.println(this);
	}

	public boolean isChecked() {
		return CHECKED.equals(state);
	}

	public int getValue() {
		return value;
	}

	public State getState() {
		return state;
	}

	public Color getColor() {
		return color;
	}

	public int getPos() {
		return pos;
	}

	@Override
	public String toString() {
		return String.format("  %s-%d [%s]",color, value, state);
	}
}
