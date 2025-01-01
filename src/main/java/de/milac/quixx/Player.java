package de.milac.quixx;

import de.milac.quixx.dice.DiceCup;
import de.milac.quixx.strategy.SimpleStrategy;
import de.milac.quixx.strategy.Strategy;

public class Player {
	private final String name;
	private final Scorecard scorecard;

	public Player(String name) {
		this(name, new SimpleStrategy());
	}

	public Player(String name, Strategy strategy) {
		this.name = name;
		this.scorecard = new Scorecard(strategy);
	}

	@Override
	public String toString() {
		return name;
	}

	public Scorecard getScorecard() {
		return scorecard;
	}

	public void matchOnTurn(DiceCup diceCup) {
		scorecard.matchOnTurn(diceCup);
	}

	public void match(DiceCup diceCup) {
		scorecard.match(diceCup);
	}
}
