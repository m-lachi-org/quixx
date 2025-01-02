package de.milac.quixx;

import de.milac.quixx.dice.DiceCup;
import de.milac.quixx.layout.CardLayout;
import de.milac.quixx.layout.DefaultCardLayout;
import de.milac.quixx.strategy.Strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static de.milac.quixx.Color.*;

public class Scorecard {
	private final Map<Color, Row> rows;
	private int nrOfMisses = 0;
	private final Strategy strategy;

	public Scorecard(Strategy strategy) {
		this(strategy, new DefaultCardLayout());
	}

	public Scorecard(Strategy strategy, CardLayout layout) {
		this.strategy = strategy;
		rows = layout.fillRows();
	}

	public void matchOnTurn(DiceCup diceCup) {
		List<MatchResult> possibleMatchesWhite = findPossibleMatchesWhite(diceCup);
		List<MatchResult> possibleMatchesColored = findPossibleMatchesColored(diceCup)
			.stream().filter(mr -> !possibleMatchesWhite.contains(mr)).toList();
		boolean matchFound = possibleMatchesWhite.stream().anyMatch(MatchResult::isPresent) ||
			possibleMatchesColored.stream().anyMatch(MatchResult::isPresent);
		if (matchFound) {
			List<Cell> cells = strategy.bestMatches(this, possibleMatchesWhite, possibleMatchesColored);
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

	public void match(DiceCup diceCup) {
		List<MatchResult> possibleMatches = findPossibleMatchesWhite(diceCup);
		AtomicBoolean matchFound = new AtomicBoolean(possibleMatches.stream().anyMatch(MatchResult::isPresent));
		if (matchFound.get()) {
			strategy.bestMatches(this, possibleMatches)
				.ifPresentOrElse(this::check, () -> matchFound.set(false));
		}
		if (!matchFound.get()) {
			System.out.println("Nothing matches!");
		}
	}

	List<MatchResult> findPossibleMatchesWhite(DiceCup diceCup) {
		List<MatchResult> possibleMatches = new ArrayList<>();
		for (Row row : rows.values()) {
			possibleMatches.add(row.findMatch(diceCup.sumOf(WHITE)));
		}
		return possibleMatches;
	}

	List<MatchResult> findPossibleMatchesColored(DiceCup diceCup) {
		List<MatchResult> possibleMatches = new ArrayList<>();
		for (Row row : rows.values()) {
			if (diceCup.hasColor(row.getColor())) {
				possibleMatches.add(row.findMatch(diceCup.sumOf(row.getColor())));
			}
		}
		return possibleMatches;
	}

	public boolean canCheck(Cell cell) {
		return rows.get(cell.getColor()).canCheck(cell);
	}

	void check(Cell cell) {
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

	public Row getRow(Color color) {
		return rows.get(color);
	}
}
