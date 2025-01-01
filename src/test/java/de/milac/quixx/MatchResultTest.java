package de.milac.quixx;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MatchResultTest {

	@Test
	void isEmpty() {
		MatchResult matchResult = MatchResult.empty(Color.RED, 5);

		assertThat(matchResult.isEmpty()).isTrue();
		assertThat(matchResult.isPresent()).isFalse();
	}

	@Test
	void isPresent() {
		MatchResult matchResult = MatchResult.of(new Cell(Color.RED, 7, 9), Color.RED, 5);

		assertThat(matchResult.isEmpty()).isFalse();
		assertThat(matchResult.isPresent()).isTrue();
	}

	@Test
	void getDistanceToFirstActive() {
		MatchResult matchResult = MatchResult.of(new Cell(Color.RED, 7, 9), Color.RED, 5);

		assertThat(matchResult.getDistanceToFirstActive()).isEqualTo(2);
	}

	@Test
	void getDistanceToFirstActiveWhenEmpty() {
		MatchResult matchResult = MatchResult.empty(Color.RED, 5);
		assertThat(matchResult.getDistanceToFirstActive()).isEqualTo(-6);

		matchResult = MatchResult.empty(Color.YELLOW, 0);
		assertThat(matchResult.getDistanceToFirstActive()).isEqualTo(-1);
	}
}