package de.milac.quixx;

import de.milac.quixx.strategy.InteractiveStrategy;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

public class QuixxGame {
	private final List<Player> players = new LinkedList<>();
	private int currentPlayer = -1;

	public static void main(String[] args) {
		QuixxGame game = new QuixxGame();
		game.join(
			new Player("Alexandra", new InteractiveStrategy())
			, new Player("Michael", new InteractiveStrategy())
//			, new Player("Leonie")
//			, new Player("Julian")
		);
		Round round = game.start();
		while (round.play()) {
			round = game.nextRound();
		}
		game.score();
	}

	private void score() {
		Map<Score, Player> scores = new HashMap<>();
		for (Player player : players) {
			Score score = player.getScorecard().getScore();
			Optional.ofNullable(scores.put(score, player)).ifPresent(p -> {
				throw new IllegalStateException(String.format("Score of %s replaced by %s: %s", p, player, score));
			});
		}
		AtomicInteger i = new AtomicInteger(0);
		scores.entrySet().stream().sorted(Map.Entry.<Score, Player>comparingByKey().reversed())
			.forEach(e -> System.out.printf("%s: %s%s%n", e.getValue(), e.getKey(), i.getAndIncrement() == 0 ? " <-- winner after " + Round.getCount() + " rounds!" : ""));
	}

	private Round start() {
		return nextRound();
	}

	Round nextRound() {
		if (players.isEmpty()) {
			throw new IllegalStateException("No players have joined yet");
		} else if (players.size() < 2) {
			throw new IllegalStateException("At least two players must join");
		}
		if (currentPlayer < 0 ) {
			System.out.printf("Starting new game with players %s%n", players);
			currentPlayer = RandomGenerator.getDefault().nextInt(players.size());
		} else {
			currentPlayer = currentPlayer == players.size()-1 ? 0 : currentPlayer + 1;
		}

		Player roundPlayer = players.get(currentPlayer);
		return new Round(roundPlayer, coPlayersOf(roundPlayer));
	}

	List<Player> coPlayersOf(Player roundPlayer) {
		List<Player> coPlayers = new LinkedList<>();
		int idx = players.indexOf(roundPlayer);
		int count = 1;
		while (count < players.size()) {
			idx = (idx == players.size() - 1) ? 0 : idx + 1;
			coPlayers.add(players.get(idx));
			count++;
		}
		return coPlayers;
	}

	void join(Player... playersToJoin) {
		players.addAll(Arrays.asList(playersToJoin));
	}

	List<Player> getPlayers() {
		return players;
	}

	int getCurrentPlayer() {
		return currentPlayer;
	}
}
