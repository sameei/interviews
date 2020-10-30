package io.github.sameei.interviews.quantcast.codingexercise.launcher;

import io.github.sameei.interviews.quantcast.codingexercise.counting.CountingSimply;
import io.github.sameei.interviews.quantcast.codingexercise.counting.CountingWithLazyIteration;
import io.github.sameei.interviews.quantcast.codingexercise.counting.CountingWithSortedLinesDesc;
import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import org.apache.commons.csv.CSVFormat;
import org.javatuples.Pair;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class CountingEngines {

    public static CountingEngine getEngine(CountingEngine.Type type, DataParser parser) {
        switch (type) {
            case LAZY: return engineLazy(parser);
            case WEIRED: return engineWeired(parser);
            case SIMPLE:
            default:
                return engineSimple(parser);
        }
    }

    protected static CountingEngine engineSimple(DataParser parser) {
        return new CountingEngine() {
            @Override
            public List<Pair<String, Integer>> apply(
                    Path file, LocalDate date, boolean fileHasHeader, boolean failfast) {
                return new CountingSimply(parser, fileHasHeader, failfast)
                        .apply(file.toFile(), date);
            }
        };
    }

    protected static CountingEngine engineLazy(DataParser parser) {
        return new CountingEngine() {
            @Override
            public List<Pair<String, Integer>> apply(Path file, LocalDate date, boolean fileHasHeader, boolean failfast) {
                CSVFormat csvFormat = CSVFormat.DEFAULT;
                if (fileHasHeader) csvFormat = csvFormat.withFirstRecordAsHeader();
                return new CountingWithLazyIteration(csvFormat, parser, failfast).apply(file.toFile(),
                        date);
            }
        };
    }

    protected static CountingEngine engineWeired(DataParser parser) {
        return new CountingEngine() {
            @Override
            public List<Pair<String, Integer>> apply(Path file, LocalDate date, boolean fileHasHeader, boolean failfast) {
                return new CountingWithSortedLinesDesc(parser, fileHasHeader)
                        .apply(file.toFile(), date);
            }
        };
    }

}
