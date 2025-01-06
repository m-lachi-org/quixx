package de.milac.quixx.strategy;

import de.milac.quixx.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SimpleStrategy implements Strategy {
	@Override
	public Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite) {
		System.out.printf("Possible matches for WHITE dice: %s%n", filterForPossibleCells(possibleMatchesWhite));
		return findBestMatch(scorecard, possibleMatchesWhite, List.of(), 1);
	}

	private Optional<Cell> findBestMatch(Scorecard scorecard, List<MatchResult> possibleMatches, List<Cell> selectedToBeChecked, int maxDistance) {
		Optional<MatchResult> match = possibleMatches.stream()
			.map(mr -> mr.updateDistanceToFirstActive(selectedToBeChecked))
			.filter(mr -> mr.isPresent()
				&& mr.getDistanceToFirstActive() <= maxDistance
				&& scorecard.canCheck(mr.getMatch().orElseThrow()).isPassed()
				&& selectedToBeChecked.stream().noneMatch(c -> c.isAfter(mr.getMatch().orElseThrow()))
			)
			.min(Comparator.comparingInt(MatchResult::getDistanceToFirstActive));
		return match.flatMap(MatchResult::getMatch);
	}

	@Override
	public List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored) {
		List<Cell> matches = new ArrayList<>();
		System.out.printf("Possible matches for WHITE dice: %s%n", filterForPossibleCells(possibleMatchesWhite));
		System.out.printf("Possible matches for COLORED dice: %s%n", filterForPossibleCells(possibleMatchesColored));
		findBestMatch(scorecard, possibleMatchesWhite, matches, 1).ifPresent(matches::add);
		findBestMatch(scorecard, possibleMatchesColored, matches, 1).ifPresent(matches::add);
		if (matches.isEmpty()) {
			findBestMatch(scorecard, possibleMatchesWhite, matches, 3).ifPresent(matches::add);
			if (matches.isEmpty()) {
				findBestMatch(scorecard, possibleMatchesColored, matches, 3).ifPresent(matches::add);
			}
		}
		return matches;
	}

	@Override
	public void notifyOnTurn(Player player) {
		System.out.printf("%s rolls the dice%n", player);
	}

	@Override
	public void notifyOnMiss(int nrOfMisses) {
		System.out.printf("Number of misses: %d%n", nrOfMisses);
	}
}
