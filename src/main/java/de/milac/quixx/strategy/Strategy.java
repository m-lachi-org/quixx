package de.milac.quixx.strategy;

import de.milac.quixx.Cell;
import de.milac.quixx.MatchResult;

import java.util.List;
import java.util.Optional;

public interface Strategy {
	Optional<Cell> bestMatches(List<MatchResult> possibleMatches);

	List<Cell> bestMatches(List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored);
}
