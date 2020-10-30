package io.github.sameei.interviews.quantcast.codingexercise.framework;

import java.util.Iterator;

public class IterFilter<E> implements Iterator<E> {

    @FunctionalInterface
    public static interface Fn<E> {
        boolean filter(E e);
    }

    private final Iterator<E> underlay;
    private final Fn<E> fn;
    private E bufferedElm;

    public IterFilter(Iterator<E> underlay, Fn<E> fn) {
        this.fn = fn;
        this.underlay = underlay;
    }

    @Override
    public boolean hasNext() {
        if (!underlay.hasNext())
            return false;

        bufferedElm = underlay.next();
        if (!fn.filter(bufferedElm)) {
            bufferedElm = null;
            return false;
        }

        return true;
    }

    @Override
    public E next() {
        if (bufferedElm == null) throw new IllegalStateException("End Of Iterator");
        return bufferedElm;
    }

    public static <E> Iterable<E> iterable(Iterable<E> iterable, Fn<E> fn) {
        return new Iterable<E>() {
            @Override
            public Iterator<E> iterator() {
                return new IterFilter<>(iterable.iterator(), fn);
            }
        };
    }
}
