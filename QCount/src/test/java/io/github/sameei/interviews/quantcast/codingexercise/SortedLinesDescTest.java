package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.SortedLinesDesc;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SortedLinesDescTest {

    DataSetDynamic dataset = new DataSetDynamic();

    @Test
    public void genData() {
        assertThat(dataset.generate(6, 4, 30, 1, 20, 10, 100).records).hasSize(3 * 30 * 11);
    }

    public void searchInTheMiddle(SortedLinesDesc lines, int m, int d) {
        int sp = lines.findStartingPoint(dataset.dt(m,d));
        assertThat(lines.getLine(sp-1)).endsWith(dataset.stringDT(m, d + 1, 10));
        assertThat(lines.getLine(sp)).endsWith(dataset.stringDT(m, d, 20));
        assertThat(lines.getLine(sp+1)).endsWith(dataset.stringDT(m, d, 19));
    }

    @Test
    public void searchToFind() throws IOException {

        DataSetDynamic.GeneratedSet ds =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        SortedLinesDesc lines = new SortedLinesDesc(ds.lines, dataset.parser);

        searchInTheMiddle(lines, 6, 28);
        searchInTheMiddle(lines, 6, 14);


        int sp = lines.findStartingPoint(dataset.dt(6,30));
        assertThat(sp).isEqualTo(0);
        assertThat(ds.lines.get(sp)).endsWith(dataset.stringDT(6, 30, 20));
        assertThat(ds.lines.get(sp+1)).endsWith(dataset.stringDT(6, 30, 19));
    }

    @Test
    public void iterate() throws Exception {

        DataSetDynamic.GeneratedSet ds =
                dataset.generate(6, 4, 30, 1, 20, 10, 100);

        SortedLinesDesc lines = new SortedLinesDesc(ds.lines, dataset.parser);

        List<Pair<String, OffsetDateTime>> la = lines.iterable(dataset.dt(6, 30)).collect();
        assertThat(la).hasSize(11);
    }

}
