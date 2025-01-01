package de.milac.quixx.dice;

import de.milac.quixx.Color;

import java.util.*;

public class PredictableNumberGenerator implements NumberGenerator {
	private final Map<Color, Iterator<Integer>> iterators;

	private PredictableNumberGenerator(Map<Color, Iterator<Integer>> iterators) {
		this.iterators = iterators;
	}

	@Override
	public int nextNumber(Color color) {
		return iterators.get(color).next();
	}

	public static Builder of(Color color, int... nrs) {
		return new Builder(color, nrs);
	}

	public static class Builder {
		private final Map<Color, List<Integer>> numbers = new HashMap<>();

		private Builder(Color color, int... nrs) {
			and(color, nrs);
		}

		public Builder and(Color color, int... nrs) {
			List<Integer> nrList = numbers.getOrDefault(color, new ArrayList<>());
			numbers.putIfAbsent(color, nrList);
			nrList.addAll(Arrays.stream(nrs).boxed().toList());
			return this;
		}

		public PredictableNumberGenerator build() {
			Map<Color, Iterator<Integer>> iter = new HashMap<>();
			numbers.forEach((c, l) -> iter.put(c, new CircularIterator<>(l)));
			return new PredictableNumberGenerator(iter);
		}
	}

	public static class CircularIterator<E> implements Iterator<E> {
		private final List<E> elements;
		private int pos = 0;

		public CircularIterator(List<E> elements) {
			this.elements = elements;
		}

		@Override
		public boolean hasNext() {
			return true;
		}

		@Override
		public E next() {
			E element = elements.get(pos);
			if (++pos == elements.size()) {
				pos = 0;
			}
			return element;
		}
	}
}
