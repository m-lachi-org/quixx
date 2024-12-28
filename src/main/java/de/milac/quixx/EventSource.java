package de.milac.quixx;

public interface EventSource {
	default void fire(Event event) {
		Events.fire(event, this);
	}
}
