package de.milac.quixx.event;

public interface EventSource {
	default void fire(Event event) {
		Events.fire(event, this);
	}
}
