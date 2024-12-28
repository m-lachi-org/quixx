package de.milac.quixx;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleStrategy implements Strategy {
	@Override
	public Optional<Cell> bestMatches(List<MatchResult> possibleMatches) {
		Optional<MatchResult> firstMatch = possibleMatches.stream().filter(MatchResult::isPresent).findFirst();
		return firstMatch.flatMap(MatchResult::getMatch);
	}

	@Override
	public List<Cell> bestMatches(List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored) {
		List<Cell> matches = new ArrayList<>();
		bestMatches(possibleMatchesWhite).ifPresent(matches::add);
		bestMatches(possibleMatchesColored).ifPresent(matches::add);
		return matches;
	}
}
