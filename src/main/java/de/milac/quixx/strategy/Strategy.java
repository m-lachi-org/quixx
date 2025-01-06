package de.milac.quixx.strategy;

import de.milac.quixx.*;

import java.util.List;
import java.util.Optional;

public interface Strategy {
	Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite);

	List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored);

	void notifyOnTurn(Player player);

	void notifyOnMiss(int nrOfMisses);

	default List<Cell> filterForPossibleCells(List<MatchResult> possibleMatchesWhite) {
		return possibleMatchesWhite.stream()
			.filter(MatchResult::isPresent)
			.map(mr -> mr.getMatch().orElseThrow())
			.toList();
	}
}
