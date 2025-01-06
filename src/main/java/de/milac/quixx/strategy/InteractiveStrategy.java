package de.milac.quixx.strategy;

import de.milac.quixx.*;
import de.milac.quixx.Row.CheckResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InteractiveStrategy implements Strategy {
	public static final String REPEAT = "x";

	private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public Optional<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite) {
		System.out.println(scorecard);
		List<Cell> possibleCells = filterForPossibleCells(possibleMatchesWhite);
		System.out.printf("Possible matches for WHITE dice: %s%n", possibleCells);
		return requestInput("WHITE", possibleCells, scorecard, List.of());
	}

	@Override
	public List<Cell> bestMatches(Scorecard scorecard, List<MatchResult> possibleMatchesWhite, List<MatchResult> possibleMatchesColored) {
		System.out.println(scorecard);
		List<Cell> possibleCellsWhite = filterForPossibleCells(possibleMatchesWhite);
		System.out.printf("Possible matches for WHITE dice: %s%n", possibleCellsWhite);
		List<Cell> possibleCellsColored = filterForPossibleCells(possibleMatchesColored);
		System.out.printf("Possible matches for COLORED dice: %s%n", possibleCellsColored);

		List<Cell> chosenCells = new ArrayList<>();
		boolean repeatInput = true;
		while (repeatInput) {
			try {
				requestInput("WHITE", possibleCellsWhite, scorecard, chosenCells, false).ifPresent(chosenCells::add);
				requestInput("COLORED", possibleCellsColored, scorecard, chosenCells, true).ifPresent(chosenCells::add);
				repeatInput = false;
			} catch (InputRepeatException e) {
				chosenCells.clear();
			}
		}
		return chosenCells;
	}

	@Override
	public void notifyOnTurn(Player player) {
		System.out.printf("Please press <Enter> to roll the dice, %s%n", player);
		requestInput();
	}

	@Override
	public void notifyOnMiss(int nrOfMisses) {
		System.out.printf("You've got %d misses now! Press <Enter> to continue%n", nrOfMisses);
		requestInput();
	}

	private String requestInput() {
		try {
			return requestInput(false);
		} catch (InputRepeatException e) {
			throw new RuntimeException(e);
		}
	}

	private String requestInput(boolean allowRepeat) throws InputRepeatException {
		String input;
		try {
			input = reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (allowRepeat && REPEAT.equalsIgnoreCase(input)) {
			throw new InputRepeatException("User wants to repeat last choice");
		}
		return input;
	}

	private Optional<Cell> requestInput(String type, List<Cell> possibleCells, Scorecard scorecard, List<Cell> chosenCells) {
		try {
			return requestInput(type, possibleCells, scorecard, chosenCells, false);
		} catch (InputRepeatException e) {
			throw new RuntimeException(e);
		}
	}

	private Optional<Cell> requestInput(String type, List<Cell> possibleCells, Scorecard scorecard, List<Cell> chosenCells, boolean allowRepeat) throws InputRepeatException {
		Optional<Cell> chosenCell = Optional.empty();
		if (!possibleCells.isEmpty()) {
			System.out.printf("Choose %s match or press <Enter> to skip %s%s:%n", type, type, allowRepeat ? ". Type '" + REPEAT + "' to repeat last choice" : "");
			boolean inputRequired = true;
			while (inputRequired) {
				try {
					chosenCell = validateInput(type, requestInput(allowRepeat), possibleCells, scorecard, chosenCells);
					inputRequired = false;
				} catch (ValidationException e) {
					System.out.printf("%s%nPlease try again%n", e.getMessage());
				}
			}
		}
		System.out.println("Your choice: " + chosenCell.map(Cell::toString).orElse("skipped"));
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
			List<String> failureReasons = new ArrayList<>();
			CheckResult checkResult = scorecard.canCheck(identifiedCell.orElseThrow());
			if (!checkResult.isPassed()) {
				failureReasons.add(checkResult.getFailureReason());
			}
			if (chosenCells.stream().anyMatch(c -> c.isAfter(identifiedCell.orElseThrow()))) {
				failureReasons.add("Cell is before latest checked cell");
			}
			if (!failureReasons.isEmpty()) {
				throw new ValidationException(String.format("Cell %s can't be checked: %s",
					identifiedCell.orElseThrow(), String.join(", ", failureReasons)));
			}
		}
		return identifiedCell;
	}

	private static class ValidationException extends Throwable {
		public ValidationException(String message) {
			super(message);
		}
	}

	private static class InputRepeatException extends Throwable {
		public InputRepeatException(String message) {
			super(message);
		}
	}
}
