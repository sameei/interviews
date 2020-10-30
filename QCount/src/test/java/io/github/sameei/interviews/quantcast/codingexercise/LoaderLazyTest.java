package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.framework.IterableRich;
import io.github.sameei.interviews.quantcast.codingexercise.counting.Loader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class LoaderLazyTest {

    @Test
    public void loadAndParse() throws IOException {

        DataSetStaticCSV dataset = new DataSetStaticCSV();

        IterableRich<CSVRecord> iter = Loader.lazy(
                // new File("./data-sample/cookies.csv"),
                dataset.tempFile(),
                CSVFormat.DEFAULT.withFirstRecordAsHeader()
        );

        List<Pair<String, OffsetDateTime>> list =
                iter
                .map(dataset.dataParser::parseCsvRecord)
                .collect();

        assertThat(list).hasSameSizeAs(dataset.lines);

        List<Pair<String, String>> newLines =
                list.stream().map(i ->
                        Pair.with(i.getValue0(), dataset.dtf.format(i.getValue1()))
                ).collect(Collectors.toList());

        assertThat(newLines).containsExactlyElementsOf(dataset.lines);
    }

}
