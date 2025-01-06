package de.milac.quixx;

import de.milac.quixx.event.Event;
import de.milac.quixx.event.EventHandler;
import de.milac.quixx.event.EventSource;
import de.milac.quixx.event.RowClosedEvent;
import de.milac.quixx.layout.AscendingNumbers;
import de.milac.quixx.layout.RowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Row extends EventHandler implements EventSource {
	private final Color color;
	private final List<Cell> cells;
	private int firstActive = 0;
	private int closedInRound = -1;

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
		if (isActiveForCurrentRound()) {
			for (int i = firstActive; i < cells.size(); i++) {
				Cell cell = cells.get(i);
				for (int sum : sums) {
					if (cell.match(sum) && canCheck(cell).isPassed()) {
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

	Cell getLastCell() {
		return cells.get(cells.size() - 2);
	}

	Cell getCellOfValue(int value) {
		return cells.stream().filter(c -> c.getValue() == value).findFirst()
			.orElseThrow(() -> new NoSuchElementException(String.format("No cell found for value %d", value)));
	}

	void shiftOrClose(int currentPos) {
		firstActive = currentPos + 1;
		boolean willClose = isLast(currentPos);
		if (willClose) {
			check(getCellAt(firstActive));
			if (!isClosed()) {
				System.out.println("Closing row " + color);
				setClosed();
				fire(RowClosedEvent.of(color));
			}
		}
	}

	private void setClosed() {
		closedInRound = Round.getCount();
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
			setClosed();
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
		return closedInRound > -1;
	}

	public boolean isActive() {
		return !isClosed();
	}

	private boolean isActiveForCurrentRound() {
		return isActive() || closedInRound == Round.getCount();
	}

	public long nrOfChecked() {
		return cells.stream().filter(Cell::isChecked).count();
	}

	public CheckResult canCheck(Cell cell) {
		List<String> failureReasons = new ArrayList<>();
		if (cell.getPos() < firstActive) {
			failureReasons.add("Cell is before latest checked cell");
		}
		if (isLast(cell) && nrOfChecked() < Rules.MIN_CHECKED_BEFORE_CLOSE) {
			failureReasons.add("Must check 5 cells before closing");
		}
		if (isCloseCell(cell) && !getLastCell().isChecked()) {
			failureReasons.add("Must check last cell before closing");
		}
		return CheckResult.of(failureReasons.isEmpty(), String.join(", ", failureReasons));
	}

	private boolean isCloseCell(Cell cell) {
		return cell.getPos() == cells.size() - 1;
	}

	private boolean isLast(Cell cell) {
		return isLast(cell.getPos());
	}

	private boolean isLast(int pos) {
		return pos == cells.size() - 2;
	}

	public void check(Cell cell) {
		CheckResult checkResult = canCheck(cell);
		if (!checkResult.isPassed()) {
			throw new IllegalStateException(String.format("Cell %s can't be checked: %s", cell, checkResult.getFailureReason()));
		}
		cell.check();
		shiftOrClose(cell.getPos());
	}

	public int getFirstActive() {
		return firstActive;
	}

	public static class CheckResult {
		private final boolean passed;
		private final String failureReason;

		private CheckResult(boolean passed, String failureReason) {
			this.passed = passed;
			this.failureReason = failureReason;
		}

		public static CheckResult of(boolean passed, String failureReason) {
			return new CheckResult(passed, failureReason);
		}

		public boolean isPassed() {
			return passed;
		}

		public String getFailureReason() {
			return failureReason;
		}
	}
}
