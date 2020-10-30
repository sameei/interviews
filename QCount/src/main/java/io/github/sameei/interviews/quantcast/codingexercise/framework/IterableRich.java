package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IterableRich<T> {

    private final Iterable<T> iterable;

    public IterableRich(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public Iterator<T> iterator() {
        return this.iterable.iterator();
    }

    public Iterable<T> iterable() {
        return this.iterable;
    }

    public IterableRich<T> filter(IterFilter.Fn<T> filter) {
        return new IterableRich<>(IterFilter.iterable(iterable, filter));
    }

    public <R> IterableRich<R> map(IterMap.Fn<T,R> map) {
        return new IterableRich(IterMap.<T,R>iterable(iterable, map));
    }

    public <R> IterableRich<R> mapOrDrop(IterMapOrDrop.Fn<T,R> fn) {
        return new IterableRich<>(IterMapOrDrop.iterable(iterable, fn));
    }

    public List<T> collect() {
        ArrayList<T> buffer = new ArrayList<>();
        for(T t: iterable()) buffer.add(t);
        return buffer;
    }
}
