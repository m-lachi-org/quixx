package de.milac.quixx.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Events {
	private static final Map<Class<? extends Event>, List<EventHandler>> REGISTRY = new HashMap<>();

	public static void registerFor(Class<? extends Event> eventType, EventHandler listener) {
		REGISTRY.putIfAbsent(eventType, new ArrayList<>());
		REGISTRY.get(eventType).add(listener);
	}

	public static void fire(Event event, EventSource source) {
		List<EventHandler> listeners = REGISTRY.getOrDefault(event.getClass(), List.of());
		for (EventHandler listener : listeners) {
			if (!source.equals(listener)) {
				listener.handleEvent(event);
			}
		}
	}
}
