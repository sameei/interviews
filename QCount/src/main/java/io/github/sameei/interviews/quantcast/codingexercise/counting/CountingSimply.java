package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import io.github.sameei.interviews.quantcast.codingexercise.framework.Counting;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

public class CountingSimply implements Counting {

    private final Logger LOG = LoggerFactory.getLogger(CountingSimply.class);

    private final DataParser dataParser;
    private final boolean skipFirstLine;
    private final boolean failfast;
    private final ZoneOffset utc = ZoneOffset.of("+00:00");

    public CountingSimply(DataParser dataParser, boolean skipFirstLine, boolean failfast) {
        this.dataParser = dataParser;
        this.skipFirstLine = skipFirstLine;
        this.failfast = failfast;
        LOG.debug("Instantiated with {}, SkipFirstLine: {}, FailFast: {}",
                dataParser, skipFirstLine, failfast);
        LOG.warn("This Counting Class supports FailFast=false only to some degree!" +
                "This means that this Class might not be able process the the input " +
                "in some cases.");
    }

    public CountingSimply withDefault() {
        return new CountingSimply(DataParser.withDefaults(), true, failfast);
    }

    @Override
    public List<Pair<String, Integer>> apply(File file, LocalDate date) {
        try {
            SortedLinesDesc lines =
                    Loader.allSortedLinesDesc(file, dataParser, skipFirstLine);

            int startingPoint = lines.findStartingPoint(date);
            Counter counter = new Counter();

            if (startingPoint < 0) return Collections.EMPTY_LIST;

            Pair<String, OffsetDateTime> e = null;
            for (int i = startingPoint; i < lines.size(); i++) {

                try {
                    e = lines.getParsed(i);
                } catch (BadDataFormatException cause) {
                    if (failfast) throw cause;
                    LOG.warn("SKIPPED_MALFORMED_RECORD: {}", lines.getLine(i));
                }

                if (!e.getValue1().toLocalDate().isEqual(date)) break;
                counter.inc(e.getValue0());
            }

            return counter.onlyMaximums();
        } catch (IOException cause) {
            throw new RuntimeException(cause);
        }
    }
}
