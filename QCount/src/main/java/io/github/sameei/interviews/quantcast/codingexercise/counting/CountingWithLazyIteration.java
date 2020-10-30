package io.github.sameei.interviews.quantcast.codingexercise.counting;

import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import io.github.sameei.interviews.quantcast.codingexercise.framework.Counting;
import io.github.sameei.interviews.quantcast.codingexercise.framework.IterableRich;
import io.github.sameei.interviews.quantcast.codingexercise.framework.OverEngineered;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ContextDefaultImpl;
import io.github.sameei.interviews.quantcast.codingexercise.frameworkimpl.ServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class CountingWithLazyIteration implements Counting {

    private final Logger LOG = LoggerFactory.getLogger(CountingWithLazyIteration.class);

    private final CSVFormat csvFormat;
    private final DataParser dataParser;
    private final boolean failFast;
    private final ZoneOffset utc = ZoneOffset.of("+00:00");

    public CountingWithLazyIteration(CSVFormat csvFormat, DataParser dataParser, boolean failFast) {
        this.csvFormat = csvFormat;
        this.dataParser = dataParser;
        this.failFast = failFast;

        LOG.debug("Instantiated with {} & {}", csvFormat, dataParser);
    }

    public CountingWithLazyIteration(CSVFormat csvFormat, DataParser dataParser) {
        this(csvFormat, dataParser, true);
    }

    public static CountingWithLazyIteration withDefaults() {
        return new CountingWithLazyIteration(
                CSVFormat.DEFAULT.withFirstRecordAsHeader(),
                DataParser.withDefaults());
    }

    @Override
    public List<Pair<String, Integer>> apply(File file, LocalDate date) {

        try {

            IterableRich<CSVRecord> records =
                    Loader.lazy(file, csvFormat);

            IterableRich<Pair<String, OffsetDateTime>> list =
                    failFast
                            ? records.map(dataParser::parseCsvRecord)
                            : records.mapOrDrop(i -> {
                        try {
                            return dataParser.parseCsvRecord(i);
                        } catch (BadDataFormatException cause) {
                            LOG.warn("SKIPPED_MALFORMED_RECORD: {}", i);
                            return null;
                        }
                    });

            OverEngineered.Service<Pair<String, OffsetDateTime>, Counter> service =
                    ServiceImpl.instance();

            OverEngineered.Result<Counter> result = service.run(list.iterable(),
                    ContextDefaultImpl.supplier(new Counter()),
                    (pair, ctx) -> {

                        boolean inGivenDate =
                                pair.getValue1().toLocalDate().isEqual(date);

                        if (inGivenDate)
                            ctx.state().inc(pair.getValue0());
                    });

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
