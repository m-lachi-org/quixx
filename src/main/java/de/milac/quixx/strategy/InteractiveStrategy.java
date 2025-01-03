package de.milac.quixx.strategy;

import de.milac.quixx.Cell;
import de.milac.quixx.MatchResult;
import de.milac.quixx.Player;
import de.milac.quixx.Scorecard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InteractiveStrategy implements Strategy {
	private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite) {
		System.out.println(scorecard);
		List<Cell> possibleCells = filterForPossibleCells(possibleMatchesWhite);
		System.out.printf("Possible matches for WHITE dice: %s%n", possibleCells);
		return requestFromUser("WHITE", possibleCells, scorecard, List.of());
	}

	@Override
	public List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored) {
		List<Cell> chosenCells = new ArrayList<>();
		System.out.println(scorecard);
		List<Cell> possibleCellsWhite = filterForPossibleCells(possibleMatchesWhite);
		System.out.printf("Possible matches for WHITE dice: %s%n", possibleCellsWhite);
		List<Cell> possibleCellsColored = filterForPossibleCells(possibleMatchesColored);
		System.out.printf("Possible matches for COLORED dice: %s%n", possibleCellsColored);
		requestFromUser("WHITE", possibleCellsWhite, scorecard, chosenCells).ifPresent(chosenCells::add);
		requestFromUser("COLORED", possibleCellsColored, scorecard, chosenCells).ifPresent(chosenCells::add);
		return chosenCells;
	}

	@Override
	public void notifyOnTurn(Player player) {
		System.out.printf("Please press <Enter> to roll the dice, %s%n", player);
		requestUserInput();
	}

	private String requestUserInput() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private List<Cell> filterForPossibleCells(List<MatchResult> possibleMatchesWhite) {
		return possibleMatchesWhite.stream().filter(MatchResult::isPresent).map(mr -> mr.getMatch().orElseThrow()).toList();
	}

	private Optional<Cell> requestFromUser(String type, List<Cell> possibleCells, Scorecard scorecard, List<Cell> chosenCells) {
		Optional<Cell> chosenCell = Optional.empty();
		if (!possibleCells.isEmpty()) {
			System.out.printf("Choose %s match or press <Enter> to skip:%n", type);
			boolean inputRequired = true;
			while (inputRequired) {
				try {
					chosenCell = validateInput(type, requestUserInput(), possibleCells, scorecard, chosenCells);
					inputRequired = false;
				} catch (ValidationException e) {
					System.out.printf("%s%nPlease try again%n", e.getMessage());
				}
			}
		}
		return chosenCell;
	}

	private Optional<Cell> validateInput(String type, String userInput, List<Cell> possibleCells, Scorecard scorecard, List<Cell> chosenCells) throws ValidationException {
		if (userInput == null || userInput.isBlank()) {
			return Optional.empty();
		}
		Optional<Cell> identifiedCell = possibleCells.stream().filter(c -> c.matchById(userInput)).findFirst();
		if (identifiedCell.isEmpty()) {
			throw new ValidationException(String.format("No cell found in possible %s matches for input '%s'", type, userInput));
		} else {
			if (!scorecard.canCheck(identifiedCell.orElseThrow()) || chosenCells.stream().anyMatch(c -> c.isAfter(identifiedCell.orElseThrow()))) {
				throw new ValidationException(String.format("Cell %s can't be checked", identifiedCell.orElseThrow()));
			}
		}
		return identifiedCell;
	}

	private static class ValidationException extends Exception {
		public ValidationException(String message) {
			super(message);
		}
	}
}
