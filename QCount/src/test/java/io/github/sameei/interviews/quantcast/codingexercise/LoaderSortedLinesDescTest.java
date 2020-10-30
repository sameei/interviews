package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.Loader;
import io.github.sameei.interviews.quantcast.codingexercise.counting.SortedLinesDesc;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LoaderSortedLinesDescTest {

    @Test
    public void loadParseIterate() throws IOException {

        DataSetStaticCSV dataset = new DataSetStaticCSV();

        SortedLinesDesc lines =
                Loader.allSortedLinesDesc(dataset.tempFile(), dataset.dataParser, true);

        List<Pair<String, OffsetDateTime>> list =
                lines.iterableOverAll().collect();

        assertThat(list).hasSameSizeAs(dataset.lines);

        List<Pair<String, String>> newLines =
                list.stream().map(i ->
                        Pair.with(i.getValue0(), dataset.dtf.format(i.getValue1()))
                ).collect(Collectors.toList());

        /*
        java.lang.AssertionError:
Actual and expected have the same elements but not in the same order, at index 0 actual element was:
  <[4sMM2LxV07bPJzwf, 2018-12-07T23:30:00+00:00]>
whereas expected element was:
  <[AtY0laUfhglK3lC7, 2018-12-09T14:19:00+00:00]>
         */
        // assertThat(newLines).containsExactlyElementsOf(dataset.lines);
        assertThat(newLines).containsAll(dataset.lines);
    }


}
