package de.milac.quixx.strategy;

import de.milac.quixx.Cell;
import de.milac.quixx.MatchResult;
import de.milac.quixx.Scorecard;

import java.util.List;
import java.util.Optional;

public interface Strategy {
	Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite);

	List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored);
}
