package io.github.sameei.interviews.quantcast.codingexercise.counting;

import java.util.ArrayList;
import java.util.Iterator;

@Deprecated
public class ReverseIter<T> implements Iterable<T> {

    private final ArrayList<T> underlay;
    private final int startPoint;

    public ReverseIter(ArrayList<T> underlay, int startPoint) {
        this.underlay = underlay;
        this.startPoint = startPoint;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private int pt = startPoint;

            @Override
            public boolean hasNext() {
                return pt >= 0;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new IllegalStateException("Out of Index");
                T value = underlay.get(pt);
                pt--;
                return value;
            }
        };
    }
}
