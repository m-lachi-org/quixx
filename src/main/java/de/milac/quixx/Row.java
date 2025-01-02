package de.milac.quixx;

import de.milac.quixx.event.Event;
import de.milac.quixx.event.EventHandler;
import de.milac.quixx.event.EventSource;
import de.milac.quixx.event.RowClosedEvent;
import de.milac.quixx.layout.AscendingNumbers;
import de.milac.quixx.layout.RowLayout;

import java.util.List;
import java.util.NoSuchElementException;

public class Row extends EventHandler implements EventSource {
	private final Color color;
	private final List<Cell> cells;
	private int firstActive = 0;

	public Row(Color color) {
		this(color, new AscendingNumbers());
	}

	public Row(Color color, RowLayout layout) {
		super();
		this.color = color;
		cells = fillCells(color, layout);
	}

	List<Cell> fillCells(Color color, RowLayout layout) {
		final List<Cell> cells;
		cells = layout.fillCells(color);
		cells.add(new Cell(getColor(), cells.size(), 0));
		return cells;
	}

	public MatchResult findMatch(int... sums) {
		if (isActive()) {
			for (int i = firstActive; i < cells.size(); i++) {
				Cell cell = cells.get(i);
				for (int sum : sums) {
					if (cell.match(sum)) {
						return MatchResult.of(cell, color, firstActive);
					}
				}
			}
		}
		return MatchResult.empty(color, firstActive);
	}

	Cell getCellAt(int idx) {
		return cells.get(idx);
	}

	Cell getCellOfValue(int value) {
		return cells.stream().filter(c -> c.getValue() == value).findFirst()
			.orElseThrow(() -> new NoSuchElementException(String.format("No cell found for value %d", value)));
	}

	void shiftOrClose(int currentPos) {
		if (!isClosed()) {
			boolean closed = currentPos >= cells.size() - 2;
			firstActive = closed ? -1 : currentPos + 1;
			if (closed) {
				System.out.println("Closing row " + color);
				check(cells.get(cells.size() - 1));
				fire(RowClosedEvent.of(color));
			}
		}
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
		StringBuilder sb = new StringBuilder(String.format("%s%s |", span(7, color.name()), color.dyedName()));
		for (Cell c : cells) {
			sb.append(String.format("%s%s %s%s%s |",
				span(3, String.valueOf(c.getValue())),
				color.dye(String.valueOf(c.getValue())),
				color.dye("("), c.getState().getSymbol(), color.dye(")")
			));
		}
		sb.append(String.format(" [%d]%s", nrOfChecked(), isClosed() ? " closed" : ""));
		return sb.toString();
	}

	private String span(int maxLength, String text) {
		return String.format("%"+Math.max(1, maxLength - text.length())+"s", " ");
	}

	public boolean isClosed() {
		return firstActive < 0;
	}

	public boolean isActive() {
		return !isClosed();
	}

	public long nrOfChecked() {
		return cells.stream().filter(Cell::isChecked).count();
	}

	public boolean canCheck(Cell cell) {
		return cell.getPos() >= firstActive;
	}

	public void check(Cell cell) {
		if (!canCheck(cell)) {
			throw new IllegalStateException(String.format("Cell %s can't be checked", cell));
		}
		cell.check();
		shiftOrClose(cell.getPos());
	}

	public int getFirstActive() {
		return firstActive;
	}
}
