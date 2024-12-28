package de.milac.quixx;

import de.milac.quixx.event.Event;
import de.milac.quixx.event.EventHandler;
import de.milac.quixx.event.EventSource;
import de.milac.quixx.event.RowClosedEvent;
import de.milac.quixx.layout.AscendingNumbers;
import de.milac.quixx.layout.RowLayout;

import java.util.Arrays;
import java.util.List;

public class Row extends EventHandler implements EventSource {
	private final Color color;
	private final Cell[] cells;
	private int firstActive = 0;

	public Row(Color color) {
		this(color, new AscendingNumbers());
	}

	public Row(Color color, RowLayout layout) {
		super();
		this.color = color;
		cells = layout.fillCells(color);
	}

	public MatchResult findMatch(int... sums) {
		if (isActive()) {
			for (int i = firstActive; i < cells.length; i++) {
				Cell cell = cells[i];
				for (int sum : sums) {
					if (cell.match(sum)) {
						return MatchResult.of(cell, color, firstActive);
					}
				}
			}
		}
		return MatchResult.empty(color, firstActive);
	}

	private int shift(int currentPos) {
		boolean closed = currentPos == cells.length-1;
		if (closed) {
			System.out.println("Closing row " + color);
			fire(RowClosedEvent.of(color));
		}
		return closed ? -1 : currentPos+1;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public List<Class<? extends Event>> eventsOfInterest() {
		return List.of(RowClosedEvent.class);
	}

	@Override
	public void handleEvent(Event event) {
		if (event instanceof RowClosedEvent e && color.equals(e.getColor())) {
			firstActive = -1;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(String.format("%6s |", color));
		for (Cell c : cells) {
			sb.append(String.format(" %2d(%s) |", c.getValue(), c.getState().getSymbol()));
		}
		sb.append(isClosed() ? " closed" : "");
		return sb.toString();
	}

	public boolean isClosed() {
		return firstActive < 0;
	}

	public boolean isActive() {
		return !isClosed();
	}

	public long nrOfChecked() {
		return Arrays.stream(cells).filter(Cell::isChecked).count();
	}

	public void check(Cell cell) {
		cell.check();
		firstActive = shift(cell.getPos());
	}
}
