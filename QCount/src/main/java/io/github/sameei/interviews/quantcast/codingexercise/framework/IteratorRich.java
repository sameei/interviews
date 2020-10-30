package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.Iterator;

public class IteratorRich<T> {

    private final Iterator<T> iterator;

    public IteratorRich(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public Iterator<T> iterator() {
        return this.iterator;
    }

    public IteratorRich<T> filter(IterFilter.Fn<T> filter) {
        return new IteratorRich<>(new IterFilter<>(iterator, filter));
    }

    public <R> IteratorRich<R> map(IterMap.Fn<T,R> map) {
        return new IteratorRich<>(new IterMap<>(iterator, map));
    }

    public <R> IteratorRich<R> mapOrDrop(IterMapOrDrop.Fn<T,R> fn) {
        return new IteratorRich<R>(new IterMapOrDrop<T,R>(iterator, fn));
    }
}
