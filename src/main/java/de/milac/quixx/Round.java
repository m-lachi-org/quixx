package de.milac.quixx;

import de.milac.quixx.dice.DiceCup;

import java.util.List;

public class Round {
	public static final String LINE = "===================================";
	private static int count = 0;

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
		count = 0;
	}

	public boolean play() {
		System.out.printf("%n%n%n%s%nRound %d: %s has the turn%n", LINE, count, player);
		player.notifyOnTurn();
		DiceCup diceCup = DiceCup.shake(player.getScorecard().getActiveColors());
		System.out.printf("%s has rolled %s%n%s%n", player, diceCup, LINE);

		player.matchOnTurn(diceCup);
		System.out.println(player.getScorecard());

		for (Player coPlayer : coPlayers) {
			System.out.printf("%s%n%s%n%s%n", LINE, coPlayer, LINE);
			coPlayer.match(diceCup);
			System.out.println(coPlayer.getScorecard());
		}
		return !gameOver();
	}

	boolean gameOver() {
		boolean maxRowsClosed = player.getScorecard().getNrOfClosedRows() >= 2;
		boolean maxMissesReached = player.getScorecard().getNrOfMisses() == 4;
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
