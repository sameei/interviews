package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.Iterator;

public class IterMapOrDrop<T,R> implements Iterator<R> {

    @FunctionalInterface
    public static interface Fn<T,R> {
        R map(T t);
    }

    private final Iterator<T> underlay;
    private final Fn<T,R> fn;
    private R bufferedElm;

    public IterMapOrDrop(Iterator<T> underlay, Fn<T,R> fn) {
        this.fn = fn;
        this.underlay = underlay;
    }

    @Override
    public boolean hasNext() {
        while(underlay.hasNext()) {
            T tmp = underlay.next();
            bufferedElm = fn.map(tmp);
            if (bufferedElm != null) return true;
        }
        bufferedElm = null;
        return false;
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
                return new IterMapOrDrop<>(iterable.iterator(), fn);
            }
        };
    }

}
