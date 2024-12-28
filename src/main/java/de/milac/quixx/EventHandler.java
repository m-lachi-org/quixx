package de.milac.quixx;

import java.util.List;

public abstract class EventHandler {
	EventHandler() {
		eventsOfInterest().forEach(c -> Events.registerFor(c, this));
	}
	abstract List<Class<? extends Event>> eventsOfInterest();
	abstract void handleEvent(Event event);
}
