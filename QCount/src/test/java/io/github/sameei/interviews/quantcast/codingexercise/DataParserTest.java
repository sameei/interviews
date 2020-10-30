package io.github.sameei.interviews.quantcast.codingexercise;

import io.github.sameei.interviews.quantcast.codingexercise.counting.DataParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.javatuples.Pair;
import org.junit.Test;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.assertj.core.api.Assertions.*;

public class DataParserTest {


    String dtOrigin = "2018-12-09T14:19:00+00:00";
    OffsetDateTime dtSample =
            OffsetDateTime.of(2018, 12, 9, 14, 19, 0, 0, ZoneOffset.of("+00:00"));
    String sampleCookie =
            "AtY0laUfhglK3lC7";
    String sampleLine = "AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00";

    @Test
    public void parseDateTime() {
        OffsetDateTime parsed = DataParser.withDefaults().parseDateTime(dtOrigin);
        assertThat(parsed).isEqualTo(dtSample);
    }

    @Test
    public void parseLine() {
        Pair<String, OffsetDateTime> pair = DataParser.withDefaults().parseLine(sampleLine);
        assertThat(pair.getValue0()).isEqualTo(sampleCookie);
        assertThat(pair.getValue1()).isEqualTo(dtSample);
    }

    @Test
    public void parseCSV() throws IOException {
        CSVParser parser =
                CSVParser.parse(sampleLine, CSVFormat.DEFAULT);
        Pair<String, OffsetDateTime> pair =
                DataParser.withDefaults().parseCsvRecord(parser.iterator().next());
        assertThat(pair.getValue0()).isEqualTo(sampleCookie);
        assertThat(pair.getValue1()).isEqualTo(dtSample);
    }

}
