package de.milac.quixx;

import java.util.*;

public class MatchResult {
	private final Cell match;
	private final Color color;
	private int distanceToFirstActive;

	private MatchResult(Cell match, Color color, int firstActiveInRow) {
		this.match = match;
		this.color = color;
		this.distanceToFirstActive = Optional.ofNullable(this.match).map(Cell::getPos).orElse(-1) - (Math.max(firstActiveInRow, 0));
	}

	public static MatchResult of(Cell match, Color color, int firstActiveInRow) {
		return new MatchResult(match, color, firstActiveInRow);
	}

	public static MatchResult empty(Color color, int firstActiveInRow) {
		return MatchResult.of(null, color, firstActiveInRow);
	}

	public boolean isEmpty() {
		return getMatch().isEmpty();
	}

	public boolean isPresent() {
		return !isEmpty();
	}

	public Optional<Cell> getMatch() {
		return Optional.ofNullable(match);
	}

	public Color getColor() {
		return color;
	}

	public int getDistanceToFirstActive() {
		return distanceToFirstActive;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		MatchResult that = (MatchResult) o;
		return getDistanceToFirstActive() == that.getDistanceToFirstActive()
			&& Objects.equals(getMatch(), that.getMatch())
			&& getColor() == that.getColor();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getMatch(), getColor(), getDistanceToFirstActive());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ")
			.add("match=" + match). add("row color=" + color)
			.toString();
	}

	public MatchResult updateDistanceToFirstActive(List<Cell> selectedToBeChecked) {
		if (!isEmpty()) {
			selectedToBeChecked.stream()
				.filter(c -> c.isBefore(getMatch().orElseThrow()))
				.max(Comparator.comparingInt(Cell::getPos))
				.ifPresent(c -> distanceToFirstActive = getMatch().orElseThrow().getPos() - c.getPos());
		}
		return this;
	}
}
