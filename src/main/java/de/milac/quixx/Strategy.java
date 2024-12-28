package de.milac.quixx;

import java.util.List;
import java.util.Optional;

public interface Strategy {
	Optional<Cell> bestMatches(List<MatchResult> possibleMatches);

	List<Cell> bestMatches(List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored);
}
