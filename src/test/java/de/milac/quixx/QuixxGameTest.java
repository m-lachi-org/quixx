package de.milac.quixx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QuixxGameTest {
	public static final Player PLAYER_1 = new Player("Player1");
	public static final Player PLAYER_2 = new Player("Player2");
	public static final Player PLAYER_3 = new Player("Player3");

	@Test
	void nextRoundWithoutPlayers() {
		QuixxGame game = new QuixxGame();

		assertThatThrownBy(game::nextRound).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void nextRoundOnePlayer() {
		QuixxGame game = new QuixxGame();
		game.join(PLAYER_1);

		assertThatThrownBy(game::nextRound).isInstanceOf(IllegalStateException.class);
	}

	@Test
	void nextRoundMorePlayers() {
		QuixxGame game = new QuixxGame();
		game.join(PLAYER_1, PLAYER_2);
		assertThat(game.getPlayers()).hasSize(2);

		Round round1 = game.nextRound();

		assertThat(round1).isNotNull();
		assertThat(round1.getPlayer()).isEqualTo(game.getCurrentPlayer()==0 ? PLAYER_1 : PLAYER_2);
		assertThat(round1.getCoPlayers()).containsExactly(game.getCurrentPlayer()==0 ? PLAYER_2 : PLAYER_1);

		Round round2 = game.nextRound();
		assertThat(round2).isNotNull();
		assertThat(round2.getPlayer()).isEqualTo(game.getCurrentPlayer()==0 ? PLAYER_1 : PLAYER_2);
		assertThat(round2.getCoPlayers()).containsExactly(game.getCurrentPlayer()==0 ? PLAYER_2 : PLAYER_1);
	}

	@Test
	void coPlayersOf() {
		QuixxGame game = new QuixxGame();
		game.join(PLAYER_1, PLAYER_2, PLAYER_3);

		assertThat(game.coPlayersOf(PLAYER_1)).containsExactly(PLAYER_2, PLAYER_3);
		assertThat(game.coPlayersOf(PLAYER_2)).containsExactly(PLAYER_3, PLAYER_1);
		assertThat(game.coPlayersOf(PLAYER_3)).containsExactly(PLAYER_1, PLAYER_2);
	}
}