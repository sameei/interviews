package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.Iterator;

public class IterMap<T,R> implements Iterator<R> {

    @FunctionalInterface
    public static interface Fn<T,R> {
        R map(T t);
    }

    private final Iterator<T> underlay;
    private final Fn<T, R> fn;
    private R bufferedElm;

    public IterMap(Iterator<T> underlay, Fn<T, R> fn) {
        this.underlay = underlay;
        this.fn = fn;
    }



    @Override
    public boolean hasNext() {
        if (!underlay.hasNext())
            return false;

        bufferedElm = fn.map(underlay.next());
        return true;
    }

    @Override
    public R next() {
        if (bufferedElm == null) throw new IllegalStateException("End Of Iterator");
        return bufferedElm;
    }


    public static <T,R> Iterable<R> iterable(Iterable<T> iterable, Fn<T,R> fn) {
        return new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                return new IterMap<>(iterable.iterator(), fn);
            }
        };
    }
}
