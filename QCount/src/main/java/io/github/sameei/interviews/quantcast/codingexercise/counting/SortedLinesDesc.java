package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import io.github.sameei.interviews.quantcast.codingexercise.framework.IterableRich;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SortedLinesDesc {

    private final ArrayList<String> lines;
    private final DataParser dataParser;

    public SortedLinesDesc(ArrayList<String> lines, DataParser dataParser) {
        this.lines = lines;
        this.dataParser = dataParser;
    }



    public IterableRich<Pair<String, OffsetDateTime>> iterableOverAll() {
        return new IterableRich<>(lines).map(dataParser::parseLine);
    }

    public IterableRich<Pair<String, OffsetDateTime>> iterable(LocalDate date) {
        int startingPoint = findStartingPoint(date);
        if (startingPoint < 0) return new IterableRich<>(Collections.EMPTY_LIST);
        return new IterableRich<>(new SmartIterable(lines, dataParser, startingPoint, date));
    }

    public int findStartingPoint(LocalDate date) {
        return findStartingPoint(0, lines.size() - 1, date);
    }

    public Pair<String, OffsetDateTime> getParsed(int i) {
        return dataParser.parseLine(lines.get(i));
    }
    public String getLine(int i) {
        return lines.get(i);
    }

    public int size() {
        return lines.size();
    }

    public int findStartingPoint(int l, int r, LocalDate date) {

        OffsetDateTime lv = dataParser.parseLine(lines.get(l)).getValue1();
        LocalDate ld = lv.toLocalDate();
        if (date.isEqual(ld)) return l;

        int diff = r - l;
        if (diff == 0) return -1;
        if (diff == 1) {
            OffsetDateTime rv = dataParser.parseLine(lines.get(r)).getValue1();
            LocalDate rd = rv.toLocalDate();
            if (date.isEqual(rd)) return r;
            return -1;
        }

        int mi = (l + r) / 2;
        OffsetDateTime mv = null;
        mv = dataParser.parseLine(lines.get(mi)).getValue1();
        LocalDate md = mv.toLocalDate();

        if (date.isBefore(md)) {
            return findStartingPoint(mi, r, date);
        }
        if (date.isAfter(md)) {
            return findStartingPoint(l, mi, date);
        }

        return findStartingPoint(l, mi, date);
    }

    public static class SmartIterator implements Iterator<Pair<String, OffsetDateTime>> {
        private final ArrayList<String> underlay;
        private final DataParser parser;
        private final int startingPoint;
        private final LocalDate demandedDate;

        private int current;
        Pair<String, OffsetDateTime> bufferedItem;

        public SmartIterator(ArrayList<String> underlay, DataParser parser, int startingPoint, LocalDate demandedDate) {
            this.underlay = underlay;
            this.parser = parser;
            this.startingPoint = startingPoint;
            this.demandedDate = demandedDate;
            current = startingPoint;
        }

        @Override
        public boolean hasNext() {
            if (current > underlay.size()) return false;
            bufferedItem = parser.parseLine(underlay.get(current));
            return bufferedItem.getValue1().toLocalDate().isEqual(demandedDate);
        }

        @Override
        public Pair<String, OffsetDateTime> next() {
            if (bufferedItem == null) throw new IllegalStateException("End of Iteration");
            Pair<String, OffsetDateTime> tmp = bufferedItem;
            bufferedItem = null;
            current++;
            return tmp;
        }
    }

    public class SmartIterable implements Iterable<Pair<String, OffsetDateTime>> {
        private final ArrayList<String> underlay;
        private final DataParser parser;
        private final int startingPoint;
        private final LocalDate demandedDate;

        public SmartIterable(ArrayList<String> underlay, DataParser parser, int startingPoint, LocalDate demandedDate) {
            this.underlay = underlay;
            this.parser = parser;
            this.startingPoint = startingPoint;
            this.demandedDate = demandedDate;
        }

        @Override
        public Iterator<Pair<String, OffsetDateTime>> iterator() {
            return new SmartIterator(underlay, parser, startingPoint, demandedDate);
        }
    }

}
