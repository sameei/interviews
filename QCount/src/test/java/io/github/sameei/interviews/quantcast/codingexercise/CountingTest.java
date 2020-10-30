package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.*;
import io.github.sameei.interviews.quantcast.codingexercise.framework.BadDataFormatException;
import org.apache.commons.csv.CSVFormat;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CountingTest {

    DataSetDynamic dataset = new DataSetDynamic();

    @Test
    public void collectAll() throws IOException {

        DataSetDynamic.GeneratedSet gs =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        File file = dataset.toTempFile(gs);

        List<Pair<String, OffsetDateTime>> la =
                Loader.lazy(file, CSVFormat.DEFAULT).map(dataset.parser::parseCsvRecord).collect();

        List<Pair<String, OffsetDateTime>> lb =
                Loader.allSortedLinesDesc(file, dataset.parser, false).iterableOverAll().collect();

        assertThat(lb).containsExactlyInAnyOrderElementsOf(la);
    }

    @Test
    public void countOneDay() throws IOException {

        DataSetDynamic.GeneratedSet gs =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        File file = dataset.toTempFile(gs);

        List<Pair<String, Integer>> la =
                new CountingWithLazyIteration(CSVFormat.DEFAULT, dataset.parser, true)
                        .apply(file, dataset.dt(6, 20));

        System.out.println(la);

        List<Pair<String, Integer>> lb =
                new CountingWithSortedLinesDesc(dataset.parser, false)
                    .apply(file, dataset.dt(6, 20));

        System.out.println(lb);

        assertThat(lb).containsExactlyInAnyOrderElementsOf(la);

        List<Pair<String, Integer>> lc =
                new CountingSimply(dataset.parser, false, true)
                .apply(file, dataset.dt(6, 20));

        System.out.println(lc);

        assertThat(lc).containsExactlyInAnyOrderElementsOf(la);
    }

    @Test
    public void countLazyHandlingBadData() throws IOException {

        DataSetDynamic.GeneratedSet gs =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        File file = dataset.toTempFile(gs, true);


        assertThatExceptionOfType(BadDataFormatException.class)
                .isThrownBy(() -> {
                    new CountingWithLazyIteration(CSVFormat.DEFAULT, dataset.parser, true)
                            .apply(file, dataset.dt(6, 20));
                });

        assertThatNoException()
                .isThrownBy(() -> {
                    new CountingWithLazyIteration(CSVFormat.DEFAULT, dataset.parser, false)
                            .apply(file, dataset.dt(6, 20));
                });
    }

    @Test
    public void countSimplyNotHandlingBadData() throws IOException {

        DataSetDynamic.GeneratedSet gs =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        File file = dataset.toTempFile(gs, true);


        assertThatExceptionOfType(BadDataFormatException.class)
                .isThrownBy(() -> {
                    new CountingSimply(dataset.parser, false, true)
                            .apply(file, dataset.dt(6, 20));
                });

        assertThatExceptionOfType(BadDataFormatException.class)
                .isThrownBy(() -> {
                    new CountingSimply(dataset.parser, false, false)
                            .apply(file, dataset.dt(6, 20));
                });
    }

    @Test
    public void countSortedLinesDescNotHandlingBadData() throws IOException {

        DataSetDynamic.GeneratedSet gs =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        File file = dataset.toTempFile(gs, true);


        assertThatExceptionOfType(BadDataFormatException.class)
                .isThrownBy(() -> {
                    new CountingWithSortedLinesDesc(dataset.parser, false)
                            .apply(file, dataset.dt(6, 20));
                });

        assertThatExceptionOfType(BadDataFormatException.class)
                .isThrownBy(() -> {
                    new CountingWithSortedLinesDesc(dataset.parser, false)
                            .apply(file, dataset.dt(6, 20));
                });
    }

}
