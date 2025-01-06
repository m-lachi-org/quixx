package de.milac.quixx;

import de.milac.quixx.dice.DiceCup;

import java.util.List;

public class Round {
	public static final String LINE = "===================================";
	private static final int INITIAL_ROUND = 0;

	private static int count = INITIAL_ROUND;

	private final Player player;
	private final List<Player> coPlayers;

	public Round(Player player, List<Player> coPlayers) {
		this.player = player;
		this.coPlayers = coPlayers;
		count++;
	}

	public static int getCount() {
		return count;
	}

	public static void reset() {
		count = INITIAL_ROUND;
	}

	public boolean play() {
		System.out.printf("%n%n%n%s%nRound %d:%n%s has the turn%n", LINE, count, player);
		player.notifyOnTurn();
		DiceCup diceCup = DiceCup.shake(player.getScorecard().getActiveColors());
		System.out.printf("%s has rolled %s%n%s%n", player, diceCup, LINE);

		player.matchOnTurn(diceCup);
		System.out.printf("%n%s%n", player.getScorecard());

		for (Player coPlayer : coPlayers) {
			System.out.printf("%s%n%s%n%s%n", LINE, coPlayer, LINE);
			coPlayer.match(diceCup);
			System.out.printf("%n%s%n", coPlayer.getScorecard());
		}
		return !gameOver();
	}

	boolean gameOver() {
		boolean maxRowsClosed = player.getScorecard().getNrOfClosedRows() >= Rules.MAX_CLOSED_ROWS_ALLOWED;
		boolean maxMissesReached = player.getScorecard().getNrOfMisses() == Rules.MAX_MISSES_ALLOWED;
		if (maxRowsClosed) {
			System.out.printf("Game over! %d closed rows!%n", player.getScorecard().getNrOfClosedRows());
		} else if (maxMissesReached) {
			System.out.printf("Game over! %d misses!%n", player.getScorecard().getNrOfMisses());
		}
		return maxRowsClosed || maxMissesReached;
	}

	Player getPlayer() {
		return player;
	}

	List<Player> getCoPlayers() {
		return coPlayers;
	}
}
