package de.milac.quixx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoundTest {
	@Mock
	Player player1;
	@Mock
	Player player2;
	@Mock
	Scorecard scorecard1;

	@BeforeEach
	public void setUp() {
		Round.reset();
		lenient().when(player1.toString()).thenReturn("Player1");
		lenient().when(player2.toString()).thenReturn("Player2");
	}

	@Test
	void getCount() {
		Round round1 = new Round(player1, List.of(player2));
		assertThat(round1).isNotNull();
		assertThat(Round.getCount()).isEqualTo(1);
		Round round2 = new Round(player1, List.of(player2));
		assertThat(round2).isNotNull();
		assertThat(Round.getCount()).isEqualTo(2);

		Round.reset();

		round1 = new Round(player1, List.of(player2));
		assertThat(round1).isNotNull();
		assertThat(Round.getCount()).isEqualTo(1);

	}

	@Test
	void gameOverAtLeast2RowsClosed() {
		when(player1.getScorecard()).thenReturn(scorecard1);
		when(scorecard1.getNrOfClosedRows()).thenReturn(0L, 1L, 2L);
		Round round1 = new Round(player1, List.of(player2));
		assertThat(round1.gameOver()).isFalse();
		Round round2 = new Round(player1, List.of(player2));
		assertThat(round2.gameOver()).isFalse();
		Round round3 = new Round(player1, List.of(player2));
		assertThat(round3.gameOver()).isTrue();
	}

	@Test
	void gameOverMaxMissesReached() {
		when(player1.getScorecard()).thenReturn(scorecard1);
		when(scorecard1.getNrOfMisses()).thenReturn(2, 3, 4);
		Round round1 = new Round(player1, List.of(player2));
		assertThat(round1.gameOver()).isFalse();
		Round round2 = new Round(player1, List.of(player2));
		assertThat(round2.gameOver()).isFalse();
		Round round3 = new Round(player1, List.of(player2));
		assertThat(round3.gameOver()).isTrue();
	}
}