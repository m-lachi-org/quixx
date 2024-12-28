package de.milac.quixx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.milac.quixx.Color.*;

public class Scorecard {
	private final Map<Color, Row> rows = new HashMap<>();
	private int nrOfMisses = 0;
	private final Strategy strategy;

	{
		rows.put(RED, new Row(RED));
		rows.put(YELLOW, new Row(YELLOW));
		rows.put(GREEN, new Row(GREEN));
		rows.put(BLUE, new Row(BLUE));
	}

	public Scorecard(Strategy strategy) {
		this.strategy = strategy;
	}

	public void matchOnTurn(DiceCup dices) {
		List<MatchResult> possibleMatchesWhite = findPossibleMatchesWhite(dices);
		List<MatchResult> possibleMatchesColored = findPossibleMatchesColored(dices)
			.stream().filter(mr -> !possibleMatchesWhite.contains(mr)).toList();
		boolean matchFound = possibleMatchesWhite.stream().anyMatch(MatchResult::isPresent) ||
			possibleMatchesColored.stream().anyMatch(MatchResult::isPresent);
		if (matchFound) {
			List<Cell> cells = strategy.bestMatches(possibleMatchesWhite, possibleMatchesColored);
			if (cells.isEmpty()) {
				matchFound = false;
			} else {
				cells.forEach(this::check);
			}
		}
		if (!matchFound) {
			System.out.println("Nothing matches!");
			nrOfMisses++;
		}
	}

	public void match(DiceCup dices) {
		List<MatchResult> possibleMatches = findPossibleMatchesWhite(dices);
		AtomicBoolean matchFound = new AtomicBoolean(possibleMatches.stream().anyMatch(MatchResult::isPresent));
		if (matchFound.get()) {
			strategy.bestMatches(possibleMatches)
				.ifPresentOrElse(this::check, () -> matchFound.set(false));
		}
	}

	private List<MatchResult> findPossibleMatchesWhite(DiceCup dices) {
		List<MatchResult> possibleMatches = new ArrayList<>();
		for (Row row : rows.values()) {
			possibleMatches.add(row.findMatch(dices.sumOf(WHITE)));
		}
		return possibleMatches;
	}

	private List<MatchResult> findPossibleMatchesColored(DiceCup dices) {
		List<MatchResult> possibleMatches = new ArrayList<>();
		for (Row row : rows.values()) {
			if (dices.hasColor(row.getColor())) {
				possibleMatches.add(row.findMatch(dices.sumOf(row.getColor())));
			}
		}
		return possibleMatches;
	}

	private void check(Cell cell) {
		rows.get(cell.getColor()).check(cell);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Row row : rows.values()) {
			sb.append(String.format("%s%n", row));
		}
		sb.append(String.format("misses: %d%n", nrOfMisses));
		return sb.toString();
	}

	public long getNrOfClosedRows() {
		return rows.values().stream().filter(Row::isClosed).count();
	}

	public Color[] getActiveColors() {
		return rows.values().stream().filter(Row::isActive).map(Row::getColor).toArray(Color[]::new);
	}

	public Score getScore() {
		return Score.of(rows.values(), nrOfMisses);
	}

	public int getNrOfMisses() {
		return nrOfMisses;
	}
}
