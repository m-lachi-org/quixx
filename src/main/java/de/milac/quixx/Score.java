package de.milac.quixx;

import java.util.*;

public class Score implements Comparable<Score> {
	private static final Map<Long, Integer> SCORE_MAP = new HashMap<>();
	static {
		SCORE_MAP.put(1L, 1);
		SCORE_MAP.put(2L, 3);
		SCORE_MAP.put(3L, 6);
		SCORE_MAP.put(4L, 10);
		SCORE_MAP.put(5L, 15);
		SCORE_MAP.put(6L, 21);
		SCORE_MAP.put(7L, 28);
		SCORE_MAP.put(8L, 36);
		SCORE_MAP.put(9L, 45);
		SCORE_MAP.put(10L, 55);
		SCORE_MAP.put(11L, 66);
		SCORE_MAP.put(12L, 78);
	}
	private static final int MISS_SCORE = -5;

	private final Map<Color, Integer> rowScores = new HashMap<>();
	private final int misses;
	private final int total;

	public static Score of(Collection<Row> rows, int misses) {
		return new Score(rows, misses);
	}

	public Score(Collection<Row> rows, int misses) {
		for (Row row : rows) {
			rowScores.put(row.getColor(), SCORE_MAP.getOrDefault(row.nrOfChecked(), 0));
		}
		this.misses = misses;
		total = rowScores.values().stream().reduce(0, Integer::sum) + (MISS_SCORE * misses);
	}

	@Override
	public String toString() {
		return String.format("%s, misses: %d, total: %d", rowScores, misses, total);
	}

	@Override
	public int compareTo(Score otherScore) {
		Comparator<Score> comp = Comparator.comparing(Score::getTotal);
		return comp.compare(this, otherScore);
	}

	public int getTotal() {
		return total;
	}

	int getRowScore(Color color) {
		return rowScores.getOrDefault(color, 0);
	}

	public int getMisses() {
		return misses;
	}
}
