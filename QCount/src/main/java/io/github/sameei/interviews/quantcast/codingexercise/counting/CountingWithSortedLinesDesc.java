package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import io.github.sameei.interviews.quantcast.codingexercise.framework.Counting;
import io.github.sameei.interviews.quantcast.codingexercise.framework.IterableRich;
import io.github.sameei.interviews.quantcast.codingexercise.framework.OverEngineered;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ContextDefaultImpl;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ServiceImpl;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CountingWithSortedLinesDesc implements Counting {

    private final Logger LOG = LoggerFactory.getLogger(CountingWithLazyIteration.class);

    private final DataParser dataParser;
    private final boolean skipFirstLine;
    private final ZoneOffset utc = ZoneOffset.of("+00:00");

    public CountingWithSortedLinesDesc(DataParser dataParser, boolean skipFirstLine) {
        this.dataParser = dataParser;
        this.skipFirstLine = skipFirstLine;

        LOG.debug("Instantiated with {} & SkipFirstLine: {}", dataParser, skipFirstLine);
        LOG.warn("This Counting Class only support failfast=true");
    }

    public CountingWithSortedLinesDesc withDefault() {
        return new CountingWithSortedLinesDesc(DataParser.withDefaults(), true);
    }

    @Override
    public List<Pair<String, Integer>> apply(File file, LocalDate date) {

        try {

            SortedLinesDesc lines =
                    Loader.allSortedLinesDesc(file, dataParser, skipFirstLine);

            IterableRich<Pair<String, OffsetDateTime>> list =
                    lines.iterable(date);

            OverEngineered.Service<Pair<String, OffsetDateTime>, Counter> service =
                    ServiceImpl.instance();

            OverEngineered.Result<Counter> result = service.run(list.iterable(),
                    ContextDefaultImpl.supplier(new Counter()),
                    (pair, ctx) -> ctx.state().inc(pair.getValue0()) );

            if (result.getFailCause().isPresent()) {
                Throwable cause = result.getFailCause().get();
                if (cause instanceof BadDataFormatException)
                    throw (BadDataFormatException) cause;
                throw new RuntimeException(cause);
            }

            return result.getTaskState().onlyMaximums();
        } catch (IOException cause) {
            throw new RuntimeException(cause);
        }

    }
}
