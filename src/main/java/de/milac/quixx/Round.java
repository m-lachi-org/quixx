package de.milac.quixx;

import java.util.List;

public class Round {
	private static int count = 0;

	private final Player player;
	private final List<Player> coPlayers;

	public Round(Player player, List<Player> coPlayers) {
		this.player = player;
		this.coPlayers = coPlayers;
		count++;
		System.out.printf("%nRound %d: %s%n", count, player);
	}

	public static int getCount() {
		return count;
	}

	public boolean play() {
		DiceCup dices = DiceCup.shake(player.getScorecard().getActiveColors());
		System.out.printf("%s has rolled %s%n", player, dices);
		player.matchOnTurn(dices);
		System.out.println(player.getScorecard());
		for (Player coPlayer : coPlayers) {
			System.out.println(coPlayer);
			coPlayer.match(dices);
			System.out.println(coPlayer.getScorecard());
		}
		return !gameOver();
	}

	private boolean gameOver() {
		boolean maxRowsClosed = player.getScorecard().getNrOfClosedRows() >= 2;
		boolean maxMissesReached = player.getScorecard().getNrOfMisses() == 4;
		if (maxRowsClosed) {
			System.out.printf("Game over! %d closed rows!%n", player.getScorecard().getNrOfClosedRows());
		} else if (maxMissesReached) {
			System.out.printf("Game over! %d misses!%n", player.getScorecard().getNrOfMisses());
		}
		return maxRowsClosed || maxMissesReached;
	}
}
