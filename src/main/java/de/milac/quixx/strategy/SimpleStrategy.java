package de.milac.quixx.strategy;

import de.milac.quixx.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SimpleStrategy implements Strategy {
	@Override
	public Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite) {
		return findBestMatch(scorecard, possibleMatchesWhite, List.of());
	}

	private Optional<Cell> findBestMatch(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<Cell> selectedToBeChecked) {
		Optional<MatchResult> firstMatch = possibleMatchesWhite.stream()
			.filter(mr -> mr.isPresent()
				&& scorecard.canCheck(mr.getMatch().orElseThrow())
				&& selectedToBeChecked.stream().noneMatch(c -> c.isAfter(mr.getMatch().orElseThrow()))
			)
			.findFirst();
		return firstMatch.flatMap(MatchResult::getMatch);
	}

	@Override
	public List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored) {
		List<Cell> matches = new ArrayList<>();
		findBestMatch(scorecard, possibleMatchesWhite, matches).ifPresent(matches::add);
		findBestMatch(scorecard, possibleMatchesColored, matches).ifPresent(matches::add);
		return matches;
	}

	@Override
	public void notifyOnTurn(Player player) {
		System.out.printf("Please roll the dice, %s%n", player);
	}
}
