package de.milac.quixx.event;

import java.util.List;

public abstract class EventHandler {
	public EventHandler() {
		eventsOfInterest().forEach(c -> Events.registerFor(c, this));
	}
	public abstract List<Class<? extends Event>> eventsOfInterest();
	public abstract void handleEvent(Event event);
}
